package br.com.lalsberg.coffee.userorder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.User.User;
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
	@RequestMapping(method= RequestMethod.POST, value = "/orders/user/{userId}", produces = "application/json")
	public ResponseEntity addCoffees(@PathVariable long userId, @RequestBody List<UserOrderCoffee> coffeeOrder) {
		Optional<Order> currentOrder = orders.findByActive(true);
		if(!currentOrder.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no active order");
		}

		Optional<UserOrder> existingUserOrder = userOrders.findByUserIdAndOrderId(userId, currentOrder.get().getId());

		UserOrder userOrder;
		if(existingUserOrder.isPresent()) {
			userOrder = existingUserOrder.get();
		} else {
			userOrder = createUserOrder(userId, currentOrder);
		}

		userOrder.addCoffees(coffeeOrder);
		userOrders.save(userOrder);

		try {
			return ResponseEntity.created(new URI("//userOrders//" + userOrder.getId())).body(existingUserOrder);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private UserOrder createUserOrder(long userId, Optional<Order> currentOrder) {
		UserOrder userOrder;
		userOrder = new UserOrder();
		User user = new User();
		user.setId(userId);
		userOrder.setUser(user);
		userOrder.setOrder(currentOrder.get());
		return userOrder;
	}

}
