package jp.hiroaki_mikami.markdown.extension.buildin

import org.scalatest.FunSuite

/**
 * Created by mikami on 14/12/15.
 */
class Table$Test extends FunSuite {
  test ("should convert text to table(html)") {
    println("Not implemented yet.")
/*
    assert(
      Table(Seq(),
        """+-----+-----------+
          ||cell1|cell2      |
          ||     +-----+-----+
          ||     |cell3|cell4|
          |+-----+-----+-----+
          |""".stripMargin) ==
        """<table border="1">
          |<tr><td rowspan="2">cell1</td><td colspan="2">cell2</td></tr>
          |<tr><td>cell3</td><td>cell4</td>
          |</table>""".stripMargin
    )
    */
  }
}
