package io.kruth.lego.html

import com.natpryce.Success
import io.mockk.every
import io.mockk.mockk
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class LegoHtmlPageTest {
  private val conn = mockk<WebConnection>()

  val loader = LegoHtmlPageTest::class.java

  @Test
  fun `should identify an Available page`() {
    every {
      conn.getHtml("test")
    } returns Success(getDocument("pages/valentine-lovebirds-40522.html"))
    assertEquals(LegoHtmlPage(conn, "test").isAvailable(), Success(true))
    assertEquals(LegoHtmlPage(conn, "test").isOutOfStock(), Success(false))
  }

  @Test
  fun `should identify an Out of stock page`() {
    every {
      conn.getHtml("test")
    } returns Success(getDocument("pages/lego-titanic-10294.html"))
    assertEquals(LegoHtmlPage(conn, "test").isAvailable(), Success(false))
    assertEquals(LegoHtmlPage(conn, "test").isOutOfStock(), Success(true))
  }

  private fun getDocument(path: String) = Jsoup.parse(getFile(path), "utf-8", "")

  private fun getFile(path: String) = File(LegoHtmlPageTest::class.java.classLoader.getResource(path).toURI())
}
