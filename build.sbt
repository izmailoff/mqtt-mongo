
name := "mqtt-mongo"

version := "0.2"
     
scalaVersion := "2.13.8"

//javacOptions ++= Seq("-target", "11")

libraryDependencies ++= {
  val akkaVersion = "2.6.19"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "org.mongodb.scala" %% "mongo-scala-driver" % "4.6.1",
    "com.sandinh" %% "paho-akka" % "1.6.1",
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.11",
    "org.specs2" %% "specs2-core" % "4.16.1" % "test", 
    "com.github.fakemongo" % "fongo" % "2.1.1" % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "3.2.12" % "test"
  )
}
