package br.com.lalsberg.coffee.userorder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.User.User;
import br.com.lalsberg.coffee.coffee.Coffee;
import br.com.lalsberg.coffee.order.Order;
import br.com.lalsberg.coffee.order.Orders;

@RestController
public class UserOrderController {

	private UserOrders userOrders;
	private Orders orders;

	@Autowired
	public UserOrderController(UserOrders userOrders, Orders orders) {
		this.userOrders = userOrders;
		this.orders = orders;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(method= RequestMethod.POST, value = "/userOrders", produces = "application/json")
	public ResponseEntity addCoffees(@RequestParam User user, @RequestParam List<Coffee> coffeeList) {
		Optional<Order> currentOrder = orders.findByActive(true);
		if(!currentOrder.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no active order");
		}

		Optional<UserOrder> userOrder = userOrders.findByUserIdAndOrderId(user.getId(), currentOrder.get().getId());
		if(userOrder.isPresent()) {
			userOrder.get().addCoffees(coffeeList);
			userOrders.save(userOrder.get());
		}

		try {
			return ResponseEntity.created(new URI("//userOrders//" + userOrder.get().getId())).body(userOrder);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
