package id.netzme.product.handler;

import id.netzme.product.entity.Product;
import id.netzme.product.service.ProductService;
import id.netzme.product.service.ProductServiceImpl;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.CorsHandler;
import org.davidmoten.rx.jdbc.Database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProductHandler implements Handler<RoutingContext> {

  private ProductService productService;
  private Router router;

  public ProductHandler(Database db, Router router) {
    this.productService = new ProductServiceImpl(db);
    this.router = router;
    buildProductRouter(router);
    enableCorsSupport(router);
  }

  private void buildProductRouter(Router router) {
    router.get("/api/v1/products").handler(this::getAllProduct);
    router.get("/api/v1/products/:id").handler(this::getProductById);
    router.delete("/api/v1/products/:id").handler(this::deleteProductById);
    router.post("/api/v1/products").handler(this::addProduct);
    router.put("/api/v1/products/:id").handler(this::updateProduct);
  }

  private void enableCorsSupport(Router router) {
    Set<String> allowHeaders = new HashSet<>();
    allowHeaders.add("x-requested-with");
    allowHeaders.add("Access-Control-Allow-Origin");
    allowHeaders.add("origin");
    allowHeaders.add("Content-Type");
    allowHeaders.add("accept");
    Set<HttpMethod> allowMethods = new HashSet<>();
    allowMethods.add(HttpMethod.GET);
    allowMethods.add(HttpMethod.PUT);
    allowMethods.add(HttpMethod.OPTIONS);
    allowMethods.add(HttpMethod.POST);
    allowMethods.add(HttpMethod.DELETE);
    allowMethods.add(HttpMethod.PATCH);

    router.route().handler(CorsHandler.create("*")
      .allowedHeaders(allowHeaders)
      .allowedMethods(allowMethods));
  }

  private void getAllProduct(RoutingContext rc) {
    productService.findAllProduct().subscribe(product -> {
      buildResponse(rc, Json.encodePrettily(product));
    });
  }

  private void getProductById(RoutingContext rc) {
    String id = rc.request().getParam("id");
    productService.findProductById(Long.valueOf(id))
      .subscribe(product -> {
        buildResponse(rc, Json.encodePrettily(product));
      }, err -> {buildNotFoundResponse(rc);});
  }

  private void buildResponse(RoutingContext rc, String s) {
    rc.response().setStatusCode(200)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(s);
  }

  private void buildNotFoundResponse(RoutingContext rc) {
    Map response = new HashMap<>();
    response.put("code", "DATA_NOT_FOUND");
    response.put("message", "Data not found");

    rc.response()
      .setStatusCode(404)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(response));
  }

  private void deleteProductById(RoutingContext rc) {
    String id = rc.request().getParam("id");
    productService.delete(Long.valueOf(id)).subscribe(() -> {
      rc.response().setStatusCode(204)
        .putHeader("content-type", "application/json; charset=utf-8")
        .end();
    });
  }

  private void addProduct(RoutingContext rc) {
    JsonObject jsonObject = rc.getBodyAsJson();
    Product product = new Product(jsonObject);
    productService.addProduct(product).subscribe(() -> {
      buildResponse(rc, jsonObject.encodePrettily());
    });
  }

  private void updateProduct(RoutingContext rc) {
    String id = rc.request().getParam("id");
    JsonObject jsonObject = rc.getBodyAsJson();
    Product product = new Product(jsonObject.getLong("id"), jsonObject.getString("name"));
    productService.editProduct(product).subscribe(() -> {
      buildResponse(rc, jsonObject.encodePrettily());
    });
  }

  @Override
  public void handle(RoutingContext routingContext) {
    router.handleContext(routingContext);
  }
}
