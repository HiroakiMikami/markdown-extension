package jp.hiroaki_mikami.markdown.extension.buildin

import org.scalatest.FunSuite
import jp.hiroaki_mikami.markdown.extension.Html

/**
 * Created by mikami on 14/12/15.
 */
class Headline$Test extends FunSuite {
  test ("headline extension test") {
    val actual =  Headline.post(Html("",
      """<h1>Title(1)</h1>
        |<h2>Title(1-1)</h2>
        |<h1>Title(2)</h1>
        |<h2>Title(2-1)</h2>
        |<h2>Title(2-2)</h2>
        |""".stripMargin))
    val expected = Html(
      """
        |<style type="text/css" media="screen, projection">
        |html, body {
        |  display: block;
        |  height: 100%;
        |  margin: 0;
        |  padding: 0;
        |  border: none 0;
        |}
        |div.headline {
        |  z-index: 0;
        |  top: 0%;
        |  left: 0%;
        |  width: 30%;
        |  height: 100%;
        |  margin: 0;
        |  padding: 0;
        |  border: none 0;
        |  position:absolute;
        |}
        |div.body {
        |  z-index: 0;
        |  top: 0%;
        |  left: 30%;
        |  width: 70%;
        |  height: 100%;
        |  margin: 0;
        |  padding: 0;
        |  border: none 0;
        |  position:absolute;
        |}
        |@media screen {
        |  div.headline {
        |    height: 100%;
        |    overflow: scroll;
        |  }
        |  div.body {
        |    height: 100%;
        |    overflow: scroll;
        |  }
        |}
        |@media projection {
        |  div.headline {
        |    height: 100%;
        |    overflow: scroll;
        |  }
        |  div.body {
        |    height: 100%;
        |    overflow: scroll;
        |  }
        |}
        |</style>
        |""".stripMargin,
      """<div class ="headline">
        |<ul>
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
        |
        |</div>
        |<div class="body">
        |
        |<h1 id="1">Title(1)</h1>
        |<h2 id="2">Title(1-1)</h2>
        |<h1 id="3">Title(2)</h1>
        |<h2 id="4">Title(2-1)</h2>
        |<h2 id="5">Title(2-2)</h2>
        |</div>
        |""".stripMargin)
    assert(actual.head == expected.head)
    assert(actual.body == expected.body)
  }
}
