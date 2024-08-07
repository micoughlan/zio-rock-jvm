ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "zio-rockthejvm"
  )

libraryDependencies += "dev.zio" %% "zio" % "2.1.4"
