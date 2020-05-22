package pl.petergood.tkt.twitter

import akka.actor.Actor

case class CollectTweetsMessage(hashTag: String)

class TweetCollectorActor extends Actor {
  override def receive = {
    case CollectTweetsMessage(hashTag) => ???
  }
}
