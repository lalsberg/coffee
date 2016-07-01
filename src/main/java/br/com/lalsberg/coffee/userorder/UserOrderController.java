package br.com.lalsberg.coffee.userorder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.lalsberg.coffee.coffee.Coffee;
import br.com.lalsberg.coffee.coffee.Coffees;
import br.com.lalsberg.coffee.order.Orders;
import br.com.lalsberg.coffee.user.Users;

@RestController
public class UserOrderController {

	private UserOrders userOrders;
	private Coffees coffees;
	private Orders orders;
	private Users users;

	@Autowired
	public UserOrderController(UserOrders userOrders, Coffees coffees, Orders orders, Users users) {
		this.userOrders = userOrders;
		this.coffees = coffees;
		this.orders = orders;
		this.users = users;
	}

	@RequestMapping(method= RequestMethod.POST, value = "/club/{clubId}/orders/user/{userId}", produces = "application/json")
	public ResponseEntity<UserOrderCoffee> addCoffee(@PathVariable long clubId, @PathVariable long userId, @RequestBody UserOrderCoffee coffeeOrder) {
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(1, userId);

		UserOrderCoffee updatedCoffeeOrder;
		if(userOrder.isPresent()) {
			userOrder.get().addCoffee(coffeeOrder);

			updatedCoffeeOrder = userOrder.get().getCoffee(coffeeOrder.getCoffee()).get();
			userOrders.save(userOrder.get());
		} else {
			UserOrder newUserOrder = new UserOrder();
			newUserOrder.setOrder(orders.findByActiveTrueAndClubId(clubId).get());
			newUserOrder.setUser(users.getOne(userId));
			newUserOrder.addCoffee(coffeeOrder);

			updatedCoffeeOrder = newUserOrder.getCoffee(coffeeOrder.getCoffee()).get();
			userOrders.save(newUserOrder);
		}

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(updatedCoffeeOrder);
	}

	@RequestMapping(method= RequestMethod.PUT, value = "/club/{clubId}/orders/user/{userId}", produces = "application/json")
	public ResponseEntity<Void> changeCoffeeQuantity(@PathVariable long clubId, @PathVariable long userId, @RequestBody UserOrderCoffee coffeeOrder) {
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(1, userId);
		if(userOrder.isPresent()) {
			if(coffeeOrder.getQuantity() == 0) {
				userOrder.get().removeCoffee(coffeeOrder.getCoffee());
			} else {
				userOrder.get().changeCoffeeQuantity(coffeeOrder);
			}
		} //TODO else erro

		userOrders.save(userOrder.get());
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method= RequestMethod.DELETE, value = "/club/{clubId}/orders/user/{userId}/coffee/{coffeeId}", produces = "application/json")
	public ResponseEntity<Void> removeCoffee(@PathVariable long clubId, @PathVariable long userId, @PathVariable long coffeeId) {
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, userId);
		Coffee coffee = new Coffee();
		coffee.setId(coffeeId);
		userOrder.get().removeCoffee(coffee);

		userOrders.save(userOrder.get());
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method= RequestMethod.GET, value = "/club/{clubId}/orders/user/{userId}", produces = "application/json")
	public UserOrder get(@PathVariable long clubId, @PathVariable long userId) {
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, userId);
		if(userOrder.isPresent()) {
			return userOrder.get();
		}
		return null;
	}

	@RequestMapping(method= RequestMethod.GET, value = "/club/{clubId}/orders/user/{userId}/unorderedCoffees", produces = "application/json")
	public List<Coffee> listUnorderedCoffees(@PathVariable long clubId, @PathVariable long userId) {
		List<Coffee> allCoffees = (List<Coffee>) coffees.findAll();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, userId);
		if(userOrder.isPresent()) {
			Set<Long> orderedCoffeesIds = userOrder.get().getCoffees().stream().map(orderedCoffee -> orderedCoffee.getCoffee().getId()).collect(Collectors.toSet());
			List<Coffee> unorderedCoffees = allCoffees.stream().filter(coffee -> !orderedCoffeesIds.contains(coffee.getId())).collect(Collectors.toList());
			return unorderedCoffees;
		}
		return allCoffees;
	}

	@RequestMapping(method= RequestMethod.GET, value = "/club/{clubId}/orders/user/{userId}/price", produces = "application/json")
	public double getPrice(@PathVariable long clubId, @PathVariable long userId) {
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, userId);
		return userOrder.get().getPrice();
	}
}
