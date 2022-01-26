package io.kruth.lego.checker.cli.check

import com.github.ajalt.clikt.core.context
import com.natpryce.Success
import io.kotest.matchers.shouldBe
import io.kruth.lego.html.WebConnection
import io.kruth.lego.util.EchoCaptureConsole
import io.kruth.lego.util.TestUtil.getDocument
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class CheckTest {
  private val baseUrl = ""
  private val conn = mockk<WebConnection>()

  @Test
  fun `should check the correct address with -s flag`() {
    val (cli, console) = getTestConsole()
    val page = "valentine-lovebirds-40522"

    every {
      conn.getHtml("$baseUrl/$page.html")
    } returns Success(getDocument("pages/$page.html"))

    cli.parse(listOf("-s", page))
    console.messages[0] shouldBe "Lego set found at $baseUrl/$page: [Available]"
  }

  private fun getTestConsole(): Pair<Check, EchoCaptureConsole> {
    val testConsole = EchoCaptureConsole()
    return Check(conn, baseUrl).context { console = testConsole } to testConsole
  }
}
