package com.izmailoff.mm.mqtt

import akka.actor._
import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttMongo
import com.izmailoff.mm.mongo.MongoSerializer
import com.sandinh.paho.akka.MqttPubSub.{Message, Subscribe, SubscribeAck}

class MqttConsumer(pubSubIntermediary: ActorRef) extends Actor with ActorLogging {
  val topicsCollectionsMappings = MqttMongo.topicsToCollectionsMappings

  subscribe()

  def subscribe() =
    topicsCollectionsMappings foreach {
      case (t, c) => pubSubIntermediary ! Subscribe(t, self)
    }

  def receive = {
    case msg: Message =>
      val topic = msg.topic
      val payload = new String(msg.payload)
      val collections = topicsCollectionsMappings(msg.topic)
      log.debug(s"topic: [$topic], payload: [$payload] will be saved to collections: [$collections].")
      MongoSerializer.save(payload, collections)
    case SubscribeAck(Subscribe(topic, `self`, _)) =>
      log.info(s"Subscription to topic [$topic] acknowledged.")
  }

}

object MqttConsumer {

  def start(system: ActorSystem, pubSubIntermediary: ActorRef): ActorRef =
    system.actorOf(props(pubSubIntermediary))

  def props(pubSubIntermediary: ActorRef): Props =
    Props(new MqttConsumer(pubSubIntermediary))
}
