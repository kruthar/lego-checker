package io.kruth.lego.checker.cli.check

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.natpryce.map
import io.kruth.lego.html.LegoHtmlPage
import io.kruth.lego.html.WebConnection

class Check(val conn: WebConnection, val baseUrl: String) : CliktCommand() {
  val setName by option("-s", "--set-name", help = "Lego.com set name to check")

  val legoUrl by lazy {
    "$baseUrl/$setName"
  }

  override fun run() {
    LegoHtmlPage(conn, legoUrl).getState().map {
      echo("Lego set found at $legoUrl: [Available]")
    }
  }
}
