package data

import scala.xml.Node

/** 文字コードをUTF8と指定する拡張
  *
  * @todo headエレメントに追加しているわけではないので, おそらく[[data.HeadlineExtension.]]との適用順によってはうまく働かない.
  */
object CharacterEncodingExtension extends extension.PostExtension {
  def apply(html: String): String =
    s"""<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    |${html}""".stripMargin
}
