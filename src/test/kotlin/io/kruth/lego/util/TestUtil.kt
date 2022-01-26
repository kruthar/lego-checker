package io.kruth.lego.util

import io.kruth.lego.html.LegoHtmlPageTest
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

object TestUtil {
  fun getDocument(path: String): Document = Jsoup.parse(getFile(path), "utf-8", "")

  private fun getFile(path: String) = File(LegoHtmlPageTest::class.java.classLoader.getResource(path)!!.toURI())
}
