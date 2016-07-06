
package br.com.lalsberg.coffee.coffee;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	@RequestMapping(method = RequestMethod.POST, path = "/coffees")
	public ResponseEntity<Coffee> create(@RequestParam String name, @RequestParam double price) {
		Coffee coffee = new Coffee();
		coffee.setName(name);
		coffee.setPrice(price);
		Coffee persistedCoffee = coffees.save(coffee);
		//TODO adicionar /coffeeId no fim da url, o mesmo para outros .created
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(persistedCoffee);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/coffees/{coffeeId}")
	public ResponseEntity<Void> update(@PathVariable long coffeeId, @RequestBody Coffee updatedCoffee) {
		Coffee coffee = coffees.findOne(coffeeId);
		coffee.setName(updatedCoffee.getName());
		coffee.setPrice(updatedCoffee.getPrice());
		coffees.save(coffee);
		return ResponseEntity.ok().build();
	}
}
