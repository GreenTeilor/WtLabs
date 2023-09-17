package by.bsuir.lab2.input.impl;

import by.bsuir.lab2.domain.Teapot;
import by.bsuir.lab2.input.InputHandler;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Implementation of InputHandler for teapot
 *
 * @see InputHandler
 */
public class TeapotInputHandler implements InputHandler<Teapot> {

    /**
     * Returns inputted teapot
     *
     * @return inputted teapot
     */
    @Override
    public Teapot input() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input name: ");
        String name = scanner.next();
        System.out.print("Input price: ");
        BigDecimal price = scanner.nextBigDecimal();
        System.out.print("Is your teapot electrical?: ");
        boolean isElectrical = scanner.nextBoolean();
        return Teapot.builder().
                name(name).
                price(price).
                isElectrical(isElectrical).
                build();
    }
}
