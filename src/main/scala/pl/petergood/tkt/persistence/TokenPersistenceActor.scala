package pl.petergood.tkt.persistence

import java.time.Instant

import akka.actor.Actor

case class TwitterTokens(tokens: Array[String], timestamp: Instant)
case class RSSToken(tokens: Array[String], timestamp: Instant)

class TokenPersistenceActor extends Actor {
  private val tokenPersister = TokenPersister()

  override def receive: Receive = {
    case TwitterTokens(tokens, timestamp) => tokens.foreach(token => tokenPersister.persist(token, timestamp, TokenType.tweet))
    case RSSToken(tokens, timestamp) => tokens.foreach(token => tokenPersister.persist(token, timestamp, TokenType.rss))
  }
}
