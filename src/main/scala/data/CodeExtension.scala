package data

/** プログラムのコードを簡単に埋め込めるようにする拡張.
  *
  * @todo Syntaxのハイライトがほしい.
  * @todo サンプルがあったほうが良いかもしれない.
  * \<code\>タグで囲うと, そこがプログラムのソースコードであると解釈される. また, class属性によって, 言語も指定できる.
  * 例えば, \<code class="scala">と書けば, scalaのソースコードであると設定できる.
  */
object CodeExtension extends extension.PreExtensionUsingHTMLTag{
  def apply(attributes: String, text: String): String = s"<pre><code ${attributes}>${text}</code></pre>"
  def tag: String = "code"
}