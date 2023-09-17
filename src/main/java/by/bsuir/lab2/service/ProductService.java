package by.bsuir.lab2.service;

import by.bsuir.lab2.domain.BaseProduct;

import java.util.List;

/**
 * Provides API for business-operations with products
 *
 * @see by.bsuir.lab2.service.impl.ProductServiceImpl
 */
public interface ProductService {

    /**
     * Create product
     *
     * @param product product to be created
     */
    void create(BaseProduct product);

    /**
     * Read products
     *
     * @return list of read products
     */
    List<BaseProduct> read();

    /**
     * Update product
     *
     * @param product product to be updated
     * @return updated product
     */
    BaseProduct update(BaseProduct product);

    /**
     * Delete product
     *
     * @param id id of product to be deleted
     */
    void delete(int id);

    /**
     * Find products by class
     *
     * @param c class of products to find
     * @return products of class c
     */
    List<BaseProduct> findByClass(Class<? extends BaseProduct> c);

    /**
     * @return cheapest product
     */
    BaseProduct findCheapest();
}
