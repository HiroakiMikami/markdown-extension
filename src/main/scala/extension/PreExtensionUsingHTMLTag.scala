package extension

import collection.JavaConversions._

/** HTMLタグを用いた[[extension.PreExtension]]
  *
  * == 概要 ==
  * 拡張は, [[extension.PreExtensionUsingHTMLTag]].tagのエレメントに対して行われる.
  * 拡張の実際の動作は, [[extension.PreExtensionUsingHTMLTag]].applyメソッドにおいて実装される.
  *
  * == 例 ==
  * {{{
  * scala> import extension._
  * import extension._
  *
  * scala> val extension = new PreExtensionUsingHTMLTag {
  *      |   val tag = "tag"
  *      |   def apply(attr: String, text: String) = {
  *      |     println("attribute: "+ attr)
  *      |     println("text: " + text)
  *      |     text
  *      |   }
  *      | }
  * extension: extension.PreExtensionUsingHTMLTag{val tag: String} = $anon$1@58bcda24
  *
  * scala> extension("""text1
  *      | &lt;tag attr=value&gt;text2&lt;/tag&gt;
  *      | """)
  * attribute:  attr=value
  * text: text2
  * }}}
  * <tag>というHTMLタグのエレメントを拡張対象とする場合は, 上記のようにインスタンスを作ればよく, 上のように引数が与えられる.
  */
trait PreExtensionUsingHTMLTag extends PreExtension {
  /** 拡張を施した後のHTMLエレメントを返す.
    *
    * @param attributes 対象のHTMLタグの属性
    * @param text 対象のHTMLタグの内部の文章
    */
  def apply(attributes: String, text: String): String

  /** 対象となるHTMLタグ. */
  def tag: String

  override def apply(markdown: String): String = {
    /** HTMLタグ */
    abstract class Tag
    /** 開始タグ */
    case class BeginTag(attributes: String) extends Tag
    /** 終了タグ */
    object EndTag extends Tag
    type Element = Either[Tag, String]

    // tagを表す正規表現
    val beginTag = s"([[^\n]|\n]*)<${this.tag}([^>]*)>([[^\n]|\n]*)".r
    val endTag = s"([[^\n]|\n]*)</${this.tag}>([[^\n]|\n]*)".r

    /** 対象のTagごとに分割した結果を返す.
      *
      * @todo HTMLパーサなどを用いてparseしたほうが安全
      * @param text 分割対象のテキスト
      */
    def splitByTag(text: String): Seq[Element] = {
      text match {
        case beginTag(t1, _attr, t2) if _attr == null =>
          splitByTag(t1) ++ List(Left(BeginTag("")): Element) ++ splitByTag(t2)
        case beginTag(t1, _attr, t2) if _attr != null =>
          splitByTag(t1) ++ List(Left(BeginTag(_attr)): Element) ++ splitByTag(t2)
        case endTag(t1, t2) => splitByTag(t1) ++ List(Left(EndTag): Element) ++ splitByTag(t2)
        case _ => List(Right(text))
      }
    }

    // 分割結果をたどりながら拡張を適応する.
    val tree = splitByTag(markdown)
    var text = ""
    var attr = ""
    var buffer = ""
    var depth = 0
    for(elem <- tree) {
      elem match {
        case Right(str) =>
          // 文字列である場合
          if (depth == 0 ) {
            text = text + str
          } else {
            buffer = buffer + str
          }
        case Left(BeginTag(_attr)) =>
          // 開始タグである場合
          if (depth == 0) {
            // 属性の保存
            attr = _attr
          } else {
            // bufferへの追加
            buffer = buffer + s"<${tag} ${attr}>"
          }
          depth = depth + 1
        case Left(EndTag) =>
          // 終了タグである場合
          depth = depth - 1
          if (depth == 0) {
            // 拡張の適応
            text = text + this(attr, buffer)
            // 属性とbufferのクリア
            attr = ""
            buffer = ""
          } else {
            // bufferへの追加
            buffer = buffer + s"</${tag}>"
          }
      }
    }
    text
  }
}
