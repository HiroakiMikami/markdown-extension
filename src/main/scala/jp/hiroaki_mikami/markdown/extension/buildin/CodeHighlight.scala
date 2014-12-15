package jp.hiroaki_mikami.markdown.extension.buildin

import jp.hiroaki_mikami.markdown.extension.{ZeroArityParameter, Parameter, Html, Extension}

/**
 * Created by mikami on 14/12/15.
 */
object CodeHighlight extends Extension {
  val commandName: String = "code"

  override def apply(parameters: Seq[Parameter], body: String): String = {
    val languageName =
      parameters collectFirst {
        case ZeroArityParameter(name) => name
      }
    s"<code ${languageName match {
      case Some(n) => "class=\"" + n + "\""
      case None => ""
    }}><pre>\n" +
    HtmlEncode.encode(body) +
    "</pre></code>"
  }

  override def post(html: Html): Html = {
    Html(html.head +
      """
        |<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.4/styles/default.min.css">
        |<script src="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.4/highlight.min.js"></script>
        |<script>hljs.initHighlightingOnLoad();</script>""".stripMargin,
      html.body)
  }
}
