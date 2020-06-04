package pl.petergood.tkt.rss

import akka.actor.{Actor, Props}
import pl.petergood.tkt.tokenizer.{TokenizeArticle, TokenizerActor}

case class ScrapeRequest(url: String)

class FeedScraperActor extends Actor {
  val feedScraper = RssFeedScraper()
  val tokenizerActor = context.actorOf(Props[TokenizerActor], "rss-tokenizer-actor")
  val articleService = DefaultArticleService()

  override def receive: Receive = {
    case ScrapeRequest(url) => {
      val articles = feedScraper.scrape(url)

      for (article <- articles) {
        if (!articleService.hasArticleBeenProcessed(article.url)) {
          articleService.recordArticle(article.url)
          tokenizerActor ! TokenizeArticle(article)
        }
      }
    }
  }
}
