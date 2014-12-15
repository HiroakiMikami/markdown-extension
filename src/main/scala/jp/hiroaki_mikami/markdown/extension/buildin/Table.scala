package jp.hiroaki_mikami.markdown.extension.buildin

import jp.hiroaki_mikami.markdown.extension.{Parameter, Extension}

/**
 * Created by mikami on 14/12/15.
 */
object Table extends Extension {
  val commandName: String = "table"

  override def apply(parameters: Seq[Parameter], body: String): String = {
    ???
  }
}
