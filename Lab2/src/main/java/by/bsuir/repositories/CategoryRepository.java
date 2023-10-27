package by.bsuir.repositories;

import by.bsuir.domain.Category;
import by.bsuir.domain.PagingParams;
import by.bsuir.exceptions.ConnectionException;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CategoryRepository {
    private final static ConnectionPool pool = ConnectionPool.getInstance();
    private final static String GET_CATEGORIES_QUERY = "SELECT * FROM categories LIMIT ?, ?";

    public List<Category> getCategories(PagingParams params) throws ConnectionException, SQLException {
        List<Category> result = new ArrayList<>();
        Connection connection = pool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_CATEGORIES_QUERY)) {
            int startPosition = params.getPageNumber() * params.getPageSize();
            statement.setInt(1, startPosition);
            statement.setInt(2, params.getPageSize());
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                result.add(Category.
                        builder().
                        id(set.getInt("id")).
                        name(set.getString("name")).
                        imagePath(set.getString("imagePath")).
                        build());
            }
            set.close();
        } finally {
            pool.returnConnection(connection);
        }
        return result;
    }
}
