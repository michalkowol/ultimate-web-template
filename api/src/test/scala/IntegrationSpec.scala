import org.scalatest.tags._
import org.scalatestplus.play._
import play.api.test.TestServer
import scaldi.play.ScaldiApplicationBuilder
import play.api.test._
import play.api.test.Helpers._

class ApplicationIntegrationSpec extends Spec {
  "Application" should "work from within a browser" in {
    running(TestServer(3333, new ScaldiApplicationBuilder().build()), FIREFOX) { browser =>
      browser.goTo("http://localhost:3333")
      browser.pageSource should include ("Your new application is ready.")
    }
  }

  it should "click button" in {
    running(TestServer(3333, new ScaldiApplicationBuilder().build()), FIREFOX) { browser =>
      browser.goTo("http://localhost:3333")
      browser.title shouldBe "Welcome to Play"
      browser.click("#change-title-btn")
      browser.waitUntil { browser.title() == "Changed: Welcome to Play" }
      //      browser.waitUntil { browser.title() == "Welcome to Play" }
    }
  }
}
//
//trait WeatherIntegrationSpec extends IntegrationSpec {
//  "Weather" should "get weather in Gliwice" in {
//    go to s"http://localhost:$port/weather/pl/Gliwice"
//    pageSource should include ("Gliwice")
//    pageSource should include (""""lon":""")
//  }
//}
//
//trait PeopleIntegrationSpec extends IntegrationSpec {
//  "People" should "get list of people" in {
//    go to s"http://localhost:$port/people"
//    pageSource should include ("Katarzyna")
//  }
//}

//trait IntegrationSpecs extends ApplicationIntegrationSpec with WeatherIntegrationSpec with PeopleIntegrationSpec
//
//@FirefoxBrowser class FirefoxIntegrationSpec extends IntegrationSpecs with FirefoxFactory
//@SafariBrowser class SafariIntegrationSpec extends IntegrationSpecs with SafariFactory
//@InternetExplorerBrowser class InternetExplorerIntegrationSpec extends IntegrationSpecs with InternetExplorerFactory
//@ChromeBrowser class ChromeIntegrationSpec extends IntegrationSpecs with ChromeFactory
//@HtmlUnitBrowser class HtmlUnitIntegrationSpec extends IntegrationSpecs with HtmlUnitFactory
