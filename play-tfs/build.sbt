name := "play-tfs"

version := "1.0"

description := "Play TFS integration"

scalaVersion := "2.9.2"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

// Resolver for ETN repository:
resolvers += Resolver.url("art-etn", new URL("http://artifactory.etn/etn"))(Patterns(false, "[organisation]/[module]/[revision]/[artifact]-[revision].[ext]") )

resolvers += Resolver.url("my-test-repo", new URL("http://artifactory.etn/twinstone"))(Patterns(false, "[organisation]/[module]/[revision]/[artifact]-[revision].[ext]") )     

libraryDependencies += "org.zweistein" % "wbeans-tfs" % "2.0.1"

libraryDependencies += "org.scala-tools" % "scala-stm_2.9.1" % "0.3"