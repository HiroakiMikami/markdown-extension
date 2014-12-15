package jp.hiroaki_mikami.markdown.extension.buildin

import org.scalatest.FunSuite

/**
 * Created by mikami on 14/12/15.
 */
class CodeHighlight$Test extends FunSuite {
  test ("test html escape") {
    assert(CodeHighlight(Seq(), "1 < 2") ==
      """<code ><pre>
        |1 &lt; 2</pre></code>""".stripMargin)

  }
}
