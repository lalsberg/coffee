package br.com.lalsberg.coffee.coffee;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoffeeController {

	private Coffees coffees;

	@Autowired
	public CoffeeController(Coffees coffees) {
		this.coffees = coffees;
	}

	@RequestMapping(method = GET, path = "/coffees")
	public Iterable<Coffee> list() {
		return coffees.findAll();
	}

}
