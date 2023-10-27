package by.bsuir.services;

import by.bsuir.constants.ExceptionMessages;
import by.bsuir.constants.PagesPaths;
import by.bsuir.constants.RequestAttributesNames;
import by.bsuir.domain.Product;
import by.bsuir.exceptions.ConnectionException;
import by.bsuir.exceptions.DbException;
import by.bsuir.exceptions.ServiceException;
import by.bsuir.repositories.ProductRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@NoArgsConstructor
public class ProductService {
    ProductRepository productRepository = new ProductRepository();

    public void getProduct(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet,
                                    String id) throws DbException, ServiceException {
        try {
            RequestDispatcher requestDispatcher = servlet.getServletContext().getRequestDispatcher(PagesPaths.PREFIX +
                    PagesPaths.PRODUCT_PAGE + PagesPaths.POSTFIX);
            if (!id.matches("\\d+")) {
                throw new NumberFormatException("id is not a number");
            }
            Optional<Product> product = productRepository.getProductById(Integer.parseInt(id));
            request.setAttribute(RequestAttributesNames.PRODUCT, product.orElseThrow(() -> new IOException("")));
            product.ifPresent(p -> request.setAttribute(RequestAttributesNames.PRODUCT, p));
            requestDispatcher.forward(request, response);
        } catch (ConnectionException | SQLException e) {
            throw new DbException(ExceptionMessages.DB_EXCEPTION);
        } catch (ServletException | IOException | NumberFormatException e) {
            throw new ServiceException(ExceptionMessages.SERVICE_EXCEPTION);
        }
    }
}
