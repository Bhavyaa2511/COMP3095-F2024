package ca.gbc.orderservice.repository;



import ca.gbc.orderservice.model.order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<order, Long> {
}
