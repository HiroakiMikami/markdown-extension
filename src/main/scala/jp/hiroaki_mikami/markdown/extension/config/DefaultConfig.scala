package jp.hiroaki_mikami.markdown.extension.config

import jp.hiroaki_mikami.markdown.extension.Extension
import jp.hiroaki_mikami.markdown.extension.buildin._

/**
 * Created by mikami on 14/12/16.
 */
class DefaultConfig extends ExtensionConfig {
  val extensions: Seq[Extension] = CharacterEncoding :: CodeHighlight :: Google :: Graphviz :: Headline :: Latex :: Nil
}
