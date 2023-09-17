package by.bsuir.lab2.input;

import by.bsuir.lab2.domain.BaseProduct;

/**
 * Provides API to input any type of product
 *
 * @see by.bsuir.lab2.input.impl.TeapotInputHandler
 * @see by.bsuir.lab2.input.impl.FridgeInputHandler
 * @see by.bsuir.lab2.input.impl.TvInputHandler
 */
public interface InputHandler<T extends BaseProduct> {

    /**
     * Returns inputted product
     *
     * @return inputted product
     */
    T input();
}
