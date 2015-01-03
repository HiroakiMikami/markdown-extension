package jp.hiroaki_mikami.markdown.extension.buildin

import jp.hiroaki_mikami.markdown.extension.{Html, Extension}
import jp.hiroaki_mikami.markdown.Markdown

/**
 * Created by mikami on 14/12/15.
 */
object Headline extends Extension {
  val commandName: String = ""

  override def post(html: Html): Html = {
    var _id = 0
    def makeId() = {
      _id = _id + 1
      _id
    }

    /*
    手法概略
    1. <h[1-9]></h[1-9]>で囲われた箇所を探す.
       この時, JOptionPaneやscalaのXMLではsvg部分をパースできないので, 独自にやる必要がある.
       @todo 高機能なHTTPパーサなら解決できるかもしれない
    2. 囲われた箇所のタイトル(t)を取得, 同時にidを適当に生成し,
       <h[1-9]>タグにname=id属性を付加する.
    3. リストで囲いながら, <a href="#id">t</a>を生成する.
       リストを作るのはmarkdownのほうが楽かも.
    4. tableを利用して分割表示する.
     */

    /*
     h[1-9]タグを探し,id属性をつける.
     条件として, 1行に収まっているかつ<h1><h2></h2></h1>のような入れ子がないことを用いることができると思う.
     */
    val hTag = """(.*)<h([1-9])>(.*)</h[1-9]>(.*)""".r
    case class Header(num: Int, id: Int, title: String)
    var headerList = scala.collection.mutable.ListBuffer[Header]()

    val htmlWithName: String = html.body.lines.foldLeft("")((html, line) => line match {
      case hTag(prefix, num, title, suffix) =>
        val id = makeId()
        headerList += Header(num.toInt, id, title)
        html + s"""\n$prefix<h$num id="$id">$title</h$num>$suffix"""
      case _line: String =>
        html+"\n"+_line
    })

    def indent(depth: Int): String = {
      if (depth == 0) {
        ""
      } else {
        "    " + indent(depth-1)
      }
    }

    /*
    リストを作る.
     */
    val listMarkdown = headerList.foldLeft("")((line, header) => {
      s"""$line
        |${indent(header.num-1)}* [${header.title}](#${header.id})""".stripMargin
    })
    val list = Markdown.toHtml(listMarkdown)
    Html(html.head + """
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
      s"""<div class ="headline">
        |$list
        |</div>
        |<div class="body">
        |$htmlWithName
        |</div>
        |""".stripMargin)
  }
}
