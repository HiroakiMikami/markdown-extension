package data

import extension.PostExtension
import markdown.Markdown

/** HTMLに見出しをつける.
  *
  * @todo tableでやるのは簡単だが, リンクがうまく飛ばない. できれば, Javascriptによる動的なframe生成っぽいことが望ましい？
  */
object HeadlineExtension extends PostExtension {
  def apply(html: String): String = {
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

    val htmlWithName = (html.lines).foldLeft("")((html, line) => line match {
      case hTag(prefix, num, title, suffix) =>
        val id = makeId()
        headerList += Header(num.toInt, id, title)
        html + s"""\n${prefix}<h${num} name="${id}">${title}</h${num}>${suffix}"""
      case _line: String => html+"\n"+_line
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
      s"""${line}
        |${indent(header.num-1)}* [${header.title}](#${header.id})""".stripMargin
    })
    val list = new Markdown(List(), List()).toHTML(listMarkdown)

    // 表にする.
    s"""<table border=1>
      |<tr>
      |<td>${list}</td>
      |<td>${htmlWithName}</td>
      |</tr>
      |</table>""".stripMargin
  }
}
