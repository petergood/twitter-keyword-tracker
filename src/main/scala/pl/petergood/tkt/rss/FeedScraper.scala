package pl.petergood.tkt.rss

import java.net.URL
import java.util.Date

import com.rometools.rome.io.{SyndFeedInput, XmlReader}

import scala.collection.mutable.ListBuffer

trait FeedScraper {
  def scrape(url: String): List[Article]
}

class Article(val url: String, val body: String, val postedAt: Date)

object RssFeedScraper {
  def apply(): FeedScraper = {
    new RomeRssFeedScraper()
  }

  private class RomeRssFeedScraper extends FeedScraper {
    override def scrape(url: String): List[Article] = {
      val feed = new SyndFeedInput().build(new XmlReader(new URL(url)))
      val articles = new ListBuffer[Article]()

      feed.getEntries.forEach(entry => {
        val contents = s"${entry.getTitle} ${entry.getDescription.getValue}"
        articles += new Article(entry.getLink, contents, entry.getPublishedDate)
      })

      articles.toList
    }
  }
}
