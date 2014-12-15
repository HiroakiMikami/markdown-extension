package jp.hiroaki_mikami.markdown.extension.buildin

import jp.hiroaki_mikami.markdown.extension.{Parameter, Extension}
import java.net.URLEncoder

/**
 * Created by mikami on 14/12/15.
 */
object Google extends Extension {
  val commandName: String = "google"

  def urlInGoogle(str: String) = {
    "https://www.google.co.jp/?#q=" + URLEncoder.encode(str, "utf-8")
  }

  override def apply(parameters: Seq[Parameter], body: String): String = {
    s"[$body](${urlInGoogle(body)})\n"
  }
}