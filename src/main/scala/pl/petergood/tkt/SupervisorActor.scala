package pl.petergood.tkt
import akka.actor.{Actor, Props}
import pl.petergood.tkt.TwitterKeywordTracker.system

case class Scrap(url: String)
case class PrintName(url: String)
//case object Start
case object Process
case object Over


class SupervisorActor extends Actor {
  //The actor supervising all the others
  val websites: List[String] = List("Panguraptor", "Londyn")
  //sites to crawl
  val prefix: String = "https://pl.wikipedia.org/wiki/"

  override def receive: Receive = {
    case DefaultStart =>
      val scheduleActor = system.actorOf(Props(new scheduleActor(self)));
      for (website <- websites) {
        scheduleActor ! Scrap(prefix + website)
      }
      //scheduleActor ! Start

  }
}
