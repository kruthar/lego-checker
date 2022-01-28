package io.kruth.lego.html

import com.natpryce.Failure
import com.natpryce.Result
import com.natpryce.Success
import com.natpryce.flatMap
import com.natpryce.mapFailure
import org.jsoup.nodes.Document

enum class SetAvailability(val value: String) {
  AVAILABLE("Available now"),
  OUT_OF_STOCK("Temporarily out of stock"),
  BACKORDER("Backorders accepted"),
  UNKNOWN("Availability Unknown");

  companion object {
    private val valueMap = values().associateBy(SetAvailability::value)
    fun of(value: String) = valueMap.getOrDefault(
      value,
      valueMap.filter { entry -> value.contains(entry.key) }.toList().map { it.second }.firstOrNull() ?: UNKNOWN
    )
  }
}

class LegoHtmlPage(val conn: WebConnection, address: String) {
  private val document by lazy {
    conn.getHtml(address)
  }

  fun getState(): Result<SetAvailability, LegoHtmlPageException> = document
    .mapFailure { exception -> LegoHtmlPageException.LegoPageRetrievalException(exception.message ?: "Cause of failure is unknown.") }
    .flatMap { doc ->
      val states = availabilityWrapperSpans(doc)
        .map { span -> span.text() }
        .filter { text ->
          SetAvailability.values().map { it.value }.contains(text.substringBefore(','))
        }

      when {
        states.isEmpty() -> Success(SetAvailability.UNKNOWN)
        states.size == 1 -> Success(SetAvailability.of(states.first()))
        else -> Failure(LegoHtmlPageException.MultipleAvailabilityStateException("Multiple availibility states found: $states"))
      }
    }

  private fun availabilityWrapperSpans(doc: Document) =
    doc.select("div.ProductOverviewstyles__PriceAvailabilityWrapper-sc-1a1az6h-10 span")
}

open class LegoHtmlPageException(message: String) : Exception(message) {
  class MultipleAvailabilityStateException(message: String) : LegoHtmlPageException(message)
  class LegoPageRetrievalException(message: String) : LegoHtmlPageException(message)
}
