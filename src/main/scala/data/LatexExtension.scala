package data

/** Latex(MathJax)を簡単に埋め込めるようにする拡張.
  *
  * @todo サンプルがあったほうがいいかも.
  * \<tex\>タグでLatexの式を囲えば, MathJaxで扱える式に変換され, さらにMathJax用のscriptも追加される.
  */
object LatexExtension { 
  object PreExtension extends extension.PreExtensionUsingHTMLTag {
    override def apply(attr: String, text: String): String  = {
      def addBackslash(text: String, c: Char) = { text.replace(s"$c", s"\\$c") }
      // 各種特殊文字の変更
      val textAddedBackslash = List('\\', '(', ')', '[', ']', '*', '_').foldLeft(text)((text, c) => addBackslash(text, c))

      if(text.lines.length <= 1) {
        s"""\\\\($textAddedBackslash\\\\)"""
      } else {
        s"""\\\\[$textAddedBackslash\\\\]"""
      }
    }
    override val tag = "tex"
  }
  object PostExtension extends extension.PostExtension {
    def apply(html: String): String = {
      // MathJax用のscriptを埋め込む.
      """<script
        |  type="text/javascript"
        |  src="https://c328740.ssl.cf1.rackcdn.com/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"
        |></script>
        |""".stripMargin + html
    }
  }
}
