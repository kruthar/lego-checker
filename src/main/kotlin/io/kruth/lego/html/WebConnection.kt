package io.kruth.lego.html

import com.natpryce.resultFrom
import org.jsoup.Jsoup

class WebConnection {
  fun getHtml(address: String) = resultFrom {
    Jsoup.connect(address).get()
  }
}
