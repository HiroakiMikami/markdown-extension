package server

import org.scalatest.FunSuite
import java.net.{HttpURLConnection, InetSocketAddress}
import markdown.Markdown
import java.io.{InputStreamReader, OutputStreamWriter, BufferedWriter}
import java.nio.charset.StandardCharsets

class MarkdownConvertServerTest extends FunSuite {
  test ("should return HTML as response") {
    val server = new MarkdownConvertServer( new InetSocketAddress("localhost" ,8080), new Markdown(List(), List()))

    val url = new java.net.URL("http://localhost:8080/post")
    var connection: HttpURLConnection = null
    try {
      server.start()
      connection = url.openConnection() match { case http: HttpURLConnection => http }
      connection.setDoOutput(true)
      connection.setRequestMethod("POST")

      val writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream, StandardCharsets.UTF_8))
      writer.write(
        """# Title1
          |* List
          |* 日本語
        """.stripMargin)
      writer.flush()

      assert(connection.getResponseCode == HttpURLConnection.HTTP_OK)

      val reader = new InputStreamReader(connection.getInputStream, StandardCharsets.UTF_8)
      while(!reader.ready) {}
      var c = reader.read()
      var _html = ""
      while (c != -1) {
        _html = _html + c.toChar
        c = reader.read()
      }
      assert(_html ==
        """<h1>Title1</h1>
          |
          |<ul>
          |<li>List</li>
          |<li>日本語</li>
          |</ul>
          |""".stripMargin)
    } finally {
      if (connection != null) connection.disconnect()
      server.stop()
    }
  }
}
