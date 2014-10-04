package data

import scala.xml.Node

/** 文字コードをUTF8と指定する拡張
  *
  * @todo 本来は, [[extension.PostExtension]]として, headエレメントに追加しないといけないのでは？
  */
object CharacterEncodingExtension extends extension.PreExtension {
  def apply(markdown: String): String =
    """<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">${markdown}"""
}
