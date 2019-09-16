name := "riot-bma280-demo"

maintainer := "frederic@auberson.net"

version := "1.0"

scalaVersion := "2.12.7"

resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

libraryDependencies ++= Seq(
  "org.riot-framework" % "riot-core" % "0.1-SNAPSHOT"
)

enablePlugins(JavaServerAppPackaging)
