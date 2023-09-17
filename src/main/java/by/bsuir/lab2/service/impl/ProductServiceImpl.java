package by.bsuir.lab2.service.impl;

import by.bsuir.lab2.dao.ProductDao;
import by.bsuir.lab2.dao.impl.ProductDaoImpl;
import by.bsuir.lab2.domain.BaseProduct;
import by.bsuir.lab2.exceptions.EntityNotFoundException;
import by.bsuir.lab2.service.ProductService;
import jakarta.xml.bind.JAXBException;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductService implementation
 *
 * @see ProductService
 */
public class ProductServiceImpl implements ProductService {

    /**
     * DAO to access objects in storage
     */
    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public void create(BaseProduct product) {
        try {
            productDao.create(product);
            System.out.println("Product was created");
        } catch (JAXBException e) {
            System.out.println("Product wasn't created");
        }
    }

    @Override
    public List<BaseProduct> read() {
        try {
            return productDao.read();
        } catch (JAXBException e) {
            System.out.println("Unable to read products");
            return new ArrayList<>();
        }
    }

    @Override
    public BaseProduct update(BaseProduct product) {
        try {
            productDao.update(product);
            System.out.println("Product was updated");
        } catch (JAXBException e) {
            System.out.println("Product wasn't updated");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void delete(int id) {
        try {
            productDao.delete(id);
            System.out.println("Product was deleted");
        } catch (JAXBException e) {
            System.out.println("Product wasn't deleted");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<BaseProduct> findByClass(Class<? extends BaseProduct> c) {
        try {
            return productDao.findByClass(c);
        } catch (JAXBException e) {
            System.out.println("Products were not found");
            return new ArrayList<>();
        }
    }

    @Override
    public BaseProduct findCheapest() {
        try {
            return productDao.findCheapest();
        } catch (JAXBException e) {
            System.out.println("Product wasn't found");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
