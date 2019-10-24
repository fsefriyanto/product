package id.netzme.product.service;

import id.netzme.product.entity.Product;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.SelectBuilder;
import org.davidmoten.rx.jdbc.UpdateBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class ProductServiceTest {

  @InjectMocks
  private ProductServiceImpl productService;

  @Mock
  private SelectBuilder selectBuilder;

  @Mock
  private UpdateBuilder updateBuilder;

  @Mock
  private Database db;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @AfterEach
  public void tearDown() throws Exception {
    Mockito.verifyNoMoreInteractions(selectBuilder, updateBuilder, db);
  }

  @Test
  public void addProduct() {
    Product product = new Product(0L, "new product");

    Mockito.when(db.update(anyString())).thenReturn(updateBuilder);
    Mockito.when(updateBuilder.parameter("name", "new product")).thenReturn(updateBuilder);
    Mockito.when(updateBuilder.complete()).thenReturn(Completable.complete());

    productService.addProduct(product).test()
      .assertComplete();

    Mockito.verify(db).update(anyString());
    Mockito.verify(updateBuilder).parameter("name", "new product");
    Mockito.verify(updateBuilder).complete();
  }

  @Test
  public void editProduct() {
    Product product = new Product(1L, "edit product");

    Mockito.when(db.update(anyString())).thenReturn(updateBuilder);
    Mockito.when(updateBuilder.parameter("name", "edit product")).thenReturn(updateBuilder);
    Mockito.when(updateBuilder.parameter("id", 1L)).thenReturn(updateBuilder);
    Mockito.when(updateBuilder.complete()).thenReturn(Completable.complete());

    productService.editProduct(product).test()
      .assertComplete();

    Mockito.verify(db).update(anyString());
    Mockito.verify(updateBuilder).parameter("name", "edit product");
    Mockito.verify(updateBuilder).parameter("id", 1L);
    Mockito.verify(updateBuilder).complete();
  }

  @Test
  public void getAllProduct() {
    Product product = new Product(1L, "product-1");

    Mockito.when(db.select("select id, name from product")).thenReturn(selectBuilder);
    Mockito.when(selectBuilder.get(any())).thenReturn(Flowable.just(product));

    productService.findAllProduct().test()
      .assertValue(Collections.singletonList(product))
      .assertComplete();

    Mockito.verify(db).select("select id, name from product");
    Mockito.verify(selectBuilder).get(any());
  }

  @Test
  public void getProductById() {
    Product product = new Product(1L, "product-1");

    Mockito.when(db.select("select id, name from product where id = :id")).thenReturn(selectBuilder);
    Mockito.when(selectBuilder.parameter("id", 1L)).thenReturn(selectBuilder);
    Mockito.when(selectBuilder.get(any())).thenReturn(Flowable.just(product));

    productService.findProductById(1L).test()
      .assertValue(product)
      .assertComplete();

    Mockito.verify(db).select("select id, name from product where id = :id");
    Mockito.verify(selectBuilder).parameter("id", 1L);
    Mockito.verify(selectBuilder).get(any());
  }

  @Test
  public void deleteProduct() {
    Mockito.when(db.update(anyString())).thenReturn(updateBuilder);
    Mockito.when(updateBuilder.parameter("id", 1L)).thenReturn(updateBuilder);
    Mockito.when(updateBuilder.complete()).thenReturn(Completable.complete());

    productService.delete(1L).test().assertComplete();

    Mockito.verify(db).update(anyString());
    Mockito.verify(updateBuilder).parameter("id", 1L);
    Mockito.verify(updateBuilder).complete();
  }
}
