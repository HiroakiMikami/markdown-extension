package extension

import scala.xml._

/** HTMLタグを用いた[[extension.PreExtension]]
  *
  * == 概要 ==
  * 拡張は, [[extension.PreExtensionUsingHTMLTag]].tagのエレメントに対して行われる.
  * 拡張の実際の動作は, [[extension.PreExtensionUsingHTMLTag]].applyメソッドにおいて実装される.
  *
  * 例えば,
  * {{{
  *   text1
  *   <tag attr=value>text2</tag>
  * }}}
  * というmarkdownに対し, [[extension.PreExtensionUsingHTMLTag]].tagが"tag"であれば,
  */
trait PreExtensionUsingHTMLTag extends PreExtension {
  def apply(attributes: MetaData, text: String): String
  def tag: String
  override def apply(markdown: String): String = {
    /*
    markdownをHTMLに変換する.
    一度[[javax.swing.JEditorPane]]を介することで, 曖昧なhtmlであっても解釈ができる.
     */
    val xml = scala.xml.XML.loadString(new javax.swing.JEditorPane("text/html", markdown).getText)
    // TODO htmlに直すときに改行が消えている.

    // [[extension.PreExtensionUsingHTMLTag]].tagに当てはまるelementに変換を施す.
    Elem(xml.prefix, "body", xml.attributes, xml.scope, xml.minimizeEmpty, (xml \\ "body")(0).child map {
      case e: Elem if e.label == this.tag  =>
        new Atom(this.apply(e.attributes, e.text))
      case n: Node => n
    }: _*).toString
  }
}
