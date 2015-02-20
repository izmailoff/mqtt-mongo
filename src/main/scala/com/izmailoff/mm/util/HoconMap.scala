package com.izmailoff.mm.util

import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._

/**
 * Reads a map of values from HOCON config file (.conf) since such functionality is not provided by default.
 */
object HoconMap {

  /**
   * Reads a map of values from conf
   * @param keyF a function for converting to Key type
   * @param valF a function for converting to Value type
   * @param conf a config at any path level, see @path
   * @param path a non-empty path aka a key name which contains the map of values
   * @tparam Key
   * @tparam Value
   * @return returns a map that contains all values from the config or throws an exception if configuration
   *         was not in a correct format.
   */
  def getMap[Key, Value](keyF: String => Key, valF: String => Value, conf: Config, path: String): Map[Key, Value] = {
    val list = conf.getObjectList(path).asScala
    (for {
      item <- list
      entry <- item.entrySet().asScala
      key = keyF(entry.getKey)
      value = valF(entry.getValue.unwrapped().toString)
    } yield (key, value)).toMap
  }

}
