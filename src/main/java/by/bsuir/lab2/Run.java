package by.bsuir.lab2;

import by.bsuir.lab2.domain.BaseProduct;
import by.bsuir.lab2.domain.Fridge;
import by.bsuir.lab2.domain.Teapot;
import by.bsuir.lab2.domain.Tv;
import by.bsuir.lab2.exceptions.UnsupportedOptionException;
import by.bsuir.lab2.input.InputHandler;
import by.bsuir.lab2.input.impl.FridgeInputHandler;
import by.bsuir.lab2.input.impl.TeapotInputHandler;
import by.bsuir.lab2.input.impl.TvInputHandler;
import by.bsuir.lab2.service.ProductService;
import by.bsuir.lab2.service.impl.ProductServiceImpl;

import java.util.Scanner;

/**
 * Main application class
 */
public class Run {

    /**
     * Allows user to input any type of product
     *
     * @return inputted product
     */
    private static BaseProduct inputAnyProduct() throws UnsupportedOptionException {
        Scanner scanner = new Scanner(System.in);
        int productOption;
        String chooseProduct = """
                                            
                Choose product type:
                    1) Teapot
                    2) Fridge
                    3) Tv
                    Input:
                """;
        System.out.println(chooseProduct);
        productOption = scanner.nextInt();
        InputHandler<?> inputHandler;
        switch (productOption) {
            case 1 -> inputHandler = new TeapotInputHandler();
            case 2 -> inputHandler = new FridgeInputHandler();
            case 3 -> inputHandler = new TvInputHandler();
            default -> throw new UnsupportedOptionException("There is no such option");
        }
        return inputHandler.input();
    }

    /**
     * Allows user to input product class
     *
     * @return inputted product class
     */
    private static Class<? extends BaseProduct> inputClass() throws UnsupportedOptionException {
        Scanner scanner = new Scanner(System.in);
        String chooseClass = """
                                                                       
                Options:
                    1) Teapot
                    2) Fridge
                    3) Tv
                    Input:
                """;
        System.out.println(chooseClass);
        int classNumber = scanner.nextInt();
        switch (classNumber) {
            case 1 -> {
                return Teapot.class;
            }
            case 2 -> {
                return Fridge.class;
            }
            case 3 -> {
                return Tv.class;
            }
            default -> throw new UnsupportedOptionException("There is no such option");
        }
    }

    /**
     * Run application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        ProductService productService = new ProductServiceImpl();
        Scanner scanner = new Scanner(System.in);
        String options = """
                                
                Options:
                    1) Add product
                    2) Read all products
                    3) Update product
                    4) Delete product
                    5) Find by class
                    6) Find cheapest
                    Input:
                """;
        try {
            int option = -1;
            while (option != 7) {
                System.out.println(options);
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> productService.create(inputAnyProduct());
                    case 2 -> productService.read().forEach(System.out::println);
                    case 3 -> {
                        BaseProduct product = inputAnyProduct();
                        System.out.print("Input id: ");
                        product.setId(scanner.nextInt());
                        productService.update(product);
                    }
                    case 4 -> {
                        System.out.print("Input id: ");
                        productService.delete(scanner.nextInt());
                    }
                    case 5 -> productService.findByClass(inputClass()).forEach(System.out::println);
                    case 6 -> {
                        BaseProduct product = productService.findCheapest();
                        System.out.println(product != null ? product : "");
                    }
                    case 7 -> System.out.println("Have a good day!");
                    default -> throw new UnsupportedOptionException("There is no such option");
                }
            }
        } catch (UnsupportedOptionException e) {
            System.out.println(e.getMessage());
        }
    }
}
