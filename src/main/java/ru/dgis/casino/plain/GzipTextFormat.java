package ru.dgis.casino.plain;

import io.confluent.connect.hdfs.HdfsSinkConnectorConfig;
import io.confluent.connect.hdfs.storage.HdfsStorage;
import io.confluent.connect.hdfs.string.StringFileReader;
import io.confluent.connect.storage.format.Format;
import io.confluent.connect.storage.format.RecordWriter;
import io.confluent.connect.storage.format.RecordWriterProvider;
import io.confluent.connect.storage.format.SchemaFileReader;
import io.confluent.connect.storage.hive.HiveFactory;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipTextFormat implements Format<HdfsSinkConnectorConfig, Path> {

    public GzipTextFormat(HdfsStorage storage) { }

    @Override
    public RecordWriterProvider<HdfsSinkConnectorConfig> getRecordWriterProvider() {
        return new GzipTextRecordWriterProvider();
    }

    @Override
    public SchemaFileReader<HdfsSinkConnectorConfig, Path> getSchemaFileReader() {
        return new StringFileReader();
    }

    @Override
    public HiveFactory getHiveFactory() {
        throw new UnsupportedOperationException("Hive integration is not currently supported with GzipTextFormat format");
    }

    private static class GzipTextRecordWriterProvider implements RecordWriterProvider<HdfsSinkConnectorConfig> {
        private static final Logger log = LoggerFactory.getLogger(GzipTextRecordWriterProvider.class);

        @Override
        public String getExtension() {
            // kafka-connect-hdfs doesn't support multiple file extensions like .txt.gz
            // when searching for files during recovery process
            // that is why the extension used by this format is missing txt part
            return ".gz";
        }

        @Override
        public RecordWriter getRecordWriter(final HdfsSinkConnectorConfig conf, final String fileName) {
            try {
                return new RecordWriter() {
                    private final Path path = new Path(fileName);
                    /*workaround before https://github.com/confluentinc/kafka-connect-hdfs/pull/460 arrives*/
                    private final FileSystem fs = FileSystem.newInstance(path.toUri(), conf.getHadoopConfiguration());
                    private final OutputStream out = new GZIPOutputStream(fs.create(path));

                    @Override
                    public void write(SinkRecord sinkRecord) {
                        try {
                            out.write(((String) sinkRecord.value()).getBytes());
                            out.write("\n".getBytes());
                        } catch (IOException cause) {
                            throw new ConnectException(cause);
                        }
                    }

                    @Override
                    public void close() {
                        try {
                            out.close();
                        } catch (IOException e) {
                            throw new ConnectException(e);
                        } finally {
                            try {
                                fs.close();
                            } catch (IOException e) {
                                log.error("Unable to close FileSystem object for " + path + " due to error:", e);
                            }
                        }
                    }

                    @Override
                    public void commit() { }
                };
            } catch (IOException cause) {
                throw new ConnectException(cause);
            }
        }
    }
}
