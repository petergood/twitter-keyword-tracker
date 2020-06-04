package pl.petergood.tkt

import akka.actor.{ActorSystem, Props}
import pl.petergood.tkt.rest.{DefaultTokenService, TokenController}
import pl.petergood.tkt.rss.{FeedScraperActor, RssFeedScraper, ScrapeRequest}
import pl.petergood.tkt.tokenizer.{TokenizeTweetMessage, TokenizerActor}
import pl.petergood.tkt.twitter.api.{Tweet, TwitterMessageStream}
import scalikejdbc.{AutoSession, ConnectionPool}
import scala.language.postfixOps

import scala.concurrent.duration._

object TwitterKeywordTracker {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem.create("twitter-system")
    import system.dispatcher

    val tokenizerActor = system.actorOf(Props[TokenizerActor], "tokenizer-actor")
    val feedScraperActor = system.actorOf(Props[FeedScraperActor], "feed-scraper-actor")

    Class.forName("org.postgresql.Driver")
    ConnectionPool.singleton(sys.env("DB_URL"), sys.env("DB_USER"), sys.env("DB_PASSWORD"))

    val messageStream = TwitterMessageStream({
      case tweet => tokenizerActor ! TokenizeTweetMessage(tweet)
    }, sys.env("HASHTAGS").split(",").toList)
    messageStream.start()

    val rssFeeds = sys.env("RSS_FEEDS").split(",").toList

    system.scheduler.schedule(0 second, 1 minute) {
      for (feed <- rssFeeds) {
        feedScraperActor ! ScrapeRequest(feed)
      }
    }

    TokenController.setupRoutes
  }
}
