package com.izmailoff.mm.mqtt

import akka.actor._
import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttMongo
import com.izmailoff.mm.mongo.MongoDbProvider
import com.izmailoff.mm.util.DbSerialization._
import com.mongodb.DBObject
import com.mongodb.casbah.MongoDB
import com.sandinh.paho.akka.MqttPubSub.{Message, Subscribe, SubscribeAck}

class MqttConsumer(pubSubIntermediary: ActorRef, db: MongoDB)
  extends Actor
  with ActorLogging {

  val topicsCollectionsMappings = MqttMongo.topicsToCollectionsMappings

  subscribe()

  def subscribe() =
    topicsCollectionsMappings foreach {
      case (t, c) => pubSubIntermediary ! Subscribe(t, self)
    }

  def receive = {
    case msg: Message =>
      val (doc, collections) = getPersistenceRecipe(msg)
      saveDb(doc, collections)
      log.debug(s"topic: [${msg.topic}], payload: [$doc] is saved to collections: [$collections].")
    case SubscribeAck(Subscribe(topic, `self`, _)) =>
      log.info(s"Subscription to topic [$topic] acknowledged.")
  }

  def getPersistenceRecipe(msg: Message) = {
    import msg._
    val collections = topicsCollectionsMappings(topic)
    val doc = serialize(payload)
    (doc, collections)
  }

  def saveDb(doc: DBObject, collections: Set[String]): Unit =
    collections foreach { collName =>
      db(collName).insert(doc)
    }
}

trait MqttConsumerComponent
  extends MongoDbProvider {
  def system: ActorSystem

  def startMqttConsumer(pubSubIntermediary: ActorRef): ActorRef =
    system.actorOf(Props(new MqttConsumer(pubSubIntermediary, db)))
}
