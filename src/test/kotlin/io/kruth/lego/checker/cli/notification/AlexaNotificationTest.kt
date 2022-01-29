package io.kruth.lego.checker.cli.notification

import com.natpryce.Failure
import com.natpryce.Success
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kruth.lego.html.WebConnection
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.Properties

class AlexaNotificationTest {
  val conn = mockk<WebConnection>()
  val props = Properties().apply {
    setProperty("notifiers.alexa.accessCode", "accessCode")
  }

  @Test
  fun `notify builds url correctly`() {
    every {
      conn.post(any())
    } returns Success("{\"sent\":{\"notification\":\"hello\"}}")

    AlexaNotifier(conn, props).notify("hello") shouldBe Success(true)
    verify { conn.post("https://api.notifymyecho.com/v1/NotifyMe?notification=hello&accessCode=accessCode") }
  }

  @Test
  fun `notify should pass through exceptions`() {
    every {
      conn.post(any())
    } returns Failure(Exception())

    AlexaNotifier(conn, props).notify("hello").shouldBeInstanceOf<Failure<Exception>>()
  }
}
