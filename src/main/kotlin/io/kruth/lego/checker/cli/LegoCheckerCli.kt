package io.kruth.lego.checker.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.kruth.lego.checker.cli.check.Check

class LegoCheckerCli : CliktCommand() {
  override fun run() = Unit
}

fun main(args: Array<String>) = LegoCheckerCli().subcommands(Check()).main(args)
