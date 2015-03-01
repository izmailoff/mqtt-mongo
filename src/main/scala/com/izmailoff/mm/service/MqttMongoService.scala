package com.izmailoff.mm.service

import akka.actor.{ActorRef, ActorSystem, Props}
import com.izmailoff.mm.mongo.MongoDbProviderImpl
import com.izmailoff.mm.mqtt.{MqttConsumer, MqttIntermediary}

trait MqttMongoService {

  def system: ActorSystem

  def startMqttIntermediary(system: ActorSystem): ActorRef

  def startMqttConsumer(system: ActorSystem, pubSubIntermediary: ActorRef): ActorRef
}

trait MqttMongoServiceImpl
extends MqttMongoService
  with MqttIntermediary {

  def system: ActorSystem

  def startMqttConsumer(system: ActorSystem, pubSubIntermediary: ActorRef): ActorRef =
    system.actorOf(Props(new MqttConsumer(pubSubIntermediary) with MongoDbProviderImpl {}))
}
