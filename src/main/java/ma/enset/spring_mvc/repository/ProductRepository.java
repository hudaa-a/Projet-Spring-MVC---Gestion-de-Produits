package ma.enset.spring_mvc.repository;

import ma.enset.spring_mvc.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
