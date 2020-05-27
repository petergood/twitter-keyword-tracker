package pl.petergood.tkt
import akka.actor.{Actor, Props}
import pl.petergood.tkt.TwitterKeywordTracker.system
import pl.petergood.tkt.twitter.{TweetCollectorActor, TweetTokenizerActor}

case class Scrap(url: String)

class SupervisorActor extends Actor {
  //The actor supervising all the others
  val websites: List[String] = List("donaldtusk?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor", "londonsbigben")
  //sites to crawl
  val prefix: String = "https://twitter.com/"

  override def receive: Receive = {
    case DefaultStart =>
      for (website <- websites) {
        println(prefix + website)
        val siteScraperActor = system.actorOf(Props(new siteScraperActor(self)));
        siteScraperActor ! Scrap(prefix + website)

      }

  }
}
