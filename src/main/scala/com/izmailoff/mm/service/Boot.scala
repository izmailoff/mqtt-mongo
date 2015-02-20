package com.izmailoff.mm.service

import akka.actor.ActorSystem
import com.izmailoff.mm.mqtt.{MqttConsumer, MqttPubSubIntermediary}

object Boot extends App {

  val system = ActorSystem("wifi_presense_system")
  import com.izmailoff.mm.service.Boot.system.log

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

  val pubSubIntermediary = MqttPubSubIntermediary.start(system)

  val messageConsumer = MqttConsumer.start(system, pubSubIntermediary)

  log.info("APPLICATION STARTED!")
}
