package by.bsuir.lab2.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Abstract class for all POJO classes
 *
 * @see Teapot
 * @see Fridge
 * @see Tv
 */
@Data
@NoArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Teapot.class, Fridge.class, Tv.class})
public abstract class BaseProduct {

    /**
     * Id to identify each object in storage
     */
    private int id;

    /**
     * Product name
     */
    private String name;

    /**
     * Product price
     */
    private BigDecimal price;
}
