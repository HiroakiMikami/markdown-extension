package extension

/** HTMLへ変換した後に施されるmarkdown拡張
  */
trait PreExtension {
  /** 拡張を施した後のmarkdownテキストを返す.
    *
    * @param markdown 拡張前のmarkdownテキスト
    */
  def apply(markdown: String): String
}