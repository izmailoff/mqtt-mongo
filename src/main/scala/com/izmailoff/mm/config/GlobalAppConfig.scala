package com.izmailoff.mm.config

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.{FiniteDuration, _}
import scala.collection.JavaConversions._

object GlobalAppConfig {

  private val config = ConfigFactory.load()

  object Application {

    object Mqtt {

      object MqttBroker {
        private lazy val mqqtConsumerConf = config.getConfig("application.mqtt.mqttBroker")
        lazy val url = mqqtConsumerConf.getString("url")
        lazy val userName = mqqtConsumerConf.getString("userName")
        lazy val password = mqqtConsumerConf.getString("password")
        lazy val stashTimeToLive: FiniteDuration =
          mqqtConsumerConf.getDuration("stashTimeToLive", TimeUnit.SECONDS) seconds
        lazy val stashCapacity = mqqtConsumerConf.getInt("stashCapacity")
        lazy val reconnectDelayMin: FiniteDuration =
          mqqtConsumerConf.getDuration("reconnectDelayMin", TimeUnit.SECONDS) seconds
        lazy val reconnectDelayMax: FiniteDuration =
          mqqtConsumerConf.getDuration("reconnectDelayMax", TimeUnit.SECONDS) seconds
      }
    }
  }

}
