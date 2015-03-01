package com.izmailoff.mm.service

import com.github.fakemongo.Fongo
import com.izmailoff.mm.config.GlobalAppConfig.Application.Mongo
import com.izmailoff.mm.mongo.MongoDbProvider
import com.mongodb.casbah.Imports._

trait TestMongoDbProviderImpl
  extends MongoDbProvider {

  private val mongoClient = new Fongo("in-memory-db")

  val db = mongoClient.getDB(Mongo.dbName).asScala
}
