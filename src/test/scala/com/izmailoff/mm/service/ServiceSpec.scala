package com.izmailoff.mm.service

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import com.izmailoff.mm.config.GlobalAppConfig
import com.sandinh.paho.akka.{Message, Subscribe, SubscribeAck}
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._
import scala.jdk.CollectionConverters._


class ServiceSpec
  extends TestKit(ActorSystem("test-mqtt-mongo-system", GlobalAppConfig.config))
  with DefaultTimeout
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with TestMqttMongoServiceImpl
  with TestHelpers {

  override def afterAll() {
    shutdown()
  }

  "Subscription between MQTT Broker and Consumer" should {
    "get established when consumer is started" in {
      val mqttBroker = startMqttIntermediary()
      val probe = TestProbe()
      val mqttConsumer = startMqttConsumer(probe.ref)

      probe.expectMsg(Subscribe(testTopic, mqttConsumer))
      probe.forward(mqttBroker, Subscribe(testTopic, probe.ref))
      probe.expectMsg(SubscribeAck(Subscribe(testTopic, probe.ref), None))
      probe.forward(mqttConsumer, SubscribeAck(Subscribe(testTopic, mqttConsumer), None))
      probe.expectNoMsg()
    }
  }

  "Sending a message to MQTT Broker" should {
    "forward it to MQTT Consumer and get saved in DB in proper JSON format" in {
      val collection = getCollectionName(testTopic).head
      db.getCollection(collection).count() should be(0)
      val mqttBroker = startMqttIntermediary()
      val mqttConsumer = startMqttConsumer(mqttBroker)
      expectNoMsg(1.second)

      mqttBroker ! new Message(testTopic, "test content".getBytes)
      mqttBroker ! new Message(testTopic, """{ "field1" : "str val", "field2" : 123 }""".getBytes)
      expectNoMsg(1.second)

      db.getCollection(collection).count() should be(2)
      val allDocsDb = db.getCollection(collection).find().iterator.asScala
      allDocsDb.exists { d =>
        val fields: Map[Any, Any] = d.toMap.asScala.toMap
        fields.size == 2 &&
          fields("payload") == "test content"
      } should be(true)
      allDocsDb.exists { d =>
        val fields: Map[Any, Any] = d.toMap.asScala.toMap
        fields.size == 3 &&
          fields("field1") == "str val" &&
          fields("field2") == 123
      } should be(true)
    }
  }


}
