package pl.petergood.tkt.twitter

import akka.actor.Actor

case class TokenizeTweetMessage(tweetBody: String)

class TweetTokenizerActor extends Actor {
  override def receive = {
    case TokenizeTweetMessage(tweetBody) => ???
  }
}
