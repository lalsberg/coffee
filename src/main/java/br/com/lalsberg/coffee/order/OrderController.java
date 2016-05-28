package br.com.lalsberg.coffee.order;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.userorder.UserOrder;

@RestController
@Scope("prototype")
//TODO: Apenas currentOrder precisa atualizar sempre, orders nao. ver @Lookup
//OU controlar a criacao da order. quando lista e algm ja fechou, vai criar e mostrar uma nova order vazia... fica um pouco imprevisivel
public class OrderController {

	private Order currentOrder;
	private Orders orders;

	@Autowired
	public OrderController(@Qualifier("withUserOrders") Order currentOrder, Orders orders) {
		this.currentOrder = currentOrder;
		this.orders = orders;
	}

	@RequestMapping(method= GET, value = "/orders", produces = "application/json")
	public List<UserOrder> listUserOrders() {
		return currentOrder.getUserOrders();
	}

	@RequestMapping(method= PUT, value = "/orders/close", produces = "application/json")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void close() {
		currentOrder.setActive(false);
		orders.save(currentOrder);
	}

}
