package by.bsuir.springbootproject.services;

import by.bsuir.springbootproject.entities.Cart;
import by.bsuir.springbootproject.entities.PagingParams;
import by.bsuir.springbootproject.entities.Product;
import by.bsuir.springbootproject.entities.SearchCriteria;
import by.bsuir.springbootproject.exceptions.NoResourceFoundException;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

public interface ProductService extends BaseService<Product>{
    ModelAndView getCategoryProducts(String category, PagingParams params);
    ModelAndView getProductById(int id) throws NoResourceFoundException;
    ModelAndView findProducts(SearchCriteria searchCriteria);
    ModelAndView addProductToCart(int id, Cart cart) throws NoResourceFoundException;
    ModelAndView changeFilter(SearchCriteria searchCriteria, String categoryName, BigDecimal priceFrom, BigDecimal priceTo);
}
