package io.kruth.lego.checker.cli.notification

import com.github.ajalt.clikt.output.TermUi.echo
import com.natpryce.Result
import com.natpryce.map
import com.natpryce.peek
import io.kruth.lego.html.WebConnection
import java.net.URLEncoder
import java.util.Properties

class AlexaNotifier(val conn: WebConnection, val props: Properties) : Notifier {
  private val baseUrl = "https://api.notifymyecho.com/v1/NotifyMe"
  private val propertyKey = "notifiers.alexa.accessCode"

  override fun notify(message: String): Result<Boolean, Exception> {
    val address = "$baseUrl?notification=${URLEncoder.encode(message, "utf-8")}&accessCode=${props.getProperty(propertyKey)}"
    echo("Notifying with Alexa: $address")

    return conn.post(address)
      .peek { echo("Response: $it") }
      .map { true }
  }
}
