package com.izmailoff.mm.util

import com.typesafe.config.{ConfigException, ConfigFactory}
import org.specs2.mutable.Specification

class HoconMapSpec extends Specification {

  "HOCON config" should {
    val rawConf =
      """
        |application {
        |  someSettings {
        |    mappings = [
        |      { "one" : 2000 },
        |      { "two" : 3000 },
        |      { "three" : 5000 }
        |    ]
        |  }
        |}
      """.stripMargin
    val conf = ConfigFactory.parseString(rawConf)

    "parse map like entries given a root conf and full path and create a proper Scala map from them" in {
      val expected = Map("one" -> 2000L,
        "two" -> 3000L,
        "three" -> 5000L)
      HoconMap.getMap[String, Long](identity(_), _.toLong, conf, "application.someSettings.mappings") === expected
    }

    "throw config exception for incomplete/wrong path because full path is required" in {
      HoconMap.getMap[String, Long](identity(_), _.toLong, conf, "mappings") must throwA[ConfigException]
    }
  }

}
