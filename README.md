# kafka-connect-hdfs-ext

This project provides extensions for [kafka-connect-hdfs](https://github.com/confluentinc/kafka-connect-hdfs) project.

# Formats

Right now we provides two additional formats: `ru.dgis.casino.plain.GzipTextFormat` and `ru.dgis.casino.plain.PlainTextFormat`.

`PlainTextFormat` is format that saves each message to hdfs as string and separates messages with `\n`. 
`PlainTextFormat` drops key value.

`GzipTextFormat` is the same format as `PlainTextFormat` but also performs compression via `gzip`.

# How to use

Here it is an example of topic config
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

We will publish next release to bintray soon.
