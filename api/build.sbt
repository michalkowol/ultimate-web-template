name := "play-scala"
scalaVersion in ThisBuild := "2.11.7"

incOptions := incOptions.value.withNameHashing(nameHashing = true)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-encoding", "UTF-8", "-Ywarn-adapted-args:false")

jacoco.settings
jacoco.thresholds in jacoco.Config := de.johoop.jacoco4sbt.Thresholds(instruction = 75, method = 75, branch = 45, complexity = 55, line = 85, clazz = 85)

lazy val root = (project in file(".")).enablePlugins(PlayScala).disablePlugins(PlayLayoutPlugin)

libraryDependencies += jdbc
libraryDependencies += ws
libraryDependencies += filters

libraryDependencies += "org.scaldi" %% "scaldi-play" % "0.5.10"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"
libraryDependencies += "com.paypal" %% "cascade-common" % "0.5.1"
libraryDependencies += "com.paypal" %% "cascade-json" % "0.5.1"
libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.11.3"
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.0"
libraryDependencies += "org.flywaydb" % "flyway-core" % "3.2.1"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1204-jdbc42" exclude("org.slf4j", "slf4j-simple")

libraryDependencies += "org.scalatestplus" %% "play" % "1.4.0-M4" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test"
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % "test"

routesGenerator := InjectedRoutesGenerator
