package pl.test.learn.productdemo.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link pl.test.learn.productdemo.models.Product}
 */
@Getter
@Setter
public class ProductDTO implements Serializable {
    Long id;
    @NotNull(message = "name property cannot be null")
    String name;
    @NotNull(message = "description property cannot be null")
    String description;
    @NotNull(message = "price property cannot be null")
    Double price;
    @NotNull(message = "message property cannot be null")
    Integer quantity;

    @Override
    public String toString() {
        return "{name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}