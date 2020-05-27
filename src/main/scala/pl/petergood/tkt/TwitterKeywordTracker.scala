package pl.petergood.tkt

import akka.actor.{ActorSystem, PoisonPill, Props}
import twitter.{TweetCollectorActor, TweetTokenizerActor}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

case class Start(query: String)
case object DefaultStart


object TwitterKeywordTracker extends App{
    val system = ActorSystem.create("twitter-system")
    val supervisorActor = system.actorOf(Props[SupervisorActor]);

    supervisorActor ! DefaultStart

    Await.result(system.whenTerminated, 5 minutes)

    supervisorActor ! PoisonPill

    system.terminate()

}
