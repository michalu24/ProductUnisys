package pl.test.learn.productdemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.test.learn.productdemo.models.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}