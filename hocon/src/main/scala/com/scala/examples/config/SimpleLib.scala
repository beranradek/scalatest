package com.scala.examples.config

import com.typesafe.config._

// we have a constructor allowing the app to provide a custom Config
class SimpleLibContext(config: Config) {

    // This verifies that the Config is sane and has our
    // reference config. Importantly, we specify the "simple-lib"
    // path so we only validate settings that belong to this
    // library. Otherwise, we might throw mistaken errors about
    // settings we know nothing about.
	// Validates this config against a reference config, throwing an exception
    // if it is invalid.
    config.checkValid(ConfigFactory.defaultReference(), "simple-lib")
    
    // Validating presence of keys (throws ConfigException.Missing) if settings is absent 
    // note that these fields are NOT lazy, because if we're going to
    // get any exceptions, we want to get them on startup.
    val foo = config.getString("simple-lib.foo")
    val hello = config.getString("simple-lib.hello")
    val whatever = config.getString("simple-lib.whatever")

    // This uses the standard default Config, if none is provided,
    // which simplifies apps willing to use the defaults
    def this() {
    	// default instance of Config (another constructor)
        this(ConfigFactory.load())
    }

    def getSetting(path: String) = config.getString(path)
}