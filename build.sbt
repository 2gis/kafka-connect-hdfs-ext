organization := "io.github.2gis"

name := "kafka-connect-hdfs-ext"

val kafkaVersion = "2.3.0"

val confluentVersion = "5.2.3"

resolvers ++= Seq(
  "Confluent repo" at "https://packages.confluent.io/maven/"
)

libraryDependencies ++= Seq(
  "org.apache.kafka" % "connect-api" % kafkaVersion % "provided",
  "io.confluent" % "kafka-connect-hdfs" % confluentVersion % "provided" exclude ("org.pentaho", "pentaho-aggdesigner-algorithm")
)

autoScalaLibrary := false

