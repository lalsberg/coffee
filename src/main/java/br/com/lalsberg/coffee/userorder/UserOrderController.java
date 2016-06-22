package br.com.lalsberg.coffee.userorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.lalsberg.coffee.coffee.Coffee;

@RestController
//@Scope("prototype")
//TODO: Apenas currentOrder precisa atualizar sempre, userOrders nao. ver @Lookup
//OU controlar a criacao da order. quando lista e algm ja fechou, vai criar e mostrar uma nova order vazia... fica um pouco imprevisivel
public class UserOrderController {

	private UserOrders userOrders;
//	private Order currentOrder;

//	@Autowired
//	public UserOrderController(UserOrders userOrders, Order currentOrder) {
//		this.userOrders = userOrders;
//		this.currentOrder = currentOrder;
//	}

	@Autowired
	public UserOrderController(UserOrders userOrders) {
		this.userOrders = userOrders;
	}

	@RequestMapping(method= RequestMethod.POST, value = "/club/{clubId}/orders/user/{userId}", produces = "application/json")
	public ResponseEntity<UserOrderCoffee> addCoffee(@PathVariable long clubId, @PathVariable long userId, @RequestBody UserOrderCoffee coffeeOrder) {
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(1, userId);
		if(userOrder.isPresent()) {
			userOrder.get().addCoffee(coffeeOrder);
		}
		//else create userorder com os coffees

		UserOrderCoffee updatedCoffeeOrder = userOrder.get().getCoffee(coffeeOrder.getCoffee()).get();

		userOrders.save(userOrder.get());
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(updatedCoffeeOrder);
	}

	@RequestMapping(method= RequestMethod.DELETE, value = "/club/{clubId}/orders/user/{userId}/coffee/{coffeeId}", produces = "application/json")
	public ResponseEntity<Void> removeCoffee(@PathVariable long clubId, @PathVariable long userId, @PathVariable long coffeeId) {
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(1, userId);
		Coffee coffee = new Coffee();
		coffee.setId(coffeeId);
		userOrder.get().removeCoffee(coffee);

		userOrders.save(userOrder.get());
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method= RequestMethod.GET, value = "/club/{clubId}/orders/user/{userId}", produces = "application/json")
	public List<UserOrderCoffee> listCoffees(@PathVariable long clubId, @PathVariable long userId) {
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, userId);
		if(userOrder.isPresent()) {
			return userOrder.get().getCoffees();
		}
		return new ArrayList<>();
	}

	
}
