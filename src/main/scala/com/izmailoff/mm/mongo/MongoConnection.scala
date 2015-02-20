package com.izmailoff.mm.mongo

import com.mongodb.casbah.Imports._
import com.izmailoff.mm.config.GlobalAppConfig.Application.Mongo

object MongoConnection {

  val mongoClient = MongoClient(Mongo.host, Mongo.port)

  val db = mongoClient(Mongo.dbName)

}
