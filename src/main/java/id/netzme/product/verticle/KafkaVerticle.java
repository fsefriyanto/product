package id.netzme.product.verticle;

import id.netzme.product.constant.KafkaTopic;
import id.netzme.product.helper.CommonHelper;
import id.netzme.product.transformer.SimpleValueTransformer;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.RecordMetadata;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.kafka.client.consumer.KafkaConsumer;
import io.vertx.reactivex.kafka.client.producer.KafkaProducer;
import io.vertx.reactivex.kafka.client.producer.KafkaProducerRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class KafkaVerticle extends AbstractVerticle {

  private KafkaProducer<String, String> producer;
  private KafkaConsumer<String, String> consumer;
  private KafkaConsumer<String, String> consumerStream;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    super.start(startFuture);

    CommonHelper.getConfigRetriever(vertx).rxGetConfig()
      .doOnSuccess(config -> {
        createProducer(vertx, config);
        createConsumer(vertx);
        createConsumerStream(vertx);
        publishMessage(producer);
        subscribeMessage(consumer);
        startKafkaStream();
        subscribeTopicUpperMessage(consumerStream);
      })
      .ignoreElement()
      .subscribe(CompletableHelper.toObserver(startFuture));
  }

  public void createProducer(Vertx vertx, JsonObject jsonObject) {
    Map<String, String> config = new HashMap<>();
    config.put("bootstrap.servers", jsonObject.getString("kafka.url"));
    config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    config.put("acks", "1");

    producer = KafkaProducer.create(vertx, config);
  }

  public void publishMessage(KafkaProducer<String, String> producer) {
    for (int i = 0; i < 5; i++) {
      KafkaProducerRecord<String, String> record =
        KafkaProducerRecord.create(KafkaTopic.ORDER_PENDING, UUID.randomUUID().toString(), "message_" + i);

      producer.send(record, done -> {
        if (done.succeeded()) {

          RecordMetadata recordMetadata = done.result();
          System.out.println("Message " + record.value() + " written on topic=" + recordMetadata.getTopic() +
            ", partition=" + recordMetadata.getPartition() +
            ", offset=" + recordMetadata.getOffset());
        }
      });
    }
  }

  public void createConsumer(Vertx vertx) {
    Map config = new HashMap();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "order");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

    consumer = KafkaConsumer.create(vertx, config);
  }

  public void createConsumerStream(Vertx vertx) {

    Map config = new HashMap();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "order-pending-stream");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

    consumerStream = KafkaConsumer.create(vertx, config);
  }

  public void subscribeMessage(KafkaConsumer<String, String> consumer) {
    consumer.handler(record -> {
      System.out.println(" Subscribe Message with record : " + record.value());
    });

    consumer.subscribe(KafkaTopic.ORDER_PENDING);
  }

  public void subscribeTopicUpperMessage(KafkaConsumer<String, String> consumer) {
    consumer.handler(record -> {
      System.out.println(" Subscribe Message from topic-upper with record : " + record.value());
    });
    consumer.subscribe(KafkaTopic.UPPER);
  }

  public void startKafkaStream() {

    final Properties properties = new Properties();
    properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "stream-upper");
    properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

    final String storeName = "stateStore";
    final StreamsBuilder builder = new StreamsBuilder();

    final StoreBuilder<KeyValueStore<String, String>> storeBuilder = Stores.keyValueStoreBuilder(
      Stores.persistentKeyValueStore(storeName),
      Serdes.String(),
      Serdes.String());

    builder.addStateStore(storeBuilder);

    builder.<String, String>stream(KafkaTopic.ORDER_PENDING)
      .transformValues(() -> new SimpleValueTransformer(storeName), storeName)
      .to(KafkaTopic.UPPER);

    final Topology topology = builder.build(properties);

    final KafkaStreams streams = new KafkaStreams(topology, properties);
    streams.start();
  }
}
