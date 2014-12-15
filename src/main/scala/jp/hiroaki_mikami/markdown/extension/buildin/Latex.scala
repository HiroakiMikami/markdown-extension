package jp.hiroaki_mikami.markdown.extension.buildin

import jp.hiroaki_mikami.markdown.extension.{Html, ExtendedMarkdown, Parameter, Extension}

/**
 * Created by mikami on 14/12/15.
 */
object Latex extends Extension {
  // todo html encodeいるのかな？ >を打つときにMathJaxはどうすればいいのかよくわからない.
  val commandName: String = "tex"

  override def post(html: Html): Html =
    Html(html.head +
      """
        |<script
        |  type="text/javascript"
        |  src="https://c328740.ssl.cf1.rackcdn.com/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"
        |></script>
        |""".stripMargin, html.body)

  override def apply(parameters: Seq[Parameter], body: String): String = {
    def addBackslash(text: String, c: Char) = { text.replace(s"$c", s"\\$c") }
    // 各種特殊文字の変更
    val textAddedBackslash = List('\\', '(', ')', '[', ']', '*', '_').foldLeft(body)((text, c) => addBackslash(text, c))

    if(body.lines.length <= 1) {
      s"""\\\\($textAddedBackslash\\\\)"""
    } else {
      s"""\\\\[$textAddedBackslash\\\\]"""
    }
  }
}
