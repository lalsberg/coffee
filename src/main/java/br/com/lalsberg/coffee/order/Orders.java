package br.com.lalsberg.coffee.order;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface Orders extends CrudRepository<Order, Long> {

	Optional<Order> findByActive(boolean active);

}
