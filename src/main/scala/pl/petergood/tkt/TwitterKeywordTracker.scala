package pl.petergood.tkt

import akka.actor.{ActorSystem, Props}
import pl.petergood.tkt.twitter.api.{Tweet, TwitterMessageStream}
import twitter.{TweetCollectorActor, TweetTokenizerActor}

object TwitterKeywordTracker {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem.create("twitter-system")
    val tweetCollectorActor = system.actorOf(Props[TweetCollectorActor], "tweet-collector-actor")
    val tweetTokenizerActor = system.actorOf(Props[TweetTokenizerActor], "tweet-tokenizer-actor")
  }
}
