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
import br.com.lalsberg.coffee.user.User;
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

	//TODO add /coffeeId, assim como o delete. so a qtd viria como param. ou mudar para botao 'buy' e setar qtd sempre 1
	@RequestMapping(method= RequestMethod.POST, value = "/me/orders/coffees", produces = "application/json")
	public ResponseEntity<UserOrderCoffee> addCoffee(@RequestBody UserOrderCoffee coffeeOrder) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndUserId(loggedUser.getId());

		UserOrderCoffee updatedCoffeeOrder;
		if(userOrder.isPresent()) {
			userOrder.get().addCoffee(coffeeOrder);

			updatedCoffeeOrder = userOrder.get().getCoffee(coffeeOrder.getCoffee()).get();
			userOrders.save(userOrder.get());
		} else {
			UserOrder newUserOrder = new UserOrder();
			User user = users.findOne(loggedUser.getId());
			newUserOrder.setOrder(orders.findByActiveTrueAndCompanyId(user.getCompany().getId()).get());
			newUserOrder.setUser(user);
			newUserOrder.addCoffee(coffeeOrder);

			updatedCoffeeOrder = newUserOrder.getCoffee(coffeeOrder.getCoffee()).get();
			userOrders.save(newUserOrder);
		}

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(updatedCoffeeOrder);
	}

	//TODO add /coffeeId, assim como o delete. so a qtd viria como param
	@RequestMapping(method= RequestMethod.PUT, value = "/me/orders/coffees", produces = "application/json")
	public ResponseEntity<Void> changeCoffeeQuantity(@RequestBody UserOrderCoffee coffeeOrder) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndUserId(loggedUser.getId());
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

	@RequestMapping(method= RequestMethod.DELETE, value = "/me/orders/coffees/{coffeeId}", produces = "application/json")
	public ResponseEntity<Void> removeCoffee(@PathVariable long coffeeId) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndUserId(loggedUser.getId());
		Coffee coffee = new Coffee();
		coffee.setId(coffeeId);
		userOrder.get().removeCoffee(coffee);

		userOrders.save(userOrder.get());
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method= RequestMethod.GET, value = "/me/orders/coffees", produces = "application/json")
	public UserOrder get() {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndUserId(loggedUser.getId());
		if(userOrder.isPresent()) {
			return userOrder.get();
		}
		return null; //404
	}

	@RequestMapping(method= RequestMethod.GET, value = "/me/orders/unorderedCoffees", produces = "application/json")
	public List<Coffee> listUnorderedCoffees() {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndUserId(loggedUser.getId());

		List<Coffee> allCoffees = (List<Coffee>) coffees.findAll();
		if(userOrder.isPresent()) {
			Set<Long> orderedCoffeesIds = userOrder.get().getCoffees().stream().map(orderedCoffee -> orderedCoffee.getCoffee().getId()).collect(Collectors.toSet());
			List<Coffee> unorderedCoffees = allCoffees.stream().filter(coffee -> !orderedCoffeesIds.contains(coffee.getId())).collect(Collectors.toList());
			return unorderedCoffees;
		}
		return allCoffees;
	}

	@RequestMapping(method= RequestMethod.GET, value = "/me/orders/price", produces = "application/json")
	public double getPrice() {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserOrder> userOrder = userOrders.findByOrderActiveTrueAndUserId(loggedUser.getId());
		return userOrder.get().getPrice();
	}

}
