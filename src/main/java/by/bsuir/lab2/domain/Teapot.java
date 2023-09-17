package by.bsuir.lab2.domain;

import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * POJO class
 *
 * @see BaseProduct
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@XmlType(name = "teapot")
public class Teapot extends BaseProduct {

    /**
     * Is teapot electrical
     */
    private boolean isElectrical;
}
