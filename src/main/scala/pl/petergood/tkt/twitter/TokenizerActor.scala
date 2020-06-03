package pl.petergood.tkt.twitter

import java.time.Instant

import akka.actor.{Actor, Props}
import pl.petergood.tkt.persistence.{TokenPersistenceActor, TokenPersister, TwitterTokens}
import pl.petergood.tkt.tokenizer.Tokenizer
import pl.petergood.tkt.twitter.api.Tweet

case class TokenizeTweetMessage(tweet: Tweet)

class TokenizerActor extends Actor {
  private val tokenPersistenceActor = context.actorOf(Props[TokenPersistenceActor], "token-persistence-actor")

  override def receive = {
    case TokenizeTweetMessage(tweet) => {
      val tokens = Tokenizer().tokenize(tweet.text)
      tokenPersistenceActor ! TwitterTokens(tokens, Instant.now())
    }
  }
}
