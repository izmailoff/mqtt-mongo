package com.izmailoff.mm.mongo

import com.izmailoff.mm.config.GlobalAppConfig.Application.Mongo
import com.mongodb.casbah.Imports._

trait MongoDbProvider {

  val db: MongoDB
}

trait MongoDbProviderImpl
extends MongoDbProvider {

  private val mongoClient = MongoClient(Mongo.host, Mongo.port)

  val db = mongoClient(Mongo.dbName)
}
