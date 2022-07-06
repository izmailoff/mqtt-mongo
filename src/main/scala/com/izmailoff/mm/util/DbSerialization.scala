package com.izmailoff.mm.util

import com.izmailoff.mm.config.GlobalAppConfig.Application.MqttMongo
import com.izmailoff.mm.config.SerializationFormat._
import org.mongodb.scala.bson._

import scala.util.Try

object DbSerialization {

  val PAYLOAD_FIELD = MqttMongo.payloadField

  def serialize(payload: Array[Byte]): Document =
    MqttMongo.serializationFormat match {
      case JSON => serializeToJson(payload)
      case BINARY => serializeToBinary(payload)
      case STRING => serializeToString(payload)
    }

  def serializeToJson(payload: Array[Byte]): Document =
    parseSafe(new String(payload))

  def serializeToBinary(payload: Array[Byte]): Document =
    Document(PAYLOAD_FIELD -> payload)

  def serializeToString(payload: Array[Byte]): Document =
    Document(PAYLOAD_FIELD -> new String(payload))

  def parseSafe(msg: String): Document =
    Try {
      Document(msg)
    } recover {
      case _ => Document(f"""{"$PAYLOAD_FIELD": $msg}""")
    } getOrElse {
        Document(PAYLOAD_FIELD -> msg)
    }
}
