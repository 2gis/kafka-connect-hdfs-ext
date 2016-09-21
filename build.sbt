organization := "ru.dgis.casino"

name := "kafka-connect-hdfs-ext"

val kafkaV = "0.10.0.1-cp1"

val confluentV = "3.0.1"

resolvers += "Confluent repo" at "http://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "connect-runtime" % kafkaV % "runtime",
  "org.apache.kafka" % "connect-api" % kafkaV % "provided",
  "io.confluent" % "kafka-connect-hdfs" % confluentV % "provided"
)

autoScalaLibrary := false
