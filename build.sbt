
name := "mqtt-mongo"

version := "0.1"
     
scalaVersion := "2.11.6"

javacOptions ++= Seq("-target", "1.7")

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "org.mongodb" %% "casbah" % "2.8.0",
    "com.sandinh" %% "paho-akka" % "1.0.2",
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.specs2" %% "specs2" % "2.3.12" % "test", // TODO: update to latest
    "com.github.fakemongo" % "fongo" % "1.6.0" % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )
}

resolvers ++= Seq(
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  "Paho Eclipse Repository" at "https://repo.eclipse.org/content/repositories/paho-releases/",
  Classpaths.sbtPluginReleases
)
