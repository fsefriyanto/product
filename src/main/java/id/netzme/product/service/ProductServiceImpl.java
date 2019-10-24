package id.netzme.product.service;

import id.netzme.product.entity.Product;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.davidmoten.rx.jdbc.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductServiceImpl implements ProductService {

  private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

  private Database db;

  public ProductServiceImpl() {}

  public ProductServiceImpl(Database db) {
    this.db = db;
  }

  @Override
  public Completable delete(Long id) {
    return db.update("delete from product where id = :id")
      .parameter("id", id)
      .complete();
  }

  @Override
  public Single<Product> findProductById(Long id) {
    return db.select("select id, name from product where id = :id")
      .parameter("id", id)
      .get(rs -> {
        return new Product(rs.getLong("id"), rs.getString("name"));
      }).firstOrError();
  }

  @Override
  public Single<List<Product>> findAllProduct() {
    return db.select("select id, name from product").get(rs -> {
      return new Product(rs.getLong("id"), rs.getString("name"));
    }).toList();
  }

  @Override
  public Completable addProduct(Product product) {
    return db.update("insert into public.product(id, name) values (nextval('product_seq'), :name)")
      .parameter("name", product.getName())
      .complete()
      .doOnError(e -> log.error(e.getMessage()));
  }

  @Override
  public Completable editProduct(Product product) {
    return db.update("update product set name= :name where id= :id")
      .parameter("name", product.getName())
      .parameter("id", product.getId())
      .complete();
  }
}
