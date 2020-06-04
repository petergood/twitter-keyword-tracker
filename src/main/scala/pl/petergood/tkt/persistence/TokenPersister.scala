package pl.petergood.tkt.persistence

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

import scalikejdbc._

import scala.collection.mutable.ListBuffer
import scala.io.Source

trait DatabaseTokenPersister {
  def persist(token: String, timestamp: Instant, tokenType: TokenType.Value)
}

object TokenType extends Enumeration {
  val tweet, rss = Value
}

object TokenPersister {
  implicit val session: AutoSession.type = AutoSession
  private val dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def apply(): DatabaseTokenPersister = {
    val ignoredTokens = new ListBuffer[String]()
    for (line <- Source.fromFile("ignored_tokens.txt").getLines) {
      ignoredTokens += line
    }

    ignoredTokens += ""
    new PostgresqlTokenPersister(ignoredTokens.toList)
  }

  private class PostgresqlTokenPersister(val ignoredTokens: List[String]) extends DatabaseTokenPersister {
    override def persist(token: String, timestamp: Instant, tokenType: TokenType.Value): Unit = {
      if (!ignoredTokens.contains(token)) {
        val dateString = dateFormatter.format(Date.from(timestamp))
        sql"insert into tokens(token, date, type) values (${token}, to_timestamp(${dateString}, 'YYYY-MM-DD HH24:MI:SS'), ${tokenType.id})".update.apply()
      }
    }
  }
}
