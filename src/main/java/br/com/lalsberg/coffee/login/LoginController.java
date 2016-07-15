package br.com.lalsberg.coffee.login;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.security.Token;
import br.com.lalsberg.coffee.user.User;
import br.com.lalsberg.coffee.user.Users;

@RestController
public class LoginController {

	private Users users;
	private Token token;

	@Autowired
	public LoginController(Users users, Token token) {
		this.users = users;
		this.token = token;
	}

	@RequestMapping(method = POST, path = "/authenticate")
	public Map<String, String> authenticate(@RequestParam String email, @RequestParam String password) {
		Optional<User> user = users.findByEmail(email);
		if(!user.isPresent()) {
			//404
		}
		if (BCrypt.checkpw(password, user.get().getPassword())) {
			Map<String, String> response = new HashMap<String, String>();
			response.put("username", user.get().getName());
			response.put("token", token.generateTokenFromEmail(email));
			return response;
		}
		return null; //error
	}

}
