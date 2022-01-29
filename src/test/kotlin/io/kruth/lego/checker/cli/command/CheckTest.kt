package io.kruth.lego.checker.cli.command

import com.github.ajalt.clikt.core.context
import com.natpryce.Success
import io.kotest.matchers.collections.shouldContain
import io.kruth.lego.checker.cli.notification.AlexaNotifier
import io.kruth.lego.html.WebConnection
import io.kruth.lego.util.EchoCaptureConsole
import io.kruth.lego.util.TestUtil.getDocument
import io.mockk.Called
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CheckTest {
  private val baseUrl = ""
  private val conn = mockk<WebConnection>()
  private val alexa = mockk<AlexaNotifier>()

  @BeforeEach
  fun resetMocks() {
    clearMocks(conn, alexa)
  }

  @Test
  fun `should not notify if notifier is set and set is not available`() {
    val page = "lego-titanic-10294"
    val (cli, console) = getTestConsoleForPage(page)

    cli.parse(listOf("-s", page, "-n", "alexa"))
    console.messages shouldContain "Lego set found at $baseUrl/$page: [Temporarily out of stock]"

    verify { alexa wasNot Called }
  }

  @Test
  fun `should not notify if no notifiers were specified but set is available`() {
    val page = "valentine-lovebirds-40522"
    val (cli, console) = getTestConsoleForPage(page)

    cli.parse(listOf("-s", page))
    console.messages shouldContain "Lego set found at $baseUrl/$page: [Available now]"

    verify { alexa wasNot Called }
  }

  @Test
  fun `should notify with Alexa Notifier`() {
    val page = "valentine-lovebirds-40522"
    val (cli, console) = getTestConsoleForPage(page)

    cli.parse(listOf("-s", page, "-n", "alexa"))
    console.messages shouldContain "Lego set found at $baseUrl/$page: [Available now]"

    verify { alexa.notify("Your Lego set [$page] is Available!") }
  }

  private fun getTestConsoleForPage(page: String): Pair<Check, EchoCaptureConsole> {
    val testConsole = EchoCaptureConsole()

    every {
      conn.getHtml("$baseUrl/$page")
    } returns Success(getDocument("pages/$page.html"))

    every {
      alexa.notify(any())
    } returns Success(true)

    return Check(conn, baseUrl, alexa).context { console = testConsole } to testConsole
  }
}
