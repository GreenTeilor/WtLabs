package by.bsuir.lab2.input.impl;

import by.bsuir.lab2.domain.Tv;
import by.bsuir.lab2.input.InputHandler;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Implementation of InputHandler for tv
 *
 * @see InputHandler
 */
public class TvInputHandler implements InputHandler<Tv> {

    /**
     * Returns inputted tv
     *
     * @return inputted tv
     */
    @Override
    public Tv input() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input name: ");
        String name = scanner.next();
        System.out.print("Input price: ");
        BigDecimal price = scanner.nextBigDecimal();
        System.out.print("Input min temperature: ");
        int horizontalDimension = scanner.nextInt();
        System.out.print("Input max temperature: ");
        int verticalDimension = scanner.nextInt();
        return Tv.builder().
                name(name).
                price(price).
                horizontalDimension(horizontalDimension).
                verticalDimension(verticalDimension).
                build();
    }
}
