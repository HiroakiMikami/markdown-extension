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

    assert(result.size == markdown.nodes.size)
    for ((expected, actual) <- result zip markdown.nodes) {
      assert(expected == actual)
    }
  }

  test ("parse should extract commandName and body") {
    markdownEquals("""$text{test{}}""", Seq(Command("text", Seq(), "test{}")))

    markdownEquals("""$text{
                     |  test
                     |  \}
                     |}""".stripMargin, Seq(Command("text", Seq(), "  test\n  }")))

    markdownEquals("""markdown
                     |$text{
                     |  test
                     |}""".stripMargin, Seq(MarkdownText("markdown\n"), Command("text", Seq(), "  test")))
    markdownEquals("""test: $test{\\frac{a}{b}}""", Seq(MarkdownText("test: "), Command("test", Seq(), "\\frac{a}{b}")))
    markdownEquals(
      """$code{
        |    void function() {
        |    }
        |}""".stripMargin, Seq(Command("code", Seq(),
        """    void function() {
          |    }""".stripMargin)))
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
        "  test"
      ))
    )
  }

  test ("test bug of 2014/1/3") {
    markdownEquals(
      """$code[scala]{
        |def restructured(urls: Seq[String]) = {
        |  while(tree != treeOld) {
        |  }
        |}
        |}""".stripMargin,
      Seq(Command(
        "code",
        Seq(ZeroArityParameter("scala")),
        """def restructured(urls: Seq[String]) = {
          |  while(tree != treeOld) {
          |  }
          |}""".stripMargin)))
  }
}
