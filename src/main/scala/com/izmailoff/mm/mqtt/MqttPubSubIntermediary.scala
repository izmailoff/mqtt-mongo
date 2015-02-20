package com.izmailoff.mm.mqtt

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.sandinh.paho.akka.MqttPubSub
import com.sandinh.paho.akka.MqttPubSub.{SubscribeAck, Subscribe, PSConfig}
import com.izmailoff.mm.config.GlobalAppConfig.Application.Mqtt.MqttBroker

object MqttPubSubIntermediary {

  def start(system: ActorSystem): ActorRef =
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
