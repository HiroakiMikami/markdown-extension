package jp.hiroaki_mikami.markdown.extension

import org.scalatest.FunSuite
import play.api.libs.json.{JsString, Json}

/**
 * Created by mikami on 14/12/13.
 */
class ExtendedMarkdownParserTest extends FunSuite {
  val parser = new ExtendedMarkdownParser(4)
  import parser._

  def markdownEquals(text: String, result: Seq[Node]) {
    val markdown = parse(text)
    assert(markdown.nodes == result)
  }

  test ("parse should extract commandName and body") {
    markdownEquals("""$text{test{}}""", Seq(Command("text", Seq(), "test{}")))

    markdownEquals("""$text{
                     |  test
                     |  \}
                     |}""".stripMargin, Seq(Command("text", Seq(), "\n  test\n  }\n")))

    markdownEquals("""markdown
                     |$text{
                     |  test
                     |}""".stripMargin, Seq(MarkdownText("markdown\n"), Command("text", Seq(), "\n  test\n")))
    markdownEquals("""test: $test{\\frac{a}{b}}""", Seq(MarkdownText("test: "), Command("test", Seq(), "\\frac{a}{b}")))
  }

  test ("parse method should extract parameters") {
    markdownEquals(
      """$text[p1, p2=arg, p3="{\"json\": \"value\", \"arr\": [1, 2, 3]}"]{
        |  test
        |}""".stripMargin,
      Seq(Command(
        "text",
        Seq(
          ZeroArityParameter("p1"),
          OneArityParameter("p2", JsString("arg")),
          OneArityParameter("p3", Json.obj("json"->"value", "arr"->Json.arr(1, 2, 3)))
        ),
        "\n  test\n"
      ))
    )
  }
}
