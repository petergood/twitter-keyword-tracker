package pl.petergood.tkt.twitter.api

import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.entities.{Tweet => TwitterClientTweet}

trait TweetStreamClient {
  def start()
  def shutdown()
}

object TwitterMessageStream {
  type MessageHandler = PartialFunction[Tweet, Unit]

  def apply(messageHandler: MessageHandler, keywords: List[String]): TweetStreamClient = {
    new TwitterMessageStreamImpl(messageHandler, keywords)
  }

  private class TwitterMessageStreamImpl(messageHandler: MessageHandler, keywords: List[String]) extends TweetStreamClient {
    val client = TwitterStreamingClient()
    val handler: PartialFunction[StreamingMessage, Unit] = {
      case tweet: TwitterClientTweet => messageHandler(new Tweet(text = tweet.text))
    }

    override def start(): Unit = client.filterStatuses(tracks = keywords)(handler)
    override def shutdown(): Unit = client.shutdown()
  }
}
