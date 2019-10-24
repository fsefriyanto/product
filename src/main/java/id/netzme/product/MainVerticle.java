package id.netzme.product;

import id.netzme.product.handler.ProductHandler;
import id.netzme.product.helper.CommonHelper;
import id.netzme.product.verticle.KafkaVerticle;
import io.reactivex.Completable;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import org.davidmoten.rx.jdbc.Database;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    CommonHelper.getConfigRetriever(vertx).rxGetConfig()
      .doOnSuccess(config -> {
        Database db = Database.from(config.getString("db.url"), 3);
        new ProductHandler(db, router);
      })
      .flatMapCompletable(config -> createHttpServer(config, router))
      .subscribe(CompletableHelper.toObserver(startFuture));
  }

  private Completable createHttpServer(JsonObject config, Router router) {
    return vertx
      .createHttpServer()
      .requestHandler(router)
      .rxListen(config.getInteger("http_port", 8080))
      .ignoreElement();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(KafkaVerticle.class.getName());
    vertx.deployVerticle(MainVerticle.class.getName());
  }
}
