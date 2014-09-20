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
  protected[this] def convertTargetedText(tag: String, text: String): String
  def convert(markdown: String): String = {
    // 対応するタグを調べて変換する.
    val startTagExp = "<${tag}(.*)>(.*)".r
    val endTagExp   = "(.*)</${tag}>(.*)".r
    
    def findTag(text: String): String = {
      text
    }

    markdown

/*
    // タグが対応タグであるかどうかを調べる.
    if(markdown.label == tag) {
      // 対応するtagである場合
      // 変換の実行
      val convertResult = convertTargetedText(markdown)

      if (isAddBackslash) {
        // 各種特殊文字の変更
        /* TODO 特殊文字に<, >を入れる場合, htmlのタグすら変換してしまい, おかしなことになる.*/
        List('\\', '(', ')', '[', ']', '*', '_').foldLeft(convertResult)((text, c) => addBackslash(text, c))
      } else {
	convertResult
      }
    } else {
      markdown
    }
*/
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
  def convertTargetedText(tag: String, text: String): String = {
    def addBackslash(text: String, c: Char) = { text.replace(s"$c", s"\\$c") }
    // 各種特殊文字の変更
    /* TODO 特殊文字に<, >を入れる場合, htmlのタグすら変換してしまい, おかしなことになる.*/
    val textAddedBackslash = List('\\', '(', ')', '[', ']', '*', '_').foldLeft(text)((text, c) => addBackslash(text, c))

    if(textAddedBackslash.lines.length == 1) {
      // 1行ならば, \(, \)で囲う
      s"\\(${textAddedBackslash}\\)"
    } else {
      // 1行ならば, \[, \]で囲う
      s"\\[${textAddedBackslash}\\]"
    }
  }
}

// dotプラグイン
object DotPlugin extends Plugin {
  val tag: String = "dot"
  // TODO engineを取得できるようになると良いかもしれない.
  def convertTargetedText(tag: String, text: String): String = {
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
  def convertTargetedText(tag: String, text: String): String = s"<pre><${tag}>${text}</code></pre>"
}
val pluginList: List[Plugin] = List(LatexPlugin, DotPlugin, CodePlugin)

// 引数の確認
if (argv.length == 0) {
  Console.println("#usage markdown-extension <MarkdownFileName>")
  exit(1)
}

// markdownファイルの読み込み.
val markdownString = (scala.io.Source.fromFile(argv(0))).fold("")((str, line) => s"${str}${line}")

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





