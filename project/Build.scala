import sbt._
import Keys._

object Build extends Build {
  lazy val core = Project("root", file("./"),
    settings = Project.defaultSettings ++ Seq(
      name := "markdown-extension.extension",
      version := "0.0",
      scalaVersion := "2.11.2",
      resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
      libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2",
      libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.4",
      libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
      libraryDependencies += "junit" % "junit" % "4.11" % "test"
    )
  )
}
