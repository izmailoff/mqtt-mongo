package com.izmailoff.mm.mqtt

import akka.actor.{ActorRef, ActorSystem, Props}
import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttBroker
import com.sandinh.paho.akka.{ConnOptions, MqttPubSub, PSConfig}
import com.izmailoff.mm.util.StringUtils._

trait MqttIntermediary
  extends MqttIntermediaryComponent:

  def system: ActorSystem

  def startMqttIntermediary(): ActorRef =
    system.actorOf(Props(classOf[MqttPubSub], PSConfig(
      brokerUrl = MqttBroker.url,
      conOpt = ConnOptions(username = emptyToNull(MqttBroker.userName), password = emptyToNull(MqttBroker.password)),
      stashTimeToLive = MqttBroker.stashTimeToLive,
      stashCapacity = MqttBroker.stashCapacity,
      reconnectDelayMin = MqttBroker.reconnectDelayMin,
      reconnectDelayMax = MqttBroker.reconnectDelayMax
    )), name = "MqttIntermediary")

trait MqttIntermediaryComponent:

  def startMqttIntermediary(): ActorRef
