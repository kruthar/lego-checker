package io.kruth.lego.checker.cli.check

import com.github.ajalt.clikt.core.context
import com.natpryce.Success
import io.kotest.matchers.collections.shouldContain
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
    val page = "lego-titanic-10294"

    every {
      conn.getHtml("$baseUrl/$page")
    } returns Success(getDocument("pages/$page.html"))

    cli.parse(listOf("-s", page))
    console.messages shouldContain "Lego set found at $baseUrl/$page: [Temporarily out of stock]"
  }

  private fun getTestConsole(): Pair<Check, EchoCaptureConsole> {
    val testConsole = EchoCaptureConsole()
    return Check(conn, baseUrl).context { console = testConsole } to testConsole
  }
}
