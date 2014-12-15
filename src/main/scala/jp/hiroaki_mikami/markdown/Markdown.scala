package jp.hiroaki_mikami.markdown

import java.io.{ByteArrayInputStream, BufferedInputStream}

object Markdown {
  def toHtml(markdown: String): String = {
    // htmlにする.
    import scala.sys.process._
    ("markdown" #< new BufferedInputStream(new ByteArrayInputStream(markdown.getBytes))).!!
  }
}
