package jp.hiroaki_mikami.markdown.extension

import org.scalatest.FunSuite

/**
 * Created by mikami on 14/12/15.
 */
class ExtendedMarkdownConverterTest extends FunSuite {
  val extensionForTest: Extension = new Extension {
    val commandName: String = "test"

    override def pre(eMarkdown: ExtendedMarkdown): ExtendedMarkdown =
      ExtendedMarkdown(eMarkdown.nodes ++ Seq(MarkdownText("\npre extension")))

    override def apply(parameters: Seq[Parameter], body: String): String = {
      s"parameters.size: ${parameters.size} body: $body"
    }

    override def post(html: Html): Html = Html(html.head + "<title>title</title>", html.body)
  }
  val eMarkdown =
    """#line1
      |$test[param1, param2=test]{line2}
      |line3""".stripMargin
  val converter = new ExtendedMarkdownConverter(extensionForTest :: List(), 4)

  test ("toMarkdown should return markdown text") {
    assert(converter.toMarkdown(eMarkdown) ==
      """<title>title</title>
        |#line1
        |parameters.size: 2 body: line2
        |line3
        |pre extension""".stripMargin)
  }
  test ("toHtml should return html text") {
    assert(converter.toHTML(eMarkdown).toString ==
      """<html>
        |<head>
        |<title>title</title>
        |</head>
        |<body>
        |<h1>line1</h1>
        |
        |<p>parameters.size: 2 body: line2
        |line3
        |pre extension</p>
        |</body>
        |</html>""".stripMargin)
  }

  test ("toHtml should throw Exception if detects undefined command") {
    intercept[UndefinedCommandException](converter.toMarkdown("""$undefined{a}"""))
  }

  test ("toMarkdown should apply extension recursively") {
    val recursiveExtension = new Extension {
      val commandName: String = "rec"

      override def apply(parameters: Seq[Parameter], body: String): String = {
        "$test{" + body + "}"
      }
    }
    val text = """$rec{body}"""
    val converter2 = new ExtendedMarkdownConverter(extensionForTest :: recursiveExtension :: List(), 4)
    assert(converter2.toMarkdown(text) ==
      """<title>title</title>
        |parameters.size: 0 body: body
        |pre extension""".stripMargin)
  }
}
