package by.bsuir.lab2.input.impl;

import by.bsuir.lab2.domain.Fridge;
import by.bsuir.lab2.input.InputHandler;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Implementation of InputHandler for fridge
 *
 * @see InputHandler
 */
public class FridgeInputHandler implements InputHandler<Fridge> {

    /**
     * Returns inputted fridge
     *
     * @return inputted fridge
     */
    @Override
    public Fridge input() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input name: ");
        String name = scanner.next();
        System.out.print("Input price: ");
        BigDecimal price = scanner.nextBigDecimal();
        System.out.print("Input min temperature: ");
        int minTemperature = scanner.nextInt();
        System.out.print("Input max temperature: ");
        int maxTemperature = scanner.nextInt();
        return Fridge.builder().
                name(name).
                price(price).
                minTemperature(minTemperature).
                maxTemperature(maxTemperature).
                build();
    }
}
