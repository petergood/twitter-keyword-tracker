package pl.petergood.tkt

import akka.actor.{ActorSystem, Props}
import pl.petergood.tkt.twitter.api.{Tweet, TwitterMessageStream}
import twitter.{TokenizeTweetMessage, TweetTokenizerActor}

object TwitterKeywordTracker {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem.create("twitter-system")
    val tweetTokenizerActor = system.actorOf(Props[TweetTokenizerActor], "tweet-tokenizer-actor")

    val messageStream = TwitterMessageStream({
      case tweet => tweetTokenizerActor ! TokenizeTweetMessage(tweet)
    }, List("#GeorgeFloyd")) //TODO: move this into config var
    messageStream.start()
  }
}
