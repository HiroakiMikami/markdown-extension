import markdown.Markdown

/** Mainオブジェクト */
object MarkdownExtensionMain {
  import data._
  private val parser =
    new Markdown(List(CharacterEncodingExtension, CodeExtension, LatexExtension, GraphvizExtension), List())

  private def showHelp() =
    Console.println(
      """Usage: markdownE options markdownFile
        |
        |  -m  PreExtensionのみを施したmarkdownを出力する.
        |  -h  すべての拡張を施したHTMLを出力する.
        |  -s  ローカルのHTMLサーバーとして起動する.
        |""".stripMargin)

  private def load(fileName: String): String = scala.io.Source.fromFile(fileName).foldLeft("")((str, line) => s"${str}${line}")

  def main(args: Array[String]) {
    if(args.length <= 1) {
      showHelp()
      System.exit(0)
    }

    args(0) match {
      case "-m" => print(parser.toMarkdown(load(args(1))))
      case "-h" => print(parser.toHTML(load(args(1))))
      case "-s" => ???
      case o: String =>
        Console.println("Invalid Option: " + o)
        showHelp()
    }
  }
}
