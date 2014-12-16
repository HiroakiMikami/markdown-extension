package jp.hiroaki_mikami.markdown.extension.buildin

import jp.hiroaki_mikami.markdown.extension.{ZeroArityParameter, Parameter, Extension}
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
    parameters collectFirst {
      case ZeroArityParameter(word) => word
    } match {
      case Some(word) =>
        s"$body [google](${urlInGoogle(word)})\n"
      case None =>
        s"[$body](${urlInGoogle(body)})\n"
    }
  }
}