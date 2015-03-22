package com.izmailoff.mm.util

import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttMongo
import com.izmailoff.mm.config.SerializationFormat._
import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.BasicBSONList

import scala.util.{Failure, Success, Try}

object DbSerialization {

  val PAYLOAD_FIELD = "payload"

  def serialize(payload: Array[Byte]): DBObject =
    MqttMongo.serializationFormat match {
      case JSON => serializeToJson(payload)
      case BINARY => serializeToBinary(payload)
      case STRING => serializeToString(payload)
    }

  def serializeToJson(payload: Array[Byte]) =
    parseSafe(new String(payload))

  def serializeToBinary(payload: Array[Byte]) =
    MongoDBObject(PAYLOAD_FIELD -> payload)

  def serializeToString(payload: Array[Byte]) =
    MongoDBObject(PAYLOAD_FIELD -> new String(payload))

  def parseSafe(msg: String): DBObject =
    Try {
      com.mongodb.util.JSON.parse(msg).asInstanceOf[DBObject]
    } match {
      case Failure(e) =>
        MongoDBObject(PAYLOAD_FIELD -> msg)
      case Success(json) =>
        json match {
          case j: BasicBSONList => MongoDBObject(PAYLOAD_FIELD -> json)
          case other => other
        }
    }
}
