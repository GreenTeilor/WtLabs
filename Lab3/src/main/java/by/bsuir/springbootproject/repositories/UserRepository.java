package by.bsuir.springbootproject.repositories;

import by.bsuir.springbootproject.entities.PagingParams;
import by.bsuir.springbootproject.entities.User;
import by.bsuir.springbootproject.exceptions.UserAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User entity);

    List<User> read(PagingParams params);

    User update(User entity);

    void delete(int id);
    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(int id);

    User getUser(String email, String password);

    void updateAddressAndPhoneNumber(String address, String phoneNumber, String email);

    String getUserFavoriteCategory(int id);
    int getUserDaysRegistered(int id);
    int getUserPurchasedBooksCount(int id);
    int getUserOrdersCount(int id);
}
