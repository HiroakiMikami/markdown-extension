package extension

/** HTMLへ変換された後に施されるmarkdown拡張 */
trait PostExtension {
  /** 拡張を施した結果のHTMLを返す.
    *
    * @param html markdownから変換されたHTML
    */
  def apply(html: scala.xml.Node): scala.xml.Node
}
