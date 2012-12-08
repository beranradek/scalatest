package com.scala.examples.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigException

/**
 * @author Radek Beran
 */
object ConfigHelper {
  def getStrings(cfg: Config, pathWithoutIndices: String, startIndex: Int): List[String] = 
    untilPresent(cfg, pathWithoutIndices, startIndex, Nil).reverse
  
  private def untilPresent(cfg: Config, pathWithoutIndices: String, index: Int, strings: List[String]): List[String] = {
    tryGetString(cfg, pathWithoutIndices + index) match {
      case None => strings
      case Some(str) => untilPresent(cfg, pathWithoutIndices, index + 1, str :: strings) 
    }
  }
  
  private def tryGetString(cfg: Config, path: String): Option[String] =
    try {
      Some(cfg.getString(path))
    } catch {
      case ex: ConfigException => None 
    }
}