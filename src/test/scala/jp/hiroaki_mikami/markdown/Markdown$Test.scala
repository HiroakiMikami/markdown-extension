package jp.hiroaki_mikami.markdown

import org.scalatest.FunSuite

/**
 * Created by mikami on 14/12/13.
 */
class Markdown$Test extends FunSuite {
  test ("toHtml method should convert markdown into html") {
    import Markdown._
    val html =
      toHtml(
        """# Header1
          |text
          |## Header2
          |* list1
          |* list2
          |    1. ordered list1
          |    2. ordered list2
          |""".stripMargin)
    assert (
      """<h1>Header1</h1>
        |
        |<p>text</p>
        |
        |<h2>Header2</h2>
        |
        |<ul>
        |<li>list1</li>
        |<li>list2
        |<ol>
        |<li>ordered list1</li>
        |<li>ordered list2</li>
        |</ol></li>
        |</ul>
        |""".stripMargin == html)
  }
}
