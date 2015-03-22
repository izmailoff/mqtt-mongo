package com.izmailoff.mm.service

import akka.actor.{ActorLogging, Actor, ActorRef}
import com.sandinh.paho.akka.MqttPubSub.{Message, Subscribe, SubscribeAck}

class TestMqttIntermediaryActor extends Actor with ActorLogging {
  var topicSubscribers: Map[String, Set[ActorRef]] = Map()

  def receive = {
    case subRequest@Subscribe(topic, subscriber, _) =>
      updateSubscribers(topic, subscriber)
      log.debug(s"Current subscribers: $topicSubscribers")
      subscriber ! SubscribeAck(subRequest)
    case msg: Message =>
      log.debug(s"Forwarding message: [$msg] to subscribers.")
      forwardToSubscribers(msg)
  }

  def forwardToSubscribers(msg: Message): Unit =
    for {
      subscribers <- topicSubscribers.get(msg.topic)
      subscriber <- subscribers
    } {
      subscriber ! msg
    }

  def updateSubscribers(topic: String, subscriber: ActorRef): Unit = {
    val existingSubscribers = topicSubscribers.getOrElse(topic, Set())
    val updatedSubscribers = existingSubscribers + subscriber
    topicSubscribers = topicSubscribers + (topic -> updatedSubscribers)
  }
}
