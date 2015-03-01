package com.izmailoff.mm.service

import akka.actor.ActorSystem

object Boot
  extends App
  with MqttMongoServiceImpl {

  val system = ActorSystem("mqtt_mongo_system")
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

  val pubSubIntermediary = startMqttIntermediary(system)

  val messageConsumer = startMqttConsumer(system, pubSubIntermediary)

  log.info("APPLICATION STARTED!")
}
