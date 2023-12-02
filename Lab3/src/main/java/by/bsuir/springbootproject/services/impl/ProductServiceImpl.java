package by.bsuir.springbootproject.services.impl;

import by.bsuir.springbootproject.constants.PagesPaths;
import by.bsuir.springbootproject.constants.RequestAttributesNames;
import by.bsuir.springbootproject.constants.Values;
import by.bsuir.springbootproject.entities.Cart;
import by.bsuir.springbootproject.entities.PagingParams;
import by.bsuir.springbootproject.entities.Product;
import by.bsuir.springbootproject.entities.SearchCriteria;
import by.bsuir.springbootproject.exceptions.NoResourceFoundException;
import by.bsuir.springbootproject.repositories.CategoryRepository;
import by.bsuir.springbootproject.repositories.ProductRepository;
import by.bsuir.springbootproject.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ModelAndView getCategoryProducts(String category, PagingParams params) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.CATEGORY_PAGE);
        List<Product> products = productRepository.getCategoryProducts(category, params);
        modelAndView.addObject(RequestAttributesNames.CATEGORY_PRODUCTS, products);
        modelAndView.addObject(RequestAttributesNames.CATEGORY_NAME, category);
        return modelAndView;
    }

    @Override
    public ModelAndView getProductById(int id) throws NoResourceFoundException {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.PRODUCT_PAGE);
        Product product = productRepository.getProductById(id).
                orElseThrow(() -> new NoResourceFoundException("Product with id " + id + " not found"));
        modelAndView.addObject(product.getName());
        modelAndView.addObject(product);
        return modelAndView;
    }

    @Override
    public ModelAndView findProducts(SearchCriteria searchCriteria) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.SEARCH_PAGE);
        if (searchCriteria.getPageNumber() < 0) {
            searchCriteria.setPageNumber(Values.DEFAULT_START_PAGE);
        }
        modelAndView.addObject(RequestAttributesNames.PRODUCTS, productRepository.findProducts(searchCriteria));
        modelAndView.addObject(RequestAttributesNames.CATEGORIES, categoryRepository.read(new PagingParams(1, 1000_000_000)));
        return modelAndView;
    }

    @Override
    public ModelAndView addProductToCart(int id, Cart cart) throws NoResourceFoundException {
        ModelAndView modelAndView = new ModelAndView("redirect:/products/" + id);
        Product product = productRepository.getProductById(id).
                orElseThrow(() -> new NoResourceFoundException("No product with id " + id + " found"));
        cart.addProduct(product);
        return modelAndView;
    }

    @Override
    public ModelAndView changeFilter(SearchCriteria searchCriteria, String categoryName, BigDecimal priceFrom, BigDecimal priceTo) {
        searchCriteria.setKeyWords("");
        searchCriteria.setSearchCategory(categoryName);
        searchCriteria.setPriceFrom(priceFrom);
        searchCriteria.setPriceTo(priceTo);
        searchCriteria.setPageNumber(Values.DEFAULT_START_PAGE);
        return findProducts(searchCriteria);
    }

    @Override
    @Transactional
    public ModelAndView create(Product product) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.PRODUCT_PAGE);
        productRepository.create(product);
        return modelAndView;
    }

    @Override
    public ModelAndView read(PagingParams params) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.SEARCH_PAGE);
        modelAndView.addObject(RequestAttributesNames.PRODUCTS, productRepository.read(params));
        modelAndView.addObject(RequestAttributesNames.CATEGORIES, categoryRepository.read(new PagingParams(1, 1000_000_000)));
        return modelAndView;
    }

    @Override
    @Transactional
    public Product update(Product product) throws NoResourceFoundException {
        productRepository.getProductById(product.getId()).orElseThrow(() ->
                new NoResourceFoundException("No product with id " + product.getId() + " found"));
        return productRepository.create(product);
    }

    @Override
    @Transactional
    public void delete(int id) {
        productRepository.delete(id);
    }
}
