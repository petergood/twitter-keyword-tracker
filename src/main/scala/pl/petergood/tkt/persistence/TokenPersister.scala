package pl.petergood.tkt.persistence

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

import scalikejdbc._

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
    new PostgresqlTokenPersister()
  }

  private class PostgresqlTokenPersister extends DatabaseTokenPersister {
    override def persist(token: String, timestamp: Instant, tokenType: TokenType.Value): Unit = {
      val dateString = dateFormatter.format(Date.from(timestamp))
      sql"insert into tokens(token, date, type) values (${token}, to_timestamp(${dateString}, 'YYYY-MM-DD HH24:MI:SS'), ${tokenType.id})".update.apply()
    }
  }
}
