package br.com.lalsberg.coffee.order;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

	@Autowired
	Orders orders;

	@SuppressWarnings("rawtypes")
	@RequestMapping(method= RequestMethod.POST, value = "/orders", produces = "application/json")
	public ResponseEntity create() {
		Optional<Order> currentOrder = orders.findByActive(true);
		if(currentOrder.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is an active order already");
		}

		Order order = new Order();
		order.setActive(true);
		orders.save(order);
		try {
			return ResponseEntity.created(new URI("//order//" + order.getId())).body(order);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
