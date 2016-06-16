package br.com.lalsberg.coffee.login;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.User.User;
import br.com.lalsberg.coffee.User.Users;
import br.com.lalsberg.coffee.security.Token;

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
	public String authenticate(@RequestParam String email, @RequestParam String password) {
		Optional<User> user = users.findByEmail(email);
		if(!user.isPresent()) {
			//404
		}
		if (BCrypt.checkpw(password, user.get().getPassword())) {
			return token.generateTokenFromEmail(email);
		}
		return null; //error
	}

}
