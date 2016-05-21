package br.com.lalsberg.coffee.userorder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.User.User;
import br.com.lalsberg.coffee.order.Order;

@RestController
@Scope("prototype")
//TODO: Apenas currentOrder precisa atualizar sempre, userOrders nao. ver @Lookup
//OU controlar a criacao da order. quando lista e algm ja fechou, vai criar e mostrar uma nova order vazia... fica um pouco imprevisivel
public class UserOrderController {

	private UserOrders userOrders;
	private Order currentOrder;

	@Autowired
	public UserOrderController(UserOrders userOrders, Order currentOrder) {
		this.userOrders = userOrders;
		this.currentOrder = currentOrder;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(method= RequestMethod.POST, value = "/orders/user/{userId}", produces = "application/json")
	public ResponseEntity addCoffees(@PathVariable long userId, @RequestBody List<UserOrderCoffee> coffeeOrder) {
		UserOrder userOrder = userOrders.findOrCreateByUserAndOrder(new User(userId), currentOrder);
		userOrder.addCoffees(coffeeOrder);
		userOrders.save(userOrder);
		try {
			return ResponseEntity.created(new URI("//userOrders//" + userOrder.getId())).body(userOrder);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(method= RequestMethod.GET, value = "/orders/user/{userId}", produces = "application/json")
	public List<UserOrderCoffee> listCoffees(@PathVariable long userId) {
		UserOrder userOrder = userOrders.findOrCreateByUserAndOrder(new User(userId), currentOrder);
		return userOrder.getCoffees();
	}

}
