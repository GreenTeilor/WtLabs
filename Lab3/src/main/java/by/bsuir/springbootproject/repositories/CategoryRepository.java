package by.bsuir.springbootproject.repositories;

import by.bsuir.springbootproject.entities.Category;
import by.bsuir.springbootproject.entities.PagingParams;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category create(Category entity);

    List<Category> read(PagingParams params);

    Category update(Category entity);

    void delete(int id);

    Optional<Category> findById(int id);
    Optional<Category> findByName(String name);
}