package pl.petergood.tkt.twitter

import akka.actor.Actor
import pl.petergood.tkt.tokenizer.Tokenizer
import pl.petergood.tkt.twitter.api.Tweet

case class TokenizeTweetMessage(tweet: Tweet)

class TweetTokenizerActor extends Actor {
  private val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".toSet

  override def receive = {
    case TokenizeTweetMessage(tweet) => {
      val tokens = Tokenizer().tokenize(tweet.text)
    }
  }
}
