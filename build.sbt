lazy val root = (project in file("."))
  .settings(
    name := "shogi-server",
    version := "0.1",
    scalaVersion := "2.11.8"
  ).enablePlugins(PlayScala)

cleanFiles += file("public/bundles")