package io.kruth.lego.checker.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.natpryce.map
import io.kruth.lego.checker.cli.command.NotificationMethod.ALEXA
import io.kruth.lego.checker.cli.command.NotificationMethod.NONE
import io.kruth.lego.checker.cli.notification.AlexaNotifier
import io.kruth.lego.html.LegoHtmlPage
import io.kruth.lego.html.SetAvailability
import io.kruth.lego.html.WebConnection

enum class NotificationMethod(val value: String) {
  ALEXA("alexa"), NONE("none");

  companion object {
    private val valueMap = values().associateBy(NotificationMethod::value)
    fun of(value: String) = valueMap.getOrDefault(value, NONE)
  }
}

class Check(
  val conn: WebConnection,
  private val baseUrl: String,
  private val alexaNotifier: AlexaNotifier
) : CliktCommand() {
  val setName by option("-s", "--set-name", help = "Lego.com set name to check")
  val notify by option("-n", "--notifier", help = "How to notify you of Lego set availability").choice(ALEXA.value).default(NONE.value)

  val legoUrl by lazy {
    "$baseUrl/$setName"
  }

  override fun run() {
    echo("Checking Url: $legoUrl")
    LegoHtmlPage(conn, legoUrl).getState().map { availability ->
      echo("Lego set found at $legoUrl: [${availability.value}]")

      if (availability == SetAvailability.AVAILABLE) {
        when (NotificationMethod.of(notify)) {
          ALEXA -> alexaNotifier.notify("Your Lego set [$setName] is Available!")
          NONE -> print("No alert method specified, skipping alerting.")
        }
      }
    }
  }
}
