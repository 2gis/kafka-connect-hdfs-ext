import xerial.sbt.Sonatype._

licenses := Seq("MPL-2.0" -> url("https://www.mozilla.org/en-US/MPL/2.0/"))

sonatypeProjectHosting := Some(GitHubHosting("2gis", "kafka-connect-hdfs-ext", "s.savulchik@2gis.ru"))

publishTo := sonatypePublishToBundle.value

publishMavenStyle := true

sonatypeCredentialHost := "s01.oss.sonatype.org"

// to have artifact name kafka-connect-hdfs-ext instead of kafka-connect-hdfs-ext_2.12
crossPaths := false

