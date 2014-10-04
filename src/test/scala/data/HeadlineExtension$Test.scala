package data

import org.scalatest.FunSuite

/**
 * Created by mikami on 14/10/04.
 */
class HeadlineExtension$Test extends FunSuite {
  test("should return html with headline") {
    assert(HeadlineExtension(
      """<h1>Title(1)</h1>
        |<h2>Title(1-1)</h2>
        |<h1>Title(2)</h1>
        |<h2>Title(2-1)</h2>
        |<h2>Title(2-2)</h2>
      """.stripMargin) == """<table border=1>
                                        |<tr>
                                        |<td><ul>
                                        |<li><a href="#1">Title(1)</a>
                                        |<ul>
                                        |<li><a href="#2">Title(1-1)</a></li>
                                        |</ul></li>
                                        |<li><a href="#3">Title(2)</a>
                                        |<ul>
                                        |<li><a href="#4">Title(2-1)</a></li>
                                        |<li><a href="#5">Title(2-2)</a></li>
                                        |</ul></li>
                                        |</ul>
                                        |</td>
                                        |<td>
                                        |<h1 name="1">Title(1)</h1>
                                        |<h2 name="2">Title(1-1)</h2>
                                        |<h1 name="3">Title(2)</h1>
                                        |<h2 name="4">Title(2-1)</h2>
                                        |<h2 name="5">Title(2-2)</h2>
                                        |      </td>
                                        |</tr>
                                        |</table>""".stripMargin)
  }
}
