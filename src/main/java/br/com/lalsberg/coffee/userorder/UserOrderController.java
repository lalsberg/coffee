package br.com.lalsberg.coffee.userorder;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	@RequestMapping(method= POST, value = "/orders/user/{userId}", produces = "application/json")
	public ResponseEntity<UserOrder> addCoffees(@PathVariable long userId, @RequestBody List<UserOrderCoffee> coffeeOrder) {
		UserOrder userOrder = userOrders.findOrCreateByUserAndOrder(new User(userId), currentOrder);
		userOrder.addCoffees(coffeeOrder);
		userOrders.save(userOrder);

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(userOrder);
	}

	@RequestMapping(method= GET, value = "/orders/user/{userId}", produces = "application/json")
	public List<UserOrderCoffee> listCoffees(@PathVariable long userId) {
		UserOrder userOrder = userOrders.findOrCreateByUserAndOrder(new User(userId), currentOrder);
		return userOrder.getCoffees();
	}

}
