// Some own imports: 
// import my.sbt._

// Setting key is evaluated only once the project is load (unlike the task key)
name := "scala-build"

version := "1.0"

scalaVersion := "2.9.2"

// Dependencies (in IVY syntax):
libraryDependencies += "org.apache.derby" % "derby" % "10.4.1.3"

libraryDependencies += "org.scala-tools" % "scala-stm_2.9.1" % "0.3"