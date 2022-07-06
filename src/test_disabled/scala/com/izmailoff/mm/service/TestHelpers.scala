package com.izmailoff.mm.service

import com.izmailoff.mm.config.GlobalAppConfig

trait TestHelpers {

  val testTopic = "test"

  def getCollectionName(topic: String = testTopic) =
    GlobalAppConfig.Application.MqttMongo.topicsToCollectionsMappings(topic)


}
