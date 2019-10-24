package id.netzme.product.service;

import id.netzme.product.entity.Product;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;

public interface ProductService {
  Completable delete(Long id);
  Single<Product> findProductById(Long id);
  Single<List<Product>> findAllProduct();
  Completable addProduct(Product product);
  Completable editProduct(Product product);
}
