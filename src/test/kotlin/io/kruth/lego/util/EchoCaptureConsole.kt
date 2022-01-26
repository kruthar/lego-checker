package io.kruth.lego.util

import com.github.ajalt.clikt.output.CliktConsole
import java.lang.UnsupportedOperationException

class EchoCaptureConsole : CliktConsole {
  val messages = mutableListOf<String>()

  override val lineSeparator: String = "\n"
  override fun print(text: String, error: Boolean) {
    print(text)
    messages.add(text.trim())
  }

  override fun promptForLine(prompt: String, hideInput: Boolean): String = throw UnsupportedOperationException()

  fun consoleOutput() = messages.joinToString(separator = lineSeparator)
}
