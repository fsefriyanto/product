package id.netzme.product.entity;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
public class Product {

  private Long id;
  private String name;

  public Product(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Product(JsonObject json) {
    ProductConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ProductConverter.toJson(this, json);
    return json;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
