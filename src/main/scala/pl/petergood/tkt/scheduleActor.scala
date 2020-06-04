package pl.petergood.tkt
import akka.actor.{Actor, ActorRef, Props}
import pl.petergood.tkt.TwitterKeywordTracker.system
import pl.petergood.tkt.TwitterKeywordTracker.system.dispatcher

import scala.concurrent.duration._
import scala.language.postfixOps


case class WikiEntry(url: String, father: String)



class scheduleActor(supervisor: ActorRef) extends Actor {
  //handles scheduling

  val scraperActor = system.actorOf(Props(new scraperActor(self)));

  var toProcess = List.empty[WikiEntry] //List of url-s to process
  var history = List.empty[WikiEntry]

  var forbidden = List.empty[String] //Newer visit the same page twice
  var winner = WikiEntry("", "")

  val tick =
    context.system.scheduler.scheduleWithFixedDelay(Duration.Zero, 1000.milliseconds, self, Process)

  override def receive: Receive = {
    case Scrap(url:String) =>
     // println(s"waiting -> $url")
      if (!forbidden.contains(url)) {
        toProcess = WikiEntry(url, winner.url) :: toProcess
        forbidden = url :: forbidden
      }
    case Process =>
      toProcess match {
        case Nil =>{
          println("OVER")
          println(winner.url)
          tick.cancel()
          printTrace()}
        case url :: list =>
        //  println(s"site scraping -> $url")
          winner = url
          scraperActor ! Scrap(url.url)
          history = url :: history
          toProcess = list
      }
  }

  def printTrace() = {
    scraperActor ! PrintName(winner.url)

    while ( { winner.father != "" }) {
      for (entry:WikiEntry <- history if winner.father == entry.url) {
        scraperActor ! PrintName(entry.url)
        winner = entry
      }
    }

  }
  //tick.cancel()
}

