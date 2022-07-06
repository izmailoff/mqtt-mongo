package com.izmailoff.mm.mongo

import com.izmailoff.mm.config.GlobalAppConfig.Application.Mongo
import org.mongodb.scala.{MongoClient, MongoDatabase}

trait MongoDbProvider {

  val db: MongoDatabase
}

trait MongoDbProviderImpl
extends MongoDbProvider {

  private val client = MongoClient(Mongo.uri)
  val db: MongoDatabase = client.getDatabase(Mongo.dbName)
}
