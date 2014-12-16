package jp.hiroaki_mikami.markdown.extension.config

import jp.hiroaki_mikami.markdown.extension.Extension

/**
 * Created by mikami on 14/12/16.
 */
trait ExtensionConfig {
  val extensions: Seq[Extension]
}
