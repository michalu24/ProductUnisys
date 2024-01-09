package pl.test.learn.productdemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.test.learn.productdemo.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}