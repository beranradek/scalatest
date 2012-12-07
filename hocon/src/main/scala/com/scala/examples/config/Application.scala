package com.scala.examples.config

import com.typesafe.config.ConfigFactory

object Application extends App {
	// example of how system properties override default configuration; note this
    // must be set before the config lib is used
    System.setProperty("simple-lib.whatever", "This value comes from a system property (whatever)")

    // Load our own config values from the default location, application.conf,
    // or else from resource.conf of used library if not overriden in application.conf 
    val conf = ConfigFactory.load()
    // val context = new SimpleLibContext()
    
    println("Value of foo is: " + conf.getString("simple-lib.foo")) // should return value from reference.conf
    println("Value of hello is: " + conf.getString("simple-lib.hello")) // should return value from application.conf (overriden in application.conf)
    println("Value of whatever is: " + conf.getString("simple-lib.whatever")) // should return value from system properties
}