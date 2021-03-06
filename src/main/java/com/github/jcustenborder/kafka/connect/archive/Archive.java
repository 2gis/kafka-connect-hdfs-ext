/**
 * Copyright © 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jcustenborder.kafka.connect.archive;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapted implementation for HDFS Sink Connector
 * that produces immutable schema suitable for comparison in TopicPartitionWriter.
 *
 * Original source code at https://github.com/jcustenborder/kafka-connect-transform-archive
 */
public class Archive implements Transformation<SinkRecord> {
  @Override
  public SinkRecord apply(SinkRecord r) {
    if (r.valueSchema() == null) {
      return applySchemaless(r);
    } else {
      return applyWithSchema(r);
    }
  }

  private SinkRecord applyWithSchema(SinkRecord r) {
    final Schema schema = SchemaBuilder.struct()
        .name("com.github.jcustenborder.kafka.connect.archive.Storage")
        .field("key", r.keySchema())
        .field("value", r.valueSchema())
        .field("topic", Schema.STRING_SCHEMA)
        .field("partition", Schema.INT32_SCHEMA)
        .field("offset", Schema.INT64_SCHEMA)
        .field("timestamp", Schema.INT64_SCHEMA)
        .build();
    Struct value = new Struct(schema)
        .put("key", r.key())
        .put("value", r.value())
        .put("topic", r.topic())
        .put("partition", r.kafkaPartition())
        .put("offset", r.kafkaOffset())
        .put("timestamp", r.timestamp());
    return new SinkRecord(r.topic(), r.kafkaPartition(), null, null, schema, value, r.kafkaOffset(), r.timestamp(), r.timestampType(), r.headers());
  }

  @SuppressWarnings("unchecked")
  private SinkRecord applySchemaless(SinkRecord r) {

    final Map<String, Object> archiveValue = new HashMap<>();

    final Map<String, Object> value = (Map<String, Object>) r.value();

    archiveValue.put("key", r.key());
    archiveValue.put("value", value);
    archiveValue.put("topic", r.topic());
    archiveValue.put("partition", r.kafkaPartition());
    archiveValue.put("offset", r.kafkaOffset());
    archiveValue.put("timestamp", r.timestamp());

    return new SinkRecord(r.topic(), r.kafkaPartition(), null, null, null, value, r.kafkaOffset(), r.timestamp(), r.timestampType(), r.headers());
  }

  @Override
  public ConfigDef config() {
    return new ConfigDef();
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> map) {

  }
}
