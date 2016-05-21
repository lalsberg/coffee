package br.com.lalsberg.coffee.order;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

	private Orders orders;

	@Autowired
	public OrderProducer(Orders orders) {
		this.orders = orders;
	}

	@Bean
	public Order currentOrder() {
		Optional<Order> currentOrder = orders.findByActive(true);
		if(currentOrder.isPresent()) {
			return currentOrder.get();
		}

		Order order = new Order();
		order.setActive(true);
		return orders.save(order);
	}

}
