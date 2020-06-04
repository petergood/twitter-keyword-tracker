package pl.petergood.tkt


import akka.actor.{Actor, ActorRef}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._


class scraperActor (supervisor: ActorRef) extends Actor{

  val browser = JsoupBrowser.typed()

  override def receive: Receive = {
    case Scrap(url:String) => {
      val doc = browser.get(url)
      val title = doc >> text("#firstHeading")
      println(title)
      val link = "https://pl.wikipedia.org/" + (doc >> element("p a") >> attr("href"))
      supervisor ! Scrap(link)
    }
    case PrintName(url:String) => {
      print(browser.get(url) >> text("#firstHeading"))
      print(" -> ")
    }

  }

}
