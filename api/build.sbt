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

val jacksonVersion = "2.6.3"

libraryDependencies += "org.scaldi" %% "scaldi-play" % "0.5.10"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"
libraryDependencies += "com.paypal" %% "cascade-common" % "0.5.1" excludeAll (ExclusionRule("com.fasterxml.jackson.dataformat"), ExclusionRule("com.fasterxml.jackson.core"), ExclusionRule("com.fasterxml.jackson.module"), ExclusionRule("com.fasterxml.jackson.datatype"))
libraryDependencies += "com.paypal" %% "cascade-json" % "0.5.1" excludeAll (ExclusionRule("com.fasterxml.jackson.dataformat"), ExclusionRule("com.fasterxml.jackson.core"), ExclusionRule("com.fasterxml.jackson.module"), ExclusionRule("com.fasterxml.jackson.datatype"))
libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.11.3"
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.0"
libraryDependencies += "org.flywaydb" % "flyway-core" % "3.2.1"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1205-jdbc42" exclude("org.slf4j", "slf4j-simple")

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % jacksonVersion
libraryDependencies += "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % jacksonVersion
libraryDependencies += "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion
libraryDependencies += "com.fasterxml.woodstox" % "woodstox-core" % "5.0.1"

libraryDependencies += "org.scalatestplus" %% "play" % "1.4.0-M4" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test"
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % "test"

routesGenerator := InjectedRoutesGenerator
