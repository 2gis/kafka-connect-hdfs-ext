import sbt._

lazy val kasha = (project in file(".")).settings(
  name := "kafka-connect-plaintext",
  organization := "ru.dgis.casino"
)

casino.Assembly.settings

casino.Release.librarySettings

val kafkaV = "0.10.0.0-cp1"

val confluentV = "3.0.0"

resolvers += "Confluent repo" at "http://packages.confluent.io/maven/"

resolvers += "Spring repo" at "http://repo.spring.io/libs-milestone/"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "connect-runtime" % kafkaV,
  "org.apache.kafka" % "connect-api" % kafkaV,
  "io.confluent" % "kafka-connect-hdfs" % confluentV,
  "org.apache.hadoop" % "hadoop-client" % "2.6.0" excludeAll(
    ExclusionRule("commons-beanutils", "commons-beanutils"),
    ExclusionRule("asm", "asm"),
    ExclusionRule("javax.servlet", "servlet-api"),
    ExclusionRule("org.slf4j", "slf4j-log4j12"),
    ExclusionRule("org.apache.httpcomponents", "httpclient")
  )
)
