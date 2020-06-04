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
    //TODO: move this into config var
    ConnectionPool.singleton("jdbc:postgresql://172.17.0.2:5432/postgres", "postgres", "mysecretpassword")

    val messageStream = TwitterMessageStream({
      case tweet => tokenizerActor ! TokenizeTweetMessage(tweet)
    }, List("#GeorgeFloyd")) //TODO: move this into config var
    messageStream.start()

    val rssFeeds = List("http://rss.cnn.com/rss/edition_us.rss")

    system.scheduler.schedule(0 second, 1 minute) {
      for (feed <- rssFeeds) {
        feedScraperActor ! ScrapeRequest(feed)
      }
    }

    TokenController.setupRoutes
  }
}
