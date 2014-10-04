package server

import java.net.{HttpURLConnection, InetSocketAddress}
import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import java.util.concurrent.{ExecutorService, Executors}
import markdown.Markdown
import java.io.InputStreamReader

/** Requestを受け取り, markdownをHTMLに変換するHTTPサーバ
  *
  * @constructor アドレスと[[markdown.Markdown]]インスタンスから新しいHTTPサーバを生成する.
  * @param address サーバのアドレス
  * @param markdown HTMLへの変換にもちいる[[markdown.Markdown]]インスタンス
  * @todo ポート使われている場合とか例外処理とか何も書いていない.
  * @todo 例くらい載せよう.
  */
class MarkdownConvertServer(address: InetSocketAddress, markdown: Markdown) {
  private var server: HttpServer = null
  private var threadPool: ExecutorService = null

  /** サーバーを起動する. */
  def start() {
    if (server == null && threadPool == null) {
      // HttpServerの作成
      server = HttpServer.create(address, address.getPort)
      threadPool = Executors.newFixedThreadPool(1)
      server.setExecutor(threadPool)
      server.createContext("/", new HttpHandler {
        def handle(exc: HttpExchange) {
          exc.getRequestMethod match {
            case "POST" =>
              val out = new InputStreamReader(exc.getRequestBody, "UTF-8")
              // markdownを取得する.
              var c = out.read()
              var _markdown = ""
              while (c != -1) {
                _markdown = _markdown + c.toChar
                c = out.read()
              }

              // htmlに変換する
              val html = markdown toHTML _markdown

              exc.sendResponseHeaders(HttpURLConnection.HTTP_OK, html.getBytes.length)
              val os = exc.getResponseBody
              os.write(html.getBytes)
              os.close()
            case _ => exc.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0)
          }
        }
      })
      server.start()
    }
  }
  /** サーバーを停止する. */
  def stop() {
    if(server != null && threadPool != null) {
      server.stop(1)
      server = null
      threadPool.shutdownNow()
    }
  }
  override def finalize() {
    stop()
  }
}
