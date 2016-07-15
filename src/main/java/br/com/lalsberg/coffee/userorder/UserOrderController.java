package br.com.lalsberg.coffee.userorder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.lalsberg.coffee.coffee.Coffee;
import br.com.lalsberg.coffee.coffee.Coffees;
import br.com.lalsberg.coffee.order.Orders;
import br.com.lalsberg.coffee.security.LoggedUser;
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

	@RequestMapping(method= RequestMethod.POST, value = "/club/{clubId}/orders/user/me", produces = "application/json")
	public ResponseEntity<UserOrderCoffee> addCoffee(@PathVariable long clubId, @RequestBody UserOrderCoffee coffeeOrder) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, loggedUser.getId());

		UserOrderCoffee updatedCoffeeOrder;
		if(userOrder.isPresent()) {
			userOrder.get().addCoffee(coffeeOrder);

			updatedCoffeeOrder = userOrder.get().getCoffee(coffeeOrder.getCoffee()).get();
			userOrders.save(userOrder.get());
		} else {
			UserOrder newUserOrder = new UserOrder();
			newUserOrder.setOrder(orders.findByActiveTrueAndClubId(clubId).get());
			newUserOrder.setUser(users.getOne(loggedUser.getId()));
			newUserOrder.addCoffee(coffeeOrder);

			updatedCoffeeOrder = newUserOrder.getCoffee(coffeeOrder.getCoffee()).get();
			userOrders.save(newUserOrder);
		}

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(updatedCoffeeOrder);
	}

	@RequestMapping(method= RequestMethod.PUT, value = "/club/{clubId}/orders/user/me", produces = "application/json")
	public ResponseEntity<Void> changeCoffeeQuantity(@PathVariable long clubId, @RequestBody UserOrderCoffee coffeeOrder) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, loggedUser.getId());
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

	@RequestMapping(method= RequestMethod.DELETE, value = "/club/{clubId}/orders/user/me/coffee/{coffeeId}", produces = "application/json")
	public ResponseEntity<Void> removeCoffee(@PathVariable long clubId, @PathVariable long coffeeId) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, loggedUser.getId());
		Coffee coffee = new Coffee();
		coffee.setId(coffeeId);
		userOrder.get().removeCoffee(coffee);

		userOrders.save(userOrder.get());
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method= RequestMethod.GET, value = "/club/{clubId}/orders/user/me", produces = "application/json")
	public UserOrder get(@PathVariable long clubId) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, loggedUser.getId());
		if(userOrder.isPresent()) {
			return userOrder.get();
		}
		return null; //404
	}

	@RequestMapping(method= RequestMethod.GET, value = "/club/{clubId}/orders/user/me/unorderedCoffees", produces = "application/json")
	public List<Coffee> listUnorderedCoffees(@PathVariable long clubId) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, loggedUser.getId());

		List<Coffee> allCoffees = (List<Coffee>) coffees.findAll();
		if(userOrder.isPresent()) {
			Set<Long> orderedCoffeesIds = userOrder.get().getCoffees().stream().map(orderedCoffee -> orderedCoffee.getCoffee().getId()).collect(Collectors.toSet());
			List<Coffee> unorderedCoffees = allCoffees.stream().filter(coffee -> !orderedCoffeesIds.contains(coffee.getId())).collect(Collectors.toList());
			return unorderedCoffees;
		}
		return allCoffees;
	}

	@RequestMapping(method= RequestMethod.GET, value = "/club/{clubId}/orders/user/me/price", produces = "application/json")
	public double getPrice(@PathVariable long clubId) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndOrderClubIdAndUserId(clubId, loggedUser.getId());
		return userOrder.get().getPrice();
	}

}
