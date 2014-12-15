package jp.hiroaki_mikami.markdown.extension

/**
 * Created by mikami on 14/12/14.
 */
sealed trait Node
case class MarkdownText(text: String) extends Node {
  override def toString: String = text
}

case class Command(commandName: String, parameters: Seq[Parameter], body: String) extends Node {
  override def toString: String = {
    val params =
      if (parameters.size == 0) {
        ""
      } else {
        parameters.foldLeft("[")(_ + _.toString + ",").init + "]"
      }
    "$" + commandName + params + "{" + body + "}"
  }
}
