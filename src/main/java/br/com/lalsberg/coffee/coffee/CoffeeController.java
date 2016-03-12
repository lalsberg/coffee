package br.com.lalsberg.coffee.coffee;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoffeeController {

	@RequestMapping("/coffee")
    public Coffee someLibraryMethod() {
		Coffee coffee = new Coffee();
		coffee.setId(1);
		coffee.setName("Vanilio");

		return coffee;
    }

}
