package com.izmailoff.mm.mqtt

import akka.actor.{ActorRef, ActorSystem, Props}
import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttBroker
import com.sandinh.paho.akka.MqttPubSub
import com.sandinh.paho.akka.MqttPubSub.PSConfig

trait MqttIntermediary {

  def startMqttIntermediary(system: ActorSystem): ActorRef =
    system.actorOf(Props(classOf[MqttPubSub], PSConfig(
      brokerUrl = MqttBroker.url,
      userName = emptyToNull(MqttBroker.userName),
      password = emptyToNull(MqttBroker.password),
      stashTimeToLive = MqttBroker.stashTimeToLive,
      stashCapacity = MqttBroker.stashCapacity,
      reconnectDelayMin = MqttBroker.reconnectDelayMin,
      reconnectDelayMax = MqttBroker.reconnectDelayMax
    )))

  private def emptyToNull(str: String) =
    if (str == null || str.trim.isEmpty) null
    else str
}
