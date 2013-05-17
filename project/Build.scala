

import sbt._
import Keys._
import Process._
import java.io.File



object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq (
    name := "sync-test",
    version := "0.1",
    resolvers += ScalaToolsSnapshots,
    scalaVersion := "2.10.1",
    scalacOptions ++= Seq("-deprecation", "-optimise"),
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.10.1"
      , "com.github.axel22" %% "scalameter" % "0.4-M1"
    ),
    testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
    logBuffered := false
  )
}


object WorkstealingBuild extends Build {
  
  /* projects */

  lazy val root = Project(
    "root",
    file("."),
    settings = BuildSettings.buildSettings
  )

}










