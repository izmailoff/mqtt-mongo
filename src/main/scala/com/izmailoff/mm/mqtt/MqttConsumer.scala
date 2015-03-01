package com.izmailoff.mm.mqtt

import akka.actor._
import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttMongo
import com.izmailoff.mm.mongo.MongoDbProvider
import com.izmailoff.mm.util.JsonDbSerialization
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
      persist(msg)
    case SubscribeAck(Subscribe(topic, `self`, _)) =>
      log.info(s"Subscription to topic [$topic] acknowledged.")
  }

  def persist(msg: Message): Unit = {
    val topic = msg.topic
    val payload = new String(msg.payload)
    val collections = topicsCollectionsMappings(msg.topic)
    val jsonDoc = JsonDbSerialization.parseSafe(payload)
    saveDb(jsonDoc, collections)
    log.debug(s"topic: [$topic], payload: [$payload] is saved to collections: [$collections].")
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