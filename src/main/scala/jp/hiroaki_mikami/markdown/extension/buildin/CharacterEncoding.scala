package jp.hiroaki_mikami.markdown.extension.buildin

import jp.hiroaki_mikami.markdown.extension.{Html, Extension}

/**
 * Created by mikami on 14/12/15.
 */
object CharacterEncoding extends Extension {
  val commandName: String = ""

  override def post(html: Html): Html =
    Html(
      html.head + "\n" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">",
      html.body)
}
