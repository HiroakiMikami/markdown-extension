package jp.hiroaki_mikami.markdown.extension

case class ExtendedMarkdown(nodes: Seq[Node]) {
  override def toString: String = nodes.foldLeft("")(_+_)
}
