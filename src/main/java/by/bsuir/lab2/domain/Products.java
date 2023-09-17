package by.bsuir.lab2.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Contains list of products and used to persist products in storage
 *
 * @see BaseProduct
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Products {

    /**
     * List of products
     */
    @XmlElement(name = "product")
    private List<BaseProduct> products;
}
