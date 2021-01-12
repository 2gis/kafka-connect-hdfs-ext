organization := "ru.dgis"

name := "kafka-connect-hdfs-ext"

val kafkaVersion = "2.3.0"

val confluentVersion = "5.2.3"

resolvers ++= Seq(
  "Confluent repo" at "https://packages.confluent.io/maven/",
  "Spring Plugins repo" at "https://repo.spring.io/plugins-release/"
)

libraryDependencies ++= Seq(
  "org.apache.kafka" % "connect-api" % kafkaVersion % "provided",
  "io.confluent" % "kafka-connect-hdfs" % confluentVersion % "provided"
)

autoScalaLibrary := false

releaseVersionBump := sbtrelease.Version.Bump.Minor

bintrayReleaseOnPublish in ThisBuild := true

bintrayOrganization := Some("2gis")

licenses += "MPL-2.0" -> url("https://www.mozilla.org/en-US/MPL/2.0/")

publishTo := {
  val maybeOriginalRepo = publishTo.value
  if (isSnapshot.value)
    Some("OSS JFrog Snapshots" at "https://oss.jfrog.org/artifactory/oss-snapshot-local")
  else maybeOriginalRepo
}

credentials += Credentials(
  "Artifactory Realm",
  "oss.jfrog.org",
  Option(System.getenv("OSS_JFROG_USER")).getOrElse(""),
  Option(System.getenv("OSS_JFROG_PASSWORD")).getOrElse("")
)

crossPaths := false
