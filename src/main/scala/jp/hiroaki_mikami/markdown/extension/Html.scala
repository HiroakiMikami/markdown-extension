package jp.hiroaki_mikami.markdown.extension

case class Html(head: String, body: String) {
  override def toString: String =
    """<html>
      |<head>
      |""".stripMargin +
      head +
      """
        |</head>
        |<body>
        |""".stripMargin +
      body +
      """</body>
        |</html>""".stripMargin
}
