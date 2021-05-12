package ru.dgis.casino.smt;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public abstract class ToJsonStringMapper implements Transformation<SinkRecord> {
    @Override
    public SinkRecord apply(SinkRecord r) {
        return newRecord(r, asJsonString(operatingValue(r)));
    }

    private final ObjectMapper om = new ObjectMapper();

    private String asJsonString(Object obj) {
        if (obj == null) return null;

        String result;
        try {
            result = om.writeValueAsString(obj.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef();
    }

    @Override
    public void close() { }

    @Override
    public void configure(Map<String, ?> map) { }

    protected abstract Object operatingValue(SinkRecord record);

    protected abstract SinkRecord newRecord(SinkRecord record, Object updatedValue);

    public static class Key extends ToJsonStringMapper {
        @Override
        protected Object operatingValue(SinkRecord record) {
            return record.key();
        }

        @Override
        protected SinkRecord newRecord(SinkRecord record, Object updatedValue) {
            return record.newRecord(
                record.topic(),
                record.kafkaPartition(),
                Schema.STRING_SCHEMA,
                updatedValue,
                record.valueSchema(),
                record.value(),
                record.timestamp()
            );
        }
    }

    public static class Value extends ToJsonStringMapper {
        @Override
        protected Object operatingValue(SinkRecord record) {
            return record.value();
        }

        @Override
        protected SinkRecord newRecord(SinkRecord record, Object updatedValue) {
            return record.newRecord(
                record.topic(),
                record.kafkaPartition(),
                record.keySchema(),
                record.key(),
                Schema.STRING_SCHEMA,
                updatedValue,
                record.timestamp()
            );
        }
    }
}
