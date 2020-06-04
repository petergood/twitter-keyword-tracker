package pl.petergood.tkt.rest

import java.text.SimpleDateFormat

import spark.{Request, Response, Route}
import spark.Spark._
import org.json4s.native.Json
import org.json4s.DefaultFormats

object TokenController {
  private val tokenService = DefaultTokenService()
  private val dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


  def setupRoutes = {
    get("/tokens", getTokensHandler)
    get("/popular", getPopularHandler)
  }

  private def getPopularHandler: Route = (request: Request, response: Response) => {
    val startTime = dateFormatter.parse(request.queryParamOrDefault("startTime", "1970-01-01 00:00:00"))
    val endTime = dateFormatter.parse(request.queryParamOrDefault("endTime", "2100-01-01 00:00:00"))
    val limit = request.queryParamOrDefault("limit", "10").toInt

    val tokens = tokenService.getPopularTokens(limit, new TimeRange(startTime, endTime))
    response.`type`("application/json")
    Json(DefaultFormats).write(tokens)
  }

  private def getTokensHandler: Route = (request: Request, response: Response) => {
    val startTime = dateFormatter.parse(request.queryParamOrDefault("startTime", "1970-01-01 00:00:00"))
    val endTime = dateFormatter.parse(request.queryParamOrDefault("endTime", "2100-01-01 00:00:00"))
    val filterTokens = request.queryParamOrDefault("tokens", "").split(",")

    val tokens = tokenService.getTokens(new TimeRange(startTime, endTime), filterTokens)
    response.`type`("application/json")
    Json(DefaultFormats).write(tokens)
  }
}
