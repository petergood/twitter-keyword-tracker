package pl.petergood.tkt.rss

import scalikejdbc._

trait ArticleService {
  def recordArticle(url: String)
  def hasArticleBeenProcessed(url: String): Boolean
}

object DefaultArticleService {
  def apply(): ArticleService = {
    new PostgresqlArticleService()
  }

  private class PostgresqlArticleService extends ArticleService {
    implicit val session: AutoSession.type = AutoSession

    override def recordArticle(url: String): Unit = {
      sql"insert into scraped_articles (url) values (${url})".update.apply()
    }

    override def hasArticleBeenProcessed(url: String): Boolean = {
      sql"select * from scraped_articles where url = ${url}".map(_.toMap)
        .list
        .apply().nonEmpty
    }
  }
}
