lazy val root = (project in file("."))
  .settings(
    name := "shogi-server",
    version := "0.1",
    scalaVersion := "2.11.8",
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions"
    )
  ).enablePlugins(PlayScala)

cleanFiles += file("public/bundles")
