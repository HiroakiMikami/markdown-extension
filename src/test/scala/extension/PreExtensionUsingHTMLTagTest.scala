package extension

import org.scalatest.FunSuite
import scala.xml.MetaData

class PreExtensionUsingHTMLTagTest extends FunSuite {
  test ("apply method should parse markdown as HTML and find targeted Tag") {
    var isCalled = false
    val testTarget = new PreExtensionUsingHTMLTag {
      def tag: String = "test"
      def apply(attributes: MetaData, text: String): String = {
        val list = attributes.toList
        assert(list.length == 1)
        assert(list(0).key == "attr")
        assert(list(0).value.toString == "value")
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
      """<body>
        |out of tag
        |text 1 > 2text 1 > 2
        |out of tag
        |</body>
      """.stripMargin)
    assert(isCalled)
  }
}
