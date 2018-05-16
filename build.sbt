organization := "ru.dgis.casino"

name := "kafka-connect-hdfs-ext"

val kafkaVersion = "1.1.0"

val confluentVersion = "4.1.0"

resolvers ++= Seq(
  "Confluent repo" at "http://packages.confluent.io/maven/",
  "Spring Plugins repo" at "http://repo.spring.io/plugins-release/"
)

libraryDependencies ++= Seq(
  "org.apache.kafka" % "connect-api" % kafkaVersion % "provided",
  "io.confluent" % "kafka-connect-hdfs" % confluentVersion % "provided",
  "org.apache.kafka" % "connect-runtime" % kafkaVersion % "test"
)

autoScalaLibrary := false
