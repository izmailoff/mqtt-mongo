package com.izmailoff.mm.mongo

import com.izmailoff.mm.config.GlobalAppConfig.Application.Mongo
import org.mongodb.scala.{MongoClient, MongoDatabase}

trait MongoDbProvider {

  val db: MongoDatabase
}

trait MongoDbProviderImpl
extends MongoDbProvider {

  private val client = MongoClient(Mongo.uri)
  val db = client.getDatabase(Mongo.dbName)
}
