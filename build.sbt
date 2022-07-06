
name := "mqtt-mongo"

version := "1.0.0"

scalaVersion := "3.1.3"

organization := "izmailoff"

scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-deprecation",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions",
//  "-Xtarget:8",
  "-unchecked",
  "-indent",
)


enablePlugins(DockerPlugin)

//addDependencyTreePlugin

libraryDependencies ++= {
  val akkaVersion = "2.6.19"
  Seq(
//    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "org.mongodb.scala" %% "mongo-scala-driver" % "4.6.1" cross CrossVersion.for3Use2_13,
    "com.sandinh" %% "paho-akka" % "1.6.1",
//    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.11",
//    "org.specs2" %% "specs2-core" % "4.16.1" % "test", // 5.0.2
//    "com.github.fakemongo" % "fongo" % "2.1.1" % "test",
//    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
//    "org.scalatest" %% "scalatest" % "3.2.12" % "test"
  )
}


docker / imageNames := Seq(ImageName(s"${organization.value}/${name.value}:${version.value}"))

docker / dockerfile := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("openjdk:8-jre")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}
