package com.izmailoff.mm.service

import com.github.fakemongo.Fongo
//import com.izmailoff.mm.config.GlobalAppConfig.Application.Mongo
import com.izmailoff.mm.mongo.MongoDbProvider

trait TestMongoDbProviderImpl
  extends MongoDbProvider {

  private val mongoClient = new Fongo("in-memory-db")

//  com.mongodb.MongoClient
  val db = mongoClient.getMongo()
//  val db = mongoClient.getDB(Mongo.dbName) //.getDatabase()
}
