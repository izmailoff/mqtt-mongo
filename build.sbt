
name := "mqtt-mongo"

version := "0.1"
     
scalaVersion := "2.11.5"

javacOptions ++= Seq("-target", "1.7")

libraryDependencies ++= {
  val akkaVersion = "2.3.8"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    //"com.github.nscala-time" %% "nscala-time" % "1.6.0",
    "com.sandinh" % "paho-akka_2.11" % "1.0.1",
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.2"
  )
}


resolvers ++= Seq(
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  "Paho Eclipse Repository" at "https://repo.eclipse.org/content/repositories/paho-releases/"
)
