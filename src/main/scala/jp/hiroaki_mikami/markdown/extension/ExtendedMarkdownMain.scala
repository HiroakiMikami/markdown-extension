package jp.hiroaki_mikami.markdown.extension

import org.apache.commons.cli.{HelpFormatter, BasicParser, Options}
import jp.hiroaki_mikami.markdown.extension.config.{ExtensionConfig, DefaultConfig}
//import com.twitter.util.Eval

/**
 * Created by mikami on 14/12/15.
 */
object ExtendedMarkdownMain extends App {
  // prepare parser
  val commandLine = """emarkdown [option]... [file]"""
  val options = new Options().
    addOption("c", "config", true, "set config file").
    addOption("h", "help", false, "print help message").
    addOption("m", "markdown", false, "output markdown text").
    addOption("t", "tab", true, "set tab width")

  // parse program
  val parser = new BasicParser()
  val cl = parser.parse(options, args)

  // show help message
  if (cl.hasOption("h") || cl.hasOption("help")) {
    new HelpFormatter().printHelp(commandLine, options)
  } else {
    def getValue(option: String) =
      if (cl.hasOption(option)) {
        Some(cl.getOptionValue(option))
      } else {
        None
      }

    // set config file and make converter
    val file = getValue("config")

    val extensions = file match {
      case Some(f) => ??? //Eval[ExtensionConfig](new File(f))
      case None => new DefaultConfig
    }

    val tabWidth = getValue("tab").map(_.toInt).getOrElse(4)
    val converter = new ExtendedMarkdownConverter(extensions.extensions, tabWidth)

    // get extended markdown text
    import scala.sys.process._
    val text =
      if (cl.getArgs.length != 0) {
        // fileから
        val file = cl.getArgs()(0)
        s"cat $file".!!
      } else {
        // stdinから
        ("cat " #< System.in).!!
      }

    // convert
    val result =
      if (cl.hasOption("markdown")) {
        converter.toMarkdown(text)
      } else {
        converter.toHTML(text)
      }

    // output
    print(result)
  }
}
