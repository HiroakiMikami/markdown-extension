package data

import scala.sys.process._

/** Graphviz(dot言語)を簡単に埋め込めるようにする拡張.
  *
  * @todo サンプルがあったほうがいいかも.
  * @todo engineを取得できるようになると良いかもしれない.
  * \<dot\>タグでDOT言語の式を囲えば，SVG形式に変換される.
  */
object GraphvizExtension extends extension.PreExtensionUsingHTMLTag {
  /**
    * @todo dotのインストール必須. 非常に環境依存性が強い.
    */
  def apply(attributes: String, text: String): String = {
    println("graphviz")
    s"echo ${text}" #| "dot -Tsvg" !!
  }
  def tag: String = "dot"
}
