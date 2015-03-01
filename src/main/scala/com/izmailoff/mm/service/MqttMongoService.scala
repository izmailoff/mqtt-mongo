package com.izmailoff.mm.service

import com.izmailoff.mm.mqtt.{MqttConsumerComponent, MqttIntermediaryComponent}

trait MqttMongoService
  extends MqttIntermediaryComponent
  with MqttConsumerComponent {

}
