package com.izmailoff.mm.mqtt

import akka.actor._
import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttMongo
import com.sandinh.paho.akka.MqttPubSub.{Message, SubscribeAck, Subscribe}

class MqttConsumer(pubSubIntermediary: ActorRef) extends Actor with ActorLogging {
  val topicsCollectionsMappings = MqttMongo.topicsToCollectionsMappings

  subscribe()

  def subscribe() =
    topicsCollectionsMappings foreach {
      case (t, c) => pubSubIntermediary ! Subscribe(t, self)
    }

  /*def receive = {
    case SubscribeAck(Subscribe(topic, `self`, _)) =>
      context become ready
  }*/

  def receive = {
    case msg: Message =>
      log.info(s"topic: ${msg.topic}, payload: ${new String(msg.payload)} " +
        s"will be saved to collections: ${topicsCollectionsMappings(msg.topic)}.")
    case SubscribeAck(Subscribe(topic, `self`, _)) =>
      log.info(s"Subscription to topic $topic acknowledged.")
  }
}

object MqttConsumer {

  def start(system: ActorSystem, pubSubIntermediary: ActorRef): ActorRef =
    system.actorOf(props(pubSubIntermediary))

  def props(pubSubIntermediary: ActorRef): Props =
    Props(new MqttConsumer(pubSubIntermediary))
}


