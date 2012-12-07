name := "hocon"

version := "1.0"

description := "HOCON example"

scalaVersion := "2.9.2"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

libraryDependencies += "org.scala-tools" % "scala-stm_2.9.1" % "0.3"

libraryDependencies += "com.typesafe" % "config" % "1.0.0"