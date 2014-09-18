#!/bin/sh
#exec scala -nowarn "$0" "$@"
exec scala -savecompiled "$0" "$@"
!#

/** markdownの拡張プラグイン
  * 
  * プラグインはhtmlタグごとに拡張を定義することで実行される.
  */
trait Plugin {
  def isAddBackslash = true
  def prefix: String = ""
  protected[this] def tag: String
  protected[this] def convertText(text: scala.xml.Node): String
  def convert(markdown: scala.xml.Node): Either[String, scala.xml.Node] = {
    def addBackslash(text: String, c: Char) = { text.replace(s"$c", s"\\$c") }
    // タグが対応タグであるかどうかを調べる.
    if(markdown.label == tag) {
      // 対応するtagである場合
      // 変換の実行
      val convertResult = convertText(markdown)

      if (isAddBackslash) {
        // 各種特殊文字の変更
        /* TODO 特殊文字に<, >を入れる場合, htmlのタグすら変換してしまい, おかしなことになる.*/
        Left(List('\\', '(', ')', '[', ']', '*', '_').foldLeft(convertResult)((text, c) => addBackslash(text, c)))
      } else {
	Left(convertResult)
      }
    } else {
      Right(markdown)
    }
  }
}

// プラグインの実装
// latexプラグイン
object LatexPlugin extends Plugin {
  override val prefix: String = """<script
  type="text/javascript"
  src="https://c328740.ssl.cf1.rackcdn.com/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"
></script>"""
  val tag: String = "tex"
  def convertText(text: scala.xml.Node): String = {
      if(text.toString.lines.length == 1) {
        // 1行ならば, \(, \)で囲う
        s"\\(${text.text}\\)"
      } else {
        // 1行ならば, \[, \]で囲う
	s"\\[${text.text}\\]"
      }
  }
}

// dotプラグイン
object DotPlugin extends Plugin {
  val tag: String = "dot"
  // TODO engineを取得できるようになると良いかもしれない.
  def convertText(text: scala.xml.Node): String = {
    // dot言語の取得
    val dot = text.text
    Console.err.println(dot)
    // svgへの変換
    import scala.sys.process._
    s"echo ${dot}" #| "dot -Tsvg" !! //< TODO 環境依存酷い
  }
  override def isAddBackslash = false
}
// codeプラグイン
object CodePlugin extends Plugin {
  val tag: String = "code"
  def convertText(text: scala.xml.Node): String = "<pre>" + text.toString + "</pre>"
  override def isAddBackslash = false
}
val pluginList: List[Plugin] = List(LatexPlugin, DotPlugin, CodePlugin)

// 引数の確認
if (argv.length == 0) {
  Console.println("#usage markdown-extension <MarkdownFileName>")
  exit(1)
}

// markdownの読み込み.
val markdownString = "<markdown>" + (scala.io.Source.fromFile(argv(0))).fold("")((str, line) => s"${str}${line}") + "</markdown>"
// xmlとして扱う.
val markdown = scala.xml.XML.loadString(markdownString)

// pluginを適用する.
def applyPlugin(markdown: scala.xml.Node) = {
    pluginList.foldLeft(Right(markdown): Either[String, scala.xml.Node])(
	(markdown: Either[String, scala.xml.Node], plugin: Plugin) => markdown match {
	    case l@Left(_) => l
	    case Right(node) => plugin.convert(node)
	}
    )
}

// prefixをつける
pluginList foreach ((p: Plugin) => println(p.prefix))
// 整形して表示する. ついでにxmlパースのためのmarkdownタグを外す.
(markdown.child.map(applyPlugin(_))) foreach {
   case Left(str) => print(str)
   case Right(node) => print(node)
}





