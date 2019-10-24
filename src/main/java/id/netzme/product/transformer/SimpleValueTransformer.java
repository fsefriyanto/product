package id.netzme.product.transformer;

import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class SimpleValueTransformer implements ValueTransformerWithKey<String, String, String> {

  private String storeName;
  private KeyValueStore<String, String> store;

  public SimpleValueTransformer(String storeName) {
    this.storeName = storeName;
  }

  @Override
  public void init(final ProcessorContext context) {
    store = (KeyValueStore) context.getStateStore(storeName);
  }

  @Override
  public String transform(String key, final String value) {
    String persistedValue = store.get(key);
    final String updatedValue = value.toUpperCase();


    if (persistedValue == null) {
      persistedValue = updatedValue;
    }

    store.put(key, updatedValue);
    return persistedValue;
  }

  @Override
  public void close() {}
}
