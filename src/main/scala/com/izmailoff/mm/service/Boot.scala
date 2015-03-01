package com.izmailoff.mm.service

import akka.actor.ActorSystem
import com.izmailoff.mm.mongo.MongoDbProviderImpl
import com.izmailoff.mm.mqtt.MqttIntermediary

object Boot
  extends App
  with MqttMongoService
  with MqttIntermediary
  with MongoDbProviderImpl {

  val system = ActorSystem("mqtt-mongo-system")
  import system.log
  val banner =
    """
      | __  __  ___ _____ _____     __  __
      ||  \/  |/ _ \_   _|_   _|   |  \/  | ___  _ __   __ _  ___
      || |\/| | | | || |   | |_____| |\/| |/ _ \| '_ \ / _` |/ _ \
      || |  | | |_| || |   | |_____| |  | | (_) | | | | (_| | (_) |
      ||_|  |_|\__\_\|_|   |_|     |_|  |_|\___/|_| |_|\__, |\___/
      |                                                |___/
      |
    """.stripMargin
  log.info(banner)

  val pubSubIntermediary = startMqttIntermediary()

  val messageConsumer = startMqttConsumer(pubSubIntermediary)

  log.info("APPLICATION STARTED!")
}
