package br.com.lalsberg.coffee.order;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.company.Companies;

@RestController
public class OrderController {

	private Orders orders;
	private Companies companies;

	@Autowired
	public OrderController(Orders orders, Companies companies) {
		this.orders = orders;
		this.companies = companies;
	}

	@RequestMapping(method= RequestMethod.GET, value = "/companies/{companyId}/order")
	public Order getCurrent(@PathVariable long companyId) {
		Optional<Order> currentOrder = orders.findByActiveTrueAndCompanyId(companyId);
		if(currentOrder.isPresent()) {
			return currentOrder.get();
		}
		return null;
	}

	@RequestMapping(method= RequestMethod.PUT, value = "/companies/{companyId}/orders")
	public Order create(@PathVariable long companyId) {
		Optional<Order> currentOrder = orders.findByActiveTrueAndCompanyId(companyId);
		if(currentOrder.isPresent()) {
			return currentOrder.get();
		}
		Order order = new Order();
		order.setActive(true);
		order.setCompany(companies.getOne(companyId));
		return orders.save(order);
	}

}
