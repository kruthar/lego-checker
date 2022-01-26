package io.kruth.lego.checker.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.kruth.lego.checker.cli.check.Check
import io.kruth.lego.html.WebConnection

class LegoCheckerCli : CliktCommand() {
  override fun run() = Unit
}

fun main(args: Array<String>) = LegoCheckerCli().subcommands(
  Check(WebConnection(), "https://www.lego.com/en-us/product")
).main(args)
