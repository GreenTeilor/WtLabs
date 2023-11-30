package by.bsuir.springbootproject.repositories;

import by.bsuir.springbootproject.entities.PagingParams;
import by.bsuir.springbootproject.entities.Product;
import by.bsuir.springbootproject.entities.SearchCriteria;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product create(Product entity);

    List<Product> read(PagingParams params);

    Product update(Product entity);

    void delete(int id);
    List<Product> getCategoryProducts(String category, PagingParams params);

    Optional<Product> getProductById(int id);

    List<Product> findProducts(SearchCriteria criteria);
}
