package com.izmailoff.mm.config

import java.util.concurrent.TimeUnit

import com.izmailoff.mm.util.HoconMap
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.{FiniteDuration, _}

object GlobalAppConfig {

  val config = ConfigFactory.load()

  object Application {

    object MqttBroker {
      private lazy val brokerConf = config.getConfig("application.mqttBroker")
      lazy val url = brokerConf.getString("url")
      lazy val userName = brokerConf.getString("userName")
      lazy val password = brokerConf.getString("password")
      lazy val stashTimeToLive: FiniteDuration =
        brokerConf.getDuration("stashTimeToLive", TimeUnit.SECONDS) seconds
      lazy val stashCapacity = brokerConf.getInt("stashCapacity")
      lazy val reconnectDelay: FiniteDuration =
        brokerConf.getDuration("reconnectDelayMin", TimeUnit.SECONDS) seconds
      lazy val reconnectDelayMax: FiniteDuration =
        brokerConf.getDuration("reconnectDelayMax", TimeUnit.SECONDS) seconds
    }

    object Mongo {
      private lazy val mongoConf = config.getConfig("application.mongo")
      lazy val host = mongoConf.getString("host")
      lazy val port = mongoConf.getInt("port")
      lazy val dbName = mongoConf.getString("dbName")
    }

    object MqttMongo {
      private lazy val mqttMongoConf = config.getConfig("application.mqttMongo")
      lazy val topicsToCollectionsMappings: Map[String, Set[String]] =
        HoconMap.getMap(identity(_), getElems,
          mqttMongoConf, "topicsToCollectionsMappings").withDefaultValue(Set.empty)
      val getElems: String => Set[String] =
        _.split(";").toList.map(_.trim).filter(!_.isEmpty).toSet
    }

  }

}
