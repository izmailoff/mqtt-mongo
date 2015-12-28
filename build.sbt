
name := "mqtt-mongo"

version := "0.1"
     
scalaVersion := "2.11.7"

javacOptions ++= Seq("-target", "1.8")

libraryDependencies ++= {
  val akkaVersion = "2.4.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "org.mongodb" %% "casbah" % "3.1.0",
    "com.sandinh" %% "paho-akka" % "1.2.0",
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.specs2" %% "specs2" % "2.3.12" % "test", // TODO: update to latest
    "com.github.fakemongo" % "fongo" % "2.0.4" % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )
}

resolvers ++= Seq(
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  "Paho Eclipse Repository" at "https://repo.eclipse.org/content/repositories/paho-releases/",
  Classpaths.sbtPluginReleases
)
