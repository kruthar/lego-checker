package io.kruth.lego.checker.cli.check

import com.github.ajalt.clikt.core.CliktCommand

class Check : CliktCommand() {
  override fun run() {
    echo("Check command run")
  }
}
