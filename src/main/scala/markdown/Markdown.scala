package markdown

import extension.{PostExtension, PreExtension}
import java.io.{ByteArrayInputStream, BufferedInputStream}

/** markdownに拡張を施したり, HTMLに変換する.
  *
  * @todo markdownという名前からは, ファイルや文字列を受け取って, toHTML, toMarkdownでtoIntのように変換するほうが適しているのかな.
  *       今の実装なら, MarkdownConverterとかのほうが近そう.
  */
class Markdown(pre: Iterable[PreExtension], post: Iterable[PostExtension]) {
  def toMarkdown(markdown: String): String = pre.foldLeft(markdown)((markdown, extension) => extension(markdown))
  def toHTML(markdown: String): String = {
    // まず通常のmarkdownにする.
    val _mark = toMarkdown(markdown)

    // htmlにする.
    import scala.sys.process._
    val html = ("markdown" #< new BufferedInputStream(new ByteArrayInputStream(_mark.getBytes))).!!

    // postExtensionを適用して文字列化
    post.foldLeft(html)((html, extension) => extension(html))
  }
}
