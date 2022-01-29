package io.kruth.lego.html

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ajalt.clikt.output.TermUi.echo
import com.natpryce.Result
import com.natpryce.resultFrom
import org.jsoup.Jsoup
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class WebConnection {
  val objectMapper = ObjectMapper()
  val client: HttpClient = HttpClient.newBuilder().build()

  fun getHtml(address: String) = resultFrom {
    echo("[GET] $address")
    Jsoup.connect(address).get()
  }

  fun post(address: String): Result<String, Exception> = resultFrom {
    echo("[POST] $address")

    val request = HttpRequest.newBuilder()
      .uri(URI.create(address))
      .POST(HttpRequest.BodyPublishers.noBody())
      .build()

    client.send(request, HttpResponse.BodyHandlers.ofString()).body()
  }
}
