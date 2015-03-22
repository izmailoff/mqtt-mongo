package com.izmailoff.mm.config

import com.izmailoff.mm.config.GlobalAppConfig.Application._
import org.specs2.matcher.DataTables
import org.specs2.mutable.Specification

class GlobalAppConfigSpec
extends Specification
with DataTables {

  "Global config" should {
    "resolve all settings without any exceptions" in {
      // TODO: complete this test

      "config value"      || "expected value"       |
      Mongo.dbName        !! "mqtt"                 |
      //MqttBroker.reconnectDelay !! FiniteDuration(10, TimeUnit.SECONDS) |
      Mongo.host          !! "localhost"            |
      Mongo.port          !! 27017                  |
      MqttBroker.url      !! "tcp://localhost:1883" |
      MqttBroker.userName !! ""                     |
      MqttBroker.password !! ""                     |> {
        (actual, expected) =>
          actual must be equalTo expected
      }
    }
  }

}
