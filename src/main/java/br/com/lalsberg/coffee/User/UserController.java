package br.com.lalsberg.coffee.User;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private Users users;

	@Autowired
	public UserController(Users users) {
		this.users = users;
	}

	@RequestMapping(method = POST, path = "/users")
	public void create(@RequestBody User user) {
		String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(encryptedPassword);
		users.save(user);
	}

}
