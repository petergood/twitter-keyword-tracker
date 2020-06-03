package pl.petergood.tkt
import akka.actor.{Actor, Props}
import pl.petergood.tkt.TwitterKeywordTracker.system

case class Scrap(url: String)

class SupervisorActor extends Actor {
  //The actor supervising all the others
  val websites: List[String] = List("Panguraptor", "Londyn")
  //sites to crawl
  val prefix: String = "https://pl.wikipedia.org/wiki/"

  override def receive: Receive = {
    case DefaultStart =>
      for (website <- websites) {
        val siteScraperActor = system.actorOf(Props(new siteScraperActor(self)));
        siteScraperActor ! Scrap(prefix + website)

      }

  }
}
