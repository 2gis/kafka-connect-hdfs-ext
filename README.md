[ ![Download](https://api.bintray.com/packages/2gis/maven/kafka-connect-hdfs-ext/images/download.svg) ](https://bintray.com/2gis/maven/kafka-connect-hdfs-ext/_latestVersion)

# kafka-connect-hdfs-ext

This project provides extensions for [kafka-connect-hdfs](https://github.com/confluentinc/kafka-connect-hdfs) project.

# Formats

The project provides an additional HDFS connector format: `ru.dgis.casino.plain.GzipTextFormat`.

`GzipTextFormat` writes each record value as `String` separated with `\n` 
and also performs compression via `GZIPOutputStream`.

# How to use

Here is an example of a connector config:
```
connector.class=io.confluent.connect.hdfs.HdfsSinkConnector
format.class=ru.dgis.casino.plain.GzipTextFormat
partitioner.class=io.confluent.connect.hdfs.partitioner.TimeBasedPartitioner
path.format=YYYY/MM/dd
name=SOME_NAME_HERE
topics=TOPIC
hdfs.url=hdfs://YOUR_HADOOP
logs.dir=LOGS_DIR
topics.dir=TOPICS_DIR

flush.size=100000
locale=ru_RU
timezone=Asia/Novosibirsk
```

