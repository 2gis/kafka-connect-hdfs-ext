package ru.dgis.casino.smt;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.Map;

public class ArchiveJsonString implements Transformation<SinkRecord> {
  @Override
  public SinkRecord apply(SinkRecord r) {
    return new SinkRecord(
            r.topic(),
            r.kafkaPartition(),
            null,
            null,
            Schema.STRING_SCHEMA,
            formatAsCompactJsonString(r),
            r.kafkaOffset(),
            r.timestamp(),
            r.timestampType(),
            r.headers()
    );
  }

  private String formatAsCompactJsonString(SinkRecord r) {
    return "{" +
            "\"key\":" + formatJsonValue(r.key()) + "," +
            "\"value\":" + formatJsonValue(r.value()) + "," +
            "\"topic\":\"" + formatJsonValue(r.topic()) + "\"," +
            "\"partition\":" + formatJsonValue(r.kafkaPartition()) + "," +
            "\"offset\":" + r.kafkaOffset() + "," +
            "\"timestamp\":" + formatJsonValue(r.timestamp()) +
            "}";
  }

  private String formatJsonValue(Object value) {
    return value != null ? value.toString() : "null";
  }

  @Override
  public ConfigDef config() {
    return new ConfigDef();
  }

  @Override
  public void close() { }

  @Override
  public void configure(Map<String, ?> map) { }
}
