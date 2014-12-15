package jp.hiroaki_mikami.markdown.extension

import jp.hiroaki_mikami.markdown.Markdown

/**
 * Created by mikami on 14/12/15.
 */
class ExtendedMarkdownConverter(extensions: Seq[Extension], tabWidth: Int) {
  private val extensionMap = extensions.map(e => e.commandName -> e).toMap

  private def parse(eMarkdown: String): ExtendedMarkdown = new ExtendedMarkdownParser(tabWidth).parse(eMarkdown)
  private def pre(eMarkdown: ExtendedMarkdown): ExtendedMarkdown = extensions.foldLeft(eMarkdown)((markdown, extension) => {
    extension.pre(markdown)
  })
  private def post(html: Html): Html = extensions.foldLeft(html)((html, extension) => {
    extension.post(html)
  })
  private def convert(eMarkdown: ExtendedMarkdown): ExtendedMarkdown = {
    ExtendedMarkdown(eMarkdown.nodes.map {
      case m: MarkdownText => Seq(m)
      case c: Command =>
        if (extensionMap contains c.commandName) {
          convert(parse(extensionMap(c.commandName).apply(c.parameters, c.body))).nodes
        } else {
          throw new UndefinedCommandException(c.commandName)
        }
    }.flatten)
  }

  def toMarkdown(eMarkdown: String): String = {
    val result = convert(pre(parse(eMarkdown)))

    val html = post(Html("", result.toString))
    html.head + "\n" + html.body
  }

  def toHTML(eMarkdown: String): Html = {
    val result = convert(pre(parse(eMarkdown)))

    post(Html("", Markdown.toHtml(result.toString)))
  }
}
