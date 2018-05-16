package ru.dgis.casino.plain;

import io.confluent.connect.avro.AvroData;
import io.confluent.connect.hdfs.*;
import io.confluent.connect.hdfs.hive.HiveMetaStore;
import io.confluent.connect.hdfs.hive.HiveUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.sink.SinkRecord;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipTextFormat implements Format {
    @Override
    public RecordWriterProvider getRecordWriterProvider() {
        return new RecordWriterProvider() {
            @Override
            public String getExtension() {
                return ".txt.gz";
            }

            @Override
            public RecordWriter<SinkRecord> getRecordWriter(final Configuration conf, final String fileName, SinkRecord record, AvroData avroData) throws IOException {
                return new RecordWriter<SinkRecord>() {
                    private final Path path = new Path(fileName);
                    private final FSDataOutputStream hadoop = path.getFileSystem(conf).create(path);
                    private final OutputStream out = new GZIPOutputStream(hadoop);

                    @Override
                    public void write(SinkRecord sinkRecord) throws IOException {
                        out.write(((String) sinkRecord.value()).getBytes());
                        out.write("\n".getBytes());
                    }

                    @Override
                    public void close() throws IOException {
                        out.close();
                    }
                };
            }
        };
    }

    @Override
    public SchemaFileReader getSchemaFileReader(AvroData avroData) {
        return null;
    }

    @Override
    public HiveUtil getHiveUtil(HdfsSinkConnectorConfig hdfsSinkConnectorConfig, HiveMetaStore hiveMetaStore) {
        return null;
    }
}
