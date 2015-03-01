package com.izmailoff.mm.mqtt

import akka.actor._
import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttMongo
import com.izmailoff.mm.mongo.MongoSerializer
import com.izmailoff.mm.util.JsonDbSerialization
import com.sandinh.paho.akka.MqttPubSub.{Message, Subscribe, SubscribeAck}

abstract class MqttConsumer(pubSubIntermediary: ActorRef)
  extends Actor
  with ActorLogging
  with MongoSerializer {

  val topicsCollectionsMappings = MqttMongo.topicsToCollectionsMappings

  subscribe()

  def subscribe() =
    topicsCollectionsMappings foreach {
      case (t, c) => pubSubIntermediary ! Subscribe(t, self)
    }

  def receive = {
    case msg: Message =>
      saveDb(msg)
    case SubscribeAck(Subscribe(topic, `self`, _)) =>
      log.info(s"Subscription to topic [$topic] acknowledged.")
  }

  def saveDb(msg: Message): Unit = {
    val topic = msg.topic
    val payload = new String(msg.payload)
    val collections = topicsCollectionsMappings(msg.topic)
    val jsonDoc = JsonDbSerialization.parseSafe(payload)
    saveDb(jsonDoc, collections)
    log.debug(s"topic: [$topic], payload: [$payload] is saved to collections: [$collections].")
  }

}
