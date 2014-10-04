package extension

import org.scalatest.FunSuite
import scala.xml.MetaData

class PreExtensionUsingHTMLTagTest extends FunSuite {
  test ("apply method should parse markdown as HTML and find targeted Tag") {
    var isCalled = false
    val testTarget = new PreExtensionUsingHTMLTag {
      val tag: String = "test"
      def apply(attributes: String, text: String): String = {
        assert(attributes == " attr=\"value\"")
        assert(text == "text 1 > 2")
        isCalled = true
        text + text
      }
    }
    val result = testTarget(
      """
        |out of tag
        |<test attr="value">text 1 > 2</test>
        |out of tag
      """.stripMargin)

    assert(result ==
      """
        |out of tag
        |text 1 > 2text 1 > 2
        |out of tag
      """.stripMargin)
    assert(isCalled)
  }
}
