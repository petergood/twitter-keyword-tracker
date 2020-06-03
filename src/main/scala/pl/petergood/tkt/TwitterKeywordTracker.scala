package pl.petergood.tkt

import akka.actor.{ActorSystem, Props}
import pl.petergood.tkt.twitter.api.{Tweet, TwitterMessageStream}
import scalikejdbc.{AutoSession, ConnectionPool}
import twitter.{TokenizeTweetMessage, TokenizerActor}

object TwitterKeywordTracker {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem.create("twitter-system")
    val tokenizerActor = system.actorOf(Props[TokenizerActor], "tokenizer-actor")

    Class.forName("org.postgresql.Driver")
    //TODO: move this into config var
    ConnectionPool.singleton("jdbc:postgresql://172.17.0.2:5432/postgres", "postgres", "mysecretpassword")

    val messageStream = TwitterMessageStream({
      case tweet => tokenizerActor ! TokenizeTweetMessage(tweet)
    }, List("#GeorgeFloyd")) //TODO: move this into config var
    messageStream.start()
  }
}
