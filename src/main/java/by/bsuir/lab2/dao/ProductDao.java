package by.bsuir.lab2.dao;

import by.bsuir.lab2.domain.BaseProduct;
import by.bsuir.lab2.exceptions.EntityNotFoundException;
import jakarta.xml.bind.JAXBException;

import java.util.List;

/**
 * Represents dao layer with CRUD operations
 *
 * @see by.bsuir.lab2.dao.impl.ProductDaoImpl
 */
public interface ProductDao {

    /**
     * Create operation
     *
     * @param product product to be created
     * @throws JAXBException xml read/write exception
     */
    void create(BaseProduct product) throws JAXBException;

    /**
     * Read operation
     *
     * @return list of read products
     * @throws JAXBException xml read/write exception
     */
    List<BaseProduct> read() throws JAXBException;

    /**
     * Update operation
     *
     * @param product product to be updated
     * @return updated product
     * @throws EntityNotFoundException if product with passed id not found
     * @throws JAXBException           xml read/write exception
     */
    BaseProduct update(BaseProduct product) throws JAXBException, EntityNotFoundException;

    /**
     * Delete operation
     *
     * @param id id of product to be deleted
     * @throws EntityNotFoundException if product with passed id not found
     * @throws JAXBException           xml read/write exception
     */
    void delete(int id) throws JAXBException, EntityNotFoundException;

    /**
     * Find products by class
     *
     * @param c class of products to find
     * @return products of class c
     * @throws JAXBException xml read/write exception
     */
    List<BaseProduct> findByClass(Class<? extends BaseProduct> c) throws JAXBException;

    /**
     * @return cheapest product
     * @throws JAXBException xml read/write exception
     * @throws EntityNotFoundException thrown if there is no entities in storage at all
     */
    BaseProduct findCheapest() throws JAXBException, EntityNotFoundException;
}
