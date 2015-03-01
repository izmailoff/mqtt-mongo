package com.izmailoff.mm.util

import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.util.JSON
import org.bson.types.BasicBSONList

import scala.util.{Failure, Success, Try}

object JsonDbSerialization {

  val PAYLOAD_FIELD = "payload"

  def parseSafe(msg: String): DBObject =
    Try {
      JSON.parse(msg).asInstanceOf[DBObject]
    } match {
      case Failure(e) =>
        MongoDBObject(PAYLOAD_FIELD -> msg)
      case Success(json) =>
        json match {
          case j:BasicBSONList => MongoDBObject(PAYLOAD_FIELD -> json)
          case other => other
        }
    }
}
