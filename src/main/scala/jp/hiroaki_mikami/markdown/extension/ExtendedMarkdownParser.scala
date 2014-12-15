package jp.hiroaki_mikami.markdown.extension

import scala.util.parsing.combinator.RegexParsers
import play.api.libs.json.{Json, JsValue, JsString}

class ExtendedMarkdownParser(tabWidth: Int) {
  private val tab = (0 until tabWidth).foldLeft("")((t, _) => t + " ")
  private def tab2Space(text: String): String = text.replaceAll("\t", tab)

  val commandParser = new RegexParsers {
    import parsing._
    private def escapedChar(avoid: String): Parser[String] = {
      val char = s"[^\\\\$avoid]".r
      val escaped = "\\" ~> ".".r
      char | escaped
    }
    private def escapedString(avoid: String): Parser[String] = escapedChar(avoid).* ^^ (_.foldLeft("")(_+_))

    private val literal: Parser[String] = {
      val emptyString: Parser[String] = "\"\"".r ^^^ ""
      val literal: Parser[String] = "\"".r ~> escapedString("\"\n") <~ "\"".r
      emptyString | literal
    }
    private def bracket(begin: Pattern, end: Pattern): Parser[String] = {
      val str = escapedString(begin.regex + end.regex + "\n")
      begin.str ~> str <~ end.str |
        (begin.str ~> str ~ bracket(begin, end) ~ str <~ end.str ^^ (x => {
          x._1._1 + begin.str + x._1._2 + end.str + x._2
        }))
    }
    private def multiLineBracket(begin: Pattern, end: Pattern): Parser[String] = {
      val str = escapedString(begin.regex + end.regex)
      begin.str ~> str <~ end.str |
        (begin.str ~> str ~ multiLineBracket(begin, end) ~ str <~ end.str ^^ (x => {
          x._1._1 + begin.str + x._1._2 + end.str + x._2
        }))
    }

    private val commandName: Parser[String] = "([^\\[\\{$])+".r ^^ {x =>
      x.replaceAll(this.whiteSpace.toString, "")
    }
    private val paramName: Parser[String] = "([^=,\\]])+".r ^^ {x =>
      x.replaceAll(this.whiteSpace.toString, "")
    }
    private val zeroArity: Parser[ZeroArityParameter] = paramName ^^ ZeroArityParameter
    private val argument: Parser[JsValue] = {
      (literal | "([^,\"\\]])+".r) ^^ (x => {
        try {
          Json.parse(x)
        } catch {
          case e: Throwable =>
            JsString(x)
        }
      })
    }
    private val oneArity: Parser[OneArityParameter] = paramName ~ "=" ~ argument ^^ (x => {
      OneArityParameter(x._1._1, x._2)
    })
    private val parameter: Parser[Parameter] = oneArity | zeroArity
    private def parameters: Parser[Seq[Parameter]] = {
      def seq: Parser[Seq[Parameter]] =
        (parameter ~ "," ~ seq ^^ (x => Seq(x._1._1) ++ x._2)) | (parameter ^^ (x => Seq(x)))
      "[" ~> seq <~ "]"
    }
    val body: Parser[String] = multiLineBracket(Pattern("{", "\\{"), Pattern("}", "\\}"))
    def command: Parser[Command] =
      ("$" ~> commandName ~ body ^^ (x => Command(x._1, Seq(), x._2))) |
        ("$" ~> commandName ~ parameters ~ body ^^ (x => Command(x._1._1, x._1._2, x._2)))
    def apply(text: String) = this.parse(command, text)

    override def skipWhitespace: Boolean = false
  }

  def parse(text: String): ExtendedMarkdown = {
    def iterativeParse(text: String, nodes: Seq[Node]): Seq[Node] = {
      val index = text.indexOf("$")
      if (index < 0) {
        // $がない.
        nodes ++ Seq(MarkdownText(text))
      } else {
        val text1 = text.substring(0, index)
        val text2 = text.substring(index)
        commandParser(text2) match {
          case s: commandParser.Success[Command] =>
            // 先頭がCommandである
            if (s.next.atEnd) {
              nodes ++ Seq(MarkdownText(text1), s.get)
            } else {
              iterativeParse(s.next.source.toString.substring(s.next.offset), nodes ++ Seq(MarkdownText(text1), s.get))
            }
          case _ =>
            // 先頭はCommandでない
            iterativeParse(text2.substring(1), nodes ++ Seq(MarkdownText(text1 + "$")))
        }
      }
    }
    val sequence: Seq[Node] = iterativeParse(tab2Space(text), Seq())
    def canonical: Seq[Node] = {
      sequence.foldLeft(Seq[Node]())((seq, node) => {
        node match {
          case MarkdownText(text1) =>
            if (seq.isEmpty) {
              seq ++ Seq(node)
            } else {
              seq.last match {
                case MarkdownText(text2) =>
                  seq.init ++ Seq(MarkdownText(text1 + text2))
                case c: Command =>
                  seq ++ Seq(node)
              }
            }
          case c: Command => seq ++ Seq(c)
        }
      }) filter {
        case MarkdownText(text2) if text2 == "" => false
        case _ => true
      }
    }
    ExtendedMarkdown(canonical)
  }
}
