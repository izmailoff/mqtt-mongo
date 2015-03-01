package com.izmailoff.mm.service

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.sandinh.paho.akka.MqttPubSub.Message
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ServiceSpec
  extends TestKit(ActorSystem("test-mqtt-mongo-system"))
  with DefaultTimeout
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with TestMqttMongoServiceImpl {

  override def afterAll {
    shutdown()
  }

  "something" should {
    "do smth" in {
      1 should be(1)
    }
  }

  "test actor" should {
    "do this" in {
      db.getCollection("messages").count() should equal(0)
      val mqttBroker = startMqttIntermediary()
      val mqttConsumer = startMqttConsumer(mqttBroker)
      mqttBroker ! new Message("test", "test content".getBytes)
      mqttBroker ! new Message("test", """{ "field1" : "str val", "field2" : 123 }""".getBytes)
      //db.getCollection("test").count() should equal(2)
    }
  }

}
