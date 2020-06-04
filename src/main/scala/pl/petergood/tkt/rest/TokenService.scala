package pl.petergood.tkt.rest

import java.text.SimpleDateFormat

import scalikejdbc._

trait TokenService {
  def getPopularTokens(count: Int, timeRange: TimeRange): List[Map[String, Any]]
}

object DefaultTokenService {
  def apply(): TokenService = {
    new PostgresqlTokenService()
  }

  private class PostgresqlTokenService extends TokenService {
    implicit val session: AutoSession.type = AutoSession
    private val dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override def getPopularTokens(count: Int, timeRange: TimeRange): List[Map[String, Any]] = {
      val startTime = dateFormatter.format(timeRange.startTime)
      val endTime = dateFormatter.format(timeRange.endTime)

      sql"""SELECT tokens.token, count(*) FROM tokens
            INNER JOIN (SELECT token, date FROM tokens WHERE type = 0) tw_tokens ON tw_tokens.token = tokens.token
            WHERE tokens.type = 1 AND tokens.date >= to_timestamp(${startTime}, 'YYYY-MM-DD HH24:MI:SS') AND tokens.date <= to_timestamp(${endTime}, 'YYYY-MM-DD HH24:MI:SS')
              AND tw_tokens.date >= to_timestamp(${startTime}, 'YYYY-MM-DD HH24:MI:SS') AND tw_tokens.date <= to_timestamp(${endTime}, 'YYYY-MM-DD HH24:MI:SS')
            GROUP BY tokens.token
            ORDER BY count(*) DESC
            LIMIT ${count}""".map(_.toMap)
        .list
        .apply()
    }
  }
}
