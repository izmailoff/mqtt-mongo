package com.izmailoff.mm.config

import java.util.concurrent.TimeUnit

import com.izmailoff.mm.util.HoconMap
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.{FiniteDuration, _}
import com.typesafe.config.Config

object GlobalAppConfig:

  val config: Config = ConfigFactory.load()

  object Application:

    object MqttBroker:
      private lazy val brokerConf = config.getConfig("application.mqttBroker")
      lazy val url: String = brokerConf.getString("url")
      lazy val userName: String = brokerConf.getString("userName")
      lazy val password: String = brokerConf.getString("password")
      lazy val stashTimeToLive: FiniteDuration =
        brokerConf.getDuration("stashTimeToLive", TimeUnit.SECONDS).seconds
      lazy val stashCapacity: Int = brokerConf.getInt("stashCapacity")
      lazy val reconnectDelayMin: FiniteDuration =
        brokerConf.getDuration("reconnectDelayMin", TimeUnit.SECONDS).seconds
      lazy val reconnectDelayMax: FiniteDuration =
        brokerConf.getDuration("reconnectDelayMax", TimeUnit.SECONDS).seconds

    object Mongo:
      private lazy val mongoConf = config.getConfig("application.mongo")
      lazy val host: String = mongoConf.getString("host")
      lazy val port: Int = mongoConf.getInt("port")
      lazy val dbName: String = mongoConf.getString("dbName")
      lazy val uri: String = mongoConf.getString("uri")

    object MqttMongo:
      private lazy val mqttMongoConf = config.getConfig("application.mqttMongo")
      lazy val topicsToCollectionsMappings: Map[String, Set[String]] =
        HoconMap.getMap(identity(_), getElems,
          mqttMongoConf, "topicsToCollectionsMappings").withDefaultValue(Set.empty)
      val getElems: String => Set[String] =
        _.split(";").toList.map(_.trim).filter(!_.isEmpty).toSet
      lazy val serializationFormat: SerializationFormat.Value = SerializationFormat.withName(mqttMongoConf.getString("serializationFormat"))
      lazy val payloadField: String = mqttMongoConf.getString("payloadField")


