package com.izmailoff.mm.mongo

import com.mongodb.DBObject

trait MongoSerializer
extends MongoDbProvider {

  def saveDb(doc: DBObject, collections: Set[String]): Unit =
    collections foreach { collName =>
      db(collName).insert(doc)
    }

}
