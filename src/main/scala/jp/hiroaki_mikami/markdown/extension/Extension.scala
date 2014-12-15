package jp.hiroaki_mikami.markdown.extension

/**
 * Created by mikami on 14/12/15.
 */
trait Extension {
  val commandName: String
  def pre(eMarkdown: ExtendedMarkdown): ExtendedMarkdown = eMarkdown
  def apply(parameters: Seq[Parameter], body: String): String = body
  def post(html: Html): Html = html
}
