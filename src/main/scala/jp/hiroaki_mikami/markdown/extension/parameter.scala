package jp.hiroaki_mikami.markdown.extension

import play.api.libs.json.{Json, JsValue}

sealed trait Parameter {
  val name: String
}

case class ZeroArityParameter(name: String) extends Parameter {
  override def toString: String = name
}
case class OneArityParameter(name: String, value: JsValue) extends Parameter {
  override def toString: String = name + "=" + Json.stringify(value)
}
