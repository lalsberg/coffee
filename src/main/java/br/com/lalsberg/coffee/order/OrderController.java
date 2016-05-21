package br.com.lalsberg.coffee.order;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.userorder.UserOrder;

@RestController
public class OrderController {

	private Order currentOrder;
	private Orders orders;

	@Autowired
	public OrderController(@Qualifier("withUserOrders") Order currentOrder, Orders orders) {
		this.currentOrder = currentOrder;
		this.orders = orders;
	}

	@RequestMapping(method= RequestMethod.GET, value = "/orders", produces = "application/json")
	public List<UserOrder> listUserOrders() {
		return currentOrder.getUserOrders();
	}

	@RequestMapping(method= RequestMethod.PUT, value = "/orders/close", produces = "application/json")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional
	public void close() {
		currentOrder.setActive(false);
		orders.save(currentOrder);
	}

}
