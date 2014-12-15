package jp.hiroaki_mikami.markdown.extension.buildin

import jp.hiroaki_mikami.markdown.extension.{Parameter, Extension}

/**
 * Created by mikami on 14/12/15.
 */
object Graphviz extends Extension {
  val commandName: String = "dot"

  import scala.sys.process._

  override def apply(parameters: Seq[Parameter], body: String): String = {
    (s"echo $body" #| "dot -Tsvg").!!
  }
}
