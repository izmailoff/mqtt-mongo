package com.izmailoff.mm.mqtt

import akka.actor.{ActorSystem, Props, ActorRef, Actor}
import com.sandinh.paho.akka.MqttPubSub.{Message, SubscribeAck, Subscribe}

class MqttConsumer(pubSubIntermediary: ActorRef) extends Actor {
  val topic = "test"

  pubSubIntermediary ! Subscribe(topic, self) // foreach ...

  def receive = {
    case SubscribeAck(Subscribe(`topic`, `self`, _)) => //??? handle each topic??
      context become ready
  }

  def ready: Receive = {
    case msg: Message =>
      println(s"topic: ${msg.topic}, payload: ${new String(msg.payload)}")
  }
}

object MqttConsumer {

  def start(system: ActorSystem, pubSubIntermediary: ActorRef): ActorRef =
    system.actorOf(props(pubSubIntermediary))

  def props(pubSubIntermediary: ActorRef): Props =
    Props(new MqttConsumer(pubSubIntermediary))
}


