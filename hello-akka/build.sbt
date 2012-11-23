name := "hello-akka"

version := "1.0"

scalaVersion := "2.9.2"

// Keys of sbt eclipse plugin are in EclipseKeys 
// Let's create also resource source folders (then reload, eclipse with-source=true)
// See: https://github.com/typesafehub/sbteclipse/wiki/Using-sbteclipse
EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

// Dependencies (in IVY syntax):
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
 
libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.4"