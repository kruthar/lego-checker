package io.kruth.lego.html

import com.natpryce.Failure
import com.natpryce.Success
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kruth.lego.util.TestUtil.getDocument
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.io.IOException

class LegoHtmlPageTest {
  private val conn = mockk<WebConnection>()

  val loader = LegoHtmlPageTest::class.java

  @Test
  fun `should get correct state of page`() {
    every {
      conn.getHtml("available")
    } returns Success(getDocument("pages/valentine-lovebirds-40522.html"))
    LegoHtmlPage(conn, "available").getState() shouldBe Success(SetAvailability.AVAILABLE)

    every {
      conn.getHtml("outofstock")
    } returns Success(getDocument("pages/lego-titanic-10294.html"))
    LegoHtmlPage(conn, "outofstock").getState() shouldBe Success(SetAvailability.OUT_OF_STOCK)

    every {
      conn.getHtml("unknown")
    } returns Success(getDocument("pages/unknown-state-00000.html"))
    LegoHtmlPage(conn, "unknown").getState() shouldBe Success(SetAvailability.UNKNOWN)
  }

  @Test
  fun `should fail when there are multiple states`() {
    every {
      conn.getHtml("multiple")
    } returns Success(getDocument("pages/multiple-states-00000.html"))
    val result = LegoHtmlPage(conn, "multiple").getState()
    result.shouldBeInstanceOf<Failure<LegoHtmlPageException.MultipleAvailabilityStateException>>()
    result.reason.message shouldBe "Multiple availibility states found: ${listOf("Temporarily out of stock", "Available now")}"
  }

  @Test
  fun `should fail when page retrieval fails`() {
    every {
      conn.getHtml("error")
    } returns Failure(IOException("error"))
    val result = LegoHtmlPage(conn, "error").getState()
    result.shouldBeInstanceOf<Failure<LegoHtmlPageException.LegoPageRetrievalException>>()
    result.reason.message shouldBe "error"
  }
}
