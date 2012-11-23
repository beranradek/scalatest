import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class IntegrationSpec extends Specification {

  "Application" should {

    "work from within a browser" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3333/")

        browser.$("h1").first.getText must equalTo("Configure your 'Hello world':")

        browser.$("#name").text("Bob")
        browser.$("#submit").click()
        browser.$("dl.error").size must equalTo(1)
        browser.$("#name").first.getValue must equalTo("Bob")
      }
    }
  }
}