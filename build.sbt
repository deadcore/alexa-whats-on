name := "alexa-whats-on"

version := "0.1"

scalaVersion := "2.12.5"

assemblyJarName in assembly := "alexa-whats-on.jar"


val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scalaj" %% "scalaj-http" % "2.4.0",
  "com.amazon.alexa" % "alexa-skills-kit" % "1.8.1",
  "com.amazonaws" % "aws-lambda-java-log4j2" % "1.1.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.0",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.11.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
)

// Assembly config

target in assembly := baseDirectory.value
test in assembly := {}

assemblyMergeStrategy in assembly := {
  case PathList(ps@_*) if ps.last == "Log4j2Plugins.dat" => MergeStrategy.last
  case PathList("META-INF", xs@_*)                       => MergeStrategy.discard
  case x                                                 => MergeStrategy.first
}