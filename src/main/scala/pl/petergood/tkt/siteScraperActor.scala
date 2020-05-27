package pl.petergood.tkt
import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import pl.petergood.tkt.TwitterKeywordTracker.system
import pl.petergood.tkt.twitter.{TweetCollectorActor, TweetTokenizerActor}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import system.dispatcher


case object Process


class siteScraperActor (supervisor: ActorRef) extends Actor {
  //handles scheduling

  var toProcess = List.empty[String] //List of url-s to process
  implicit val timeout = Timeout(8 seconds)

  val tick =
    context.system.scheduler.schedule(Duration.Zero, 1000.milliseconds, self, Process)

  override def receive: Receive = {
    case Scrap(url:String) =>
      println(s"waiting -> $url")
      toProcess = url :: toProcess
    case Process =>
      toProcess match {
        case Nil =>
        case url :: list =>
          println(s"site scraping -> $url")
          toProcess = list
      }
  }
  tick.cancel()
}

