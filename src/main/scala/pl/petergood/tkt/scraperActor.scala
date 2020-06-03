package pl.petergood.tkt


import akka.actor.{Actor, ActorRef}
import net.ruippeixotog.scalascraper.browser.HtmlUnitBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._


class scraperActor (supervisor: ActorRef) extends Actor{
  val browser = HtmlUnitBrowser.typed()
  override def receive: Receive = {
    case Scrap(url:String) =>
      val doc = browser.get(url)
      val title = doc >> text("#firstHeading")
      println(title)

      val  link = "https://pl.wikipedia.org/" + (doc >> element("p a") >> attr("href"))
      supervisor ! Scrap(link)

  }

}

//class="css-901oao css-16my406 r-1qd0xha r-ad9z0x r-bcqeeo r-qvutc0"
//pl.wikipedia.org/wiki/Panguraptor*/