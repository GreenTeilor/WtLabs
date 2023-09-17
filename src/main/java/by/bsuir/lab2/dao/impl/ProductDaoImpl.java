package by.bsuir.lab2.dao.impl;

import by.bsuir.lab2.dao.ProductDao;
import by.bsuir.lab2.domain.BaseProduct;
import by.bsuir.lab2.domain.Products;
import by.bsuir.lab2.exceptions.EntityNotFoundException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ProductDao implementation
 *
 * @see by.bsuir.lab2.dao.ProductDao
 */
public class ProductDaoImpl implements ProductDao {

    /**
     * Storage file
     */
    private static final File file = new File("src\\main\\java\\by\\bsuir\\lab2\\test.xml");

    /**
     * JAXBContext for marshalling/unmarshalling
     */
    private static final JAXBContext context;

    static {
        try {
            context = JAXBContext.newInstance(Products.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed obtain a new instance of a JAXBContext class");
        }
    }

    @Override
    public void create(BaseProduct product) throws JAXBException {
        List<BaseProduct> productList = read();
        int newId = productList.stream().mapToInt(BaseProduct::getId).max().orElse(0) + 1;
        product.setId(newId);
        productList.add(product);
        Products products = new Products(productList);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(products, file);
    }

    @Override
    public List<BaseProduct> read() throws JAXBException {
        try {
            if (file.length() == 0) {
                return new ArrayList<>();
            }
            List<BaseProduct> products = ((Products) context.createUnmarshaller()
                    .unmarshal(new FileReader(file))).getProducts();
            return products == null ? new ArrayList<>() : products;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public BaseProduct update(BaseProduct product) throws JAXBException, EntityNotFoundException {
        List<BaseProduct> productList = read();
        int index = IntStream.range(0, productList.size())
                .filter(i -> product.getId() == productList.get(i).getId())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + product.getId() + " not found"));
        productList.set(index, product);
        Products products = new Products(productList);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(products, file);
        return product;
    }

    @Override
    public void delete(int id) throws JAXBException, EntityNotFoundException {
        List<BaseProduct> productList = read();
        int index = IntStream.range(0, productList.size())
                .filter(i -> id == productList.get(i).getId())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        productList.remove(index);
        Products products = new Products(productList);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(products, file);
    }

    @Override
    public List<BaseProduct> findByClass(Class<? extends BaseProduct> c) throws JAXBException {
        List<BaseProduct> productList = read();
        return productList.stream().filter(c::isInstance).collect(Collectors.toList());
    }

    @Override
    public BaseProduct findCheapest() throws JAXBException, EntityNotFoundException {
        List<BaseProduct> productList = read();
        return productList.stream().min(Comparator.comparing(BaseProduct::getPrice)).orElseThrow(() ->
                new EntityNotFoundException("No product found"));
    }
}
