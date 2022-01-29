package io.kruth.lego.checker.cli.notification

import com.natpryce.Result

interface Notifier {
  fun notify(message: String): Result<Boolean, Exception>
}
