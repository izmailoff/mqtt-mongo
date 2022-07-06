package com.izmailoff.mm.service

import akka.actor.{ActorRef, ActorSystem, Props}

trait TestMqttMongoServiceImpl
  extends MqttMongoService
  with TestMqttIntermediary
  with TestMongoDbProviderImpl {}

trait TestMqttIntermediary {
  def system: ActorSystem

  def startMqttIntermediary(): ActorRef =
    system.actorOf(Props[TestMqttIntermediaryActor])
}