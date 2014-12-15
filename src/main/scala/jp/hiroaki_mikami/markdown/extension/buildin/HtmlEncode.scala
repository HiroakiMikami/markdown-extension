package jp.hiroaki_mikami.markdown.extension.buildin

/**
 * Created by mikami on 14/12/15.
 */
object HtmlEncode {
  private val htmlEnc = Array('&' -> "&amp;", '"' -> "&quot;", '<' -> "&lt;", '>' -> "&gt;")
  def encode(strIn: String): String = {
    // play frameworkとかに絶対methodあると思う.
    // HTMLエンコード処理
    val strOut = new StringBuffer(strIn)
    // エンコードが必要な文字を順番に処理
    for ((c, s) <- htmlEnc) {
      // エンコードが必要な文字の検索
      var idx = strOut.toString.indexOf(c)

      while (idx != -1) {
        // エンコードが必要な文字の置換
        strOut.setCharAt(idx, s.charAt(0))
        strOut.insert(idx + 1, s.substring(1))

        // 次のエンコードが必要な文字の検索
        idx = idx + s.length()
        idx = strOut.toString.indexOf(c, idx)
      }
    }
    strOut.toString
  }
}
