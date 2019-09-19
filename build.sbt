import riot.riotctl.sbt.RiotCtl._

name := "riot-bma280-demo"

maintainer := "frederic@auberson.net"

version := "1.0"

scalaVersion := "2.12.7"

resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

libraryDependencies ++= Seq(
  "org.riot-framework" % "riot-core" % "0.+",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

lazy val root = (project in file(".")).enablePlugins(JavaServerAppPackaging).settings(
  publishArtifact in (Compile, packageDoc) := false,
  riotTargets := Seq(
    riotTarget("raspberrypi", "pi", "raspberry")
  ),
  riotPrereqs := "oracle-java8-jdk wiringpi i2c-tools",
  riotRequiresI2C := true
)
