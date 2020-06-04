package pl.petergood.tkt.tokenizer

import java.time.Instant

import akka.actor.{Actor, Props}
import pl.petergood.tkt.persistence.{RSSToken, TokenPersistenceActor, TwitterTokens}
import pl.petergood.tkt.rss.Article
import pl.petergood.tkt.twitter.api.Tweet

case class TokenizeTweetMessage(tweet: Tweet)
case class TokenizeArticle(article: Article)

class TokenizerActor extends Actor {
  private val tokenPersistenceActor = context.actorOf(Props[TokenPersistenceActor], "token-persistence-actor")
  val tokenizer = Tokenizer()

  override def receive = {
    case TokenizeTweetMessage(tweet) => {
      val tokens = tokenizer.tokenize(tweet.text)
      tokenPersistenceActor ! TwitterTokens(tokens, Instant.now())
    }
    case TokenizeArticle(article) => {
      val tokens = tokenizer.tokenize(article.body)
      if (article.postedAt != null) {
        tokenPersistenceActor ! RSSToken(tokens, article.postedAt.toInstant)
      }
    }
  }
}
