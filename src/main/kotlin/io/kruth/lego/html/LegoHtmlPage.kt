package io.kruth.lego.html

import com.natpryce.map
import com.natpryce.Result
import org.jsoup.nodes.Document

class LegoHtmlPage(val conn: WebConnection, address: String) {
  companion object {
    const val AVAILABLE_NOW = "Available now"
    const val TEMPORARILY_OUT_OF_STOCK = "Temporarily out of stock"
  }

  private val document by lazy {
    conn.getHtml(address)
  }

  fun isAvailable(): Result<Boolean, Exception> = document.map { doc ->
    availabilityWrapperSpans(doc).any { span ->
      span.text() == AVAILABLE_NOW
    }
  }

  fun isOutOfStock(): Result<Boolean, Exception> = document.map { doc ->
    availabilityWrapperSpans(doc).any { span ->
      span.text() == TEMPORARILY_OUT_OF_STOCK
    }
  }

  private fun availabilityWrapperSpans(doc: Document) =
    doc.select("div.ProductOverviewstyles__PriceAvailabilityWrapper-sc-1a1az6h-10 span")
}
