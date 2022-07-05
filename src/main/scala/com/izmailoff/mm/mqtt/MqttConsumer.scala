package com.izmailoff.mm.mqtt

import akka.actor._
import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttMongo
import com.izmailoff.mm.mongo.MongoDbProvider
import com.izmailoff.mm.util.DbSerialization._
import com.sandinh.paho.akka.{Message, Subscribe, SubscribeAck}
import org.mongodb.scala.{Completed, Document, MongoDatabase, Observer}

class MqttConsumer(pubSubIntermediary: ActorRef, db: MongoDatabase)
  extends Actor
  with ActorLogging {

  val topicsCollectionsMappings = MqttMongo.topicsToCollectionsMappings

  private def resultHandler(doc: Document) = new Observer[Completed] {
    override def onNext(result: Completed): Unit = log.debug(f"Inserted $doc into db successfully.")
    override def onError(e: Throwable): Unit = log.error(e, f"Failed to insert $doc into db.")
    override def onComplete(): Unit = log.debug(f"Insert $doc finished.")
  }

  subscribe()

  def subscribe() =
    topicsCollectionsMappings foreach {
      case (t, c) => pubSubIntermediary ! Subscribe(t, self)
    }

  def receive = {
    case msg: Message =>
      val (doc, collections) = getPersistenceRecipe(msg)
      saveDb(doc, collections)
      log.debug(s"topic: [${msg.topic}], payload: [$doc] is saved to collections: [$collections].")
    case SubscribeAck(Subscribe(topic, `self`, _), None) =>
      log.info(s"Subscription to topic [$topic] acknowledged.")
    case SubscribeAck(Subscribe(topic, `self`, _), Some(err)) =>
      log.error(err, s"Subscription to topic [$topic] failed.")
      // TODO: implement error recovery or terminate the service
  }

  def getPersistenceRecipe(msg: Message) = {
    import msg._
    val collections = topicsCollectionsMappings(topic)
    val doc = serialize(payload)
    (doc, collections)
  }

  def saveDb(doc: Document, collections: Set[String]): Unit =
    collections foreach { collName =>
      val res = db.getCollection[Document](collName).insertOne(doc)
      res.subscribe(resultHandler(doc))
    }
}

trait MqttConsumerComponent
  extends MongoDbProvider {
  def system: ActorSystem

  def startMqttConsumer(pubSubIntermediary: ActorRef): ActorRef =
    system.actorOf(Props(new MqttConsumer(pubSubIntermediary, db)))
}
