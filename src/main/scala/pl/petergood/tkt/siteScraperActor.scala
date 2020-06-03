package pl.petergood.tkt
import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import pl.petergood.tkt.TwitterKeywordTracker.system
import pl.petergood.tkt.TwitterKeywordTracker.system.dispatcher

import scala.concurrent.duration._
import scala.language.postfixOps


case object Process


class siteScraperActor (supervisor: ActorRef) extends Actor {
  //handles scheduling

  val scraperActor = system.actorOf(Props(new scraperActor(self)));

  var toProcess = List.empty[String] //List of url-s to process
  implicit val timeout = Timeout(8 seconds)

  val tick =
    context.system.scheduler.scheduleWithFixedDelay(Duration.Zero, 1000.milliseconds, self, Process)

  override def receive: Receive = {
    case Scrap(url:String) =>
     // println(s"waiting -> $url")
      toProcess = url :: toProcess
    case Process =>
      toProcess match {
        case Nil =>
        case url :: list =>
        //  println(s"site scraping -> $url")
          scraperActor ! Scrap(url)
          toProcess = list
      }
  }
  //tick.cancel()
}

