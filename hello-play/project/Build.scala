import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "hello-play"
    val appVersion      = "1.0-SNAPSHOT"
      
    implicit val etnIvyPatterns = Patterns(false, "[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]")

    lazy val etnRepo = Resolver.url("ETN Repository", new URL("http://artifactory.etn/etn"))
    lazy val twnRepo = Resolver.url("Twinstone Repository", new URL("http://artifactory.etn/twinstone"))
    lazy val etnScratch = Resolver.url("ETN Experimental Repository", new URL("http://artifactory.etn/etn-scratch"))
      
    val appDependencies = Seq(
      "com.etnetera" %% "play-tfs" % "0.1-SNAPSHOT"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers ++= Seq(
          etnRepo, twnRepo, etnScratch
      )
    )

}