package br.com.lalsberg.coffee.order;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.club.Clubs;

@RestController
public class OrderController {

	private Orders orders;
	private Clubs clubs;

	@Autowired
	public OrderController(Orders orders, Clubs clubs) {
		this.orders = orders;
		this.clubs = clubs;
	}

	@RequestMapping(method= RequestMethod.GET, value = "/clubs/{clubId}/order")
	public Order getCurrent(@PathVariable long clubId) {
		Optional<Order> currentOrder = orders.findByActiveTrueAndClubId(clubId);
		if(currentOrder.isPresent()) {
			return currentOrder.get();
		}
		return null;
	}

	@RequestMapping(method= RequestMethod.PUT, value = "/clubs/{clubId}/orders")
	public Order create(@PathVariable long clubId) {
		Optional<Order> currentOrder = orders.findByActiveTrueAndClubId(clubId);
		if(currentOrder.isPresent()) {
			return currentOrder.get();
		}
		Order order = new Order();
		order.setActive(true);
		order.setClub(clubs.getOne(clubId));
		return orders.save(order);
	}
}
