package by.bsuir.springbootproject.repositories;

import by.bsuir.springbootproject.entities.Order;
import by.bsuir.springbootproject.entities.PagingParams;

import java.util.List;

public interface OrderRepository {
    Order create(Order entity);

    List<Order> read();

    Order update(Order entity);

    void delete(int id);

    List<Order> findAllByUserId(int userId, PagingParams params);
}
