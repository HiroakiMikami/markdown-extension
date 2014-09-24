#!/bin/sh
exec scala -nowarn "$0" "$@"
#exec scala -savecompiled "$0" "$@"
!#

/** markdownの拡張プラグイン
  * 
  * プラグインはhtmlタグごとに拡張を定義することで実行される.
  */
trait Plugin {
  def prefix: String = ""
  protected[this] def tag: String
  protected[this] def convertTargetedText(attributes: String, text: String): String
  // TODO attributesをちゃんとパースして, HashMapとかにしたほうが良い？
  def convert(markdown: String): String = {
    // tagを表す正規表現
    val beginTag = s"([[^\n]|\n]*)<${tag}([^>])*>([[^\n]|\n]*)".r
    val endTag = s"([[^\n]|\n]*)</${tag}>([[^\n]|\n]*)".r
    // Tagを表すデータ構造
    abstract class Tag
    case class BeginTag(attributes: String) extends Tag
    object EndTag extends Tag
    type Element = Either[Tag, String]
    // Tagごとに分割する関数
    def splitByTag(text: String): Seq[Element] = {
      text match {
	case beginTag(t1, attr, t2) if attr == null => splitByTag(t1) ++ List(Left(BeginTag("")): Element) ++ splitByTag(t2)
	case beginTag(t1, attr, t2) if attr != null => splitByTag(t1) ++ List(Left(BeginTag(attr)): Element) ++ splitByTag(t2)
	case endTag(t1, t2) => splitByTag(t1) ++ List(Left(EndTag): Element) ++ splitByTag(t2)
	case _ => List(Right(text))
      }
    }

    val tree = splitByTag(markdown)
    var text = ""
    var attr = ""
    var buffer = ""
    var depth = 0
    for(elem <- tree) {
      elem match {
	case Right(str) => 
	  if (depth == 0 ) {
	    text = text + str
	  } else {
	    buffer = buffer + str
	  }
	case Left(BeginTag(_attr)) => 
	  if (depth == 0) {
	    // 属性の保存
	    attr = _attr
	  } else {
	    // bufferへの追加
	    buffer = buffer + s"<${tag} ${attr}>"
	  }
	  depth = depth + 1
	case Left(EndTag) => 
	  depth = depth - 1
	  if (depth == 0) {
	    // 文字の変換
	    text = text + convertTargetedText(attr, buffer)
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

// プラグインの実装
// latexプラグイン
object LatexPlugin extends Plugin {
  override val prefix: String = """<script
  type="text/javascript"
  src="https://c328740.ssl.cf1.rackcdn.com/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"
></script>"""
  val tag: String = "tex"
  def convertTargetedText(attributes: String, text: String): String = {
    def addBackslash(text: String, c: Char) = { text.replace(s"$c", s"\\$c") }
    // 各種特殊文字の変更
    val textAddedBackslash = List('\\', '(', ')', '[', ']', '*', '_').foldLeft(text)((text, c) => addBackslash(text, c))

    if(textAddedBackslash.lines.length == 1) {
      // 1行ならば, \(, \)で囲う
      s"\\\\(${textAddedBackslash}\\\\)"
    } else {
      // 1行ならば, \[, \]で囲う
      s"\\\\[${textAddedBackslash}\\\\]"
    }
  }
}

// dotプラグイン
object DotPlugin extends Plugin {
  val tag: String = "dot"
  // TODO engineを取得できるようになると良いかもしれない.
  def convertTargetedText(attributes: String, text: String): String = {
    // dot言語の取得
    val dot = text
    // svgへの変換
    import scala.sys.process._
    s"echo ${dot}" #| "dot -Tsvg" !! //< TODO 環境依存酷い
  }
}
// codeプラグイン
object CodePlugin extends Plugin {
  val tag: String = "code"
  def convertTargetedText(attributes: String, text: String): String = s"<pre><code ${attributes}>${text}</code></pre>"
}
val pluginList: List[Plugin] = List(LatexPlugin, DotPlugin, CodePlugin)

// 引数の確認
if (args.length == 0) {
  Console.println("#usage markdown-extension <MarkdownFileName>")
  System.exit(1)
}

// markdownファイルの読み込み.
val markdownString = (scala.io.Source.fromFile(args(0))).fold("")((str, line) => s"${str}${line}")

// prefixをつける
pluginList foreach ((p: Plugin) => println(p.prefix))
// pluginの適用
print(pluginList.foldLeft(markdownString)((str, plugin) => plugin.convert(s"${str}")))

/*
// xmlとして扱う.
val markdown = scala.xml.XML.loadString(markdownString)

// pluginを適用する.
def applyPlugin(markdown: scala.xml.Node) = {
    pluginList.foldLeft(Right(markdown): Either[String, scala.xml.Node])(
	(markdown: Either[String, scala.xml.Node], plugin: Plugin) => markdown match {
	    case l@Left(_) => l
	    case Right(node) => plugin.convert(node)
	}
    )
}

// prefixをつける
pluginList foreach ((p: Plugin) => println(p.prefix))
// 整形して表示する. ついでにxmlパースのためのmarkdownタグを外す.
(markdown.child.map(applyPlugin(_))) foreach {
   case Left(str) => print(str)
   case Right(node) => print(node)
}
*/





