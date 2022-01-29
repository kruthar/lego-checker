package io.kruth.lego.checker.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.kruth.lego.checker.cli.command.Check
import io.kruth.lego.checker.cli.notification.AlexaNotifier
import io.kruth.lego.html.WebConnection
import java.io.File
import java.util.Properties

class LegoCheckerCli : CliktCommand() {
  override fun run() = Unit
}

fun main(args: Array<String>) {
  val conn = WebConnection()
  val props = Properties()
  props.load(File("lego-cli.properties").inputStream())

  LegoCheckerCli().subcommands(
    Check(conn, "https://www.lego.com/en-us/product", AlexaNotifier(conn, props))
  ).main(args)
}
