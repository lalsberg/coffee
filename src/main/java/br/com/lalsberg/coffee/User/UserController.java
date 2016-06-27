package br.com.lalsberg.coffee.user;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.club.Club;
import br.com.lalsberg.coffee.club.Clubs;
import br.com.lalsberg.coffee.security.Token;

@RestController
public class UserController {

	private Users users;
	private Clubs clubs;
	private Token token;

	@Autowired
	public UserController(Users users, Clubs clubs, Token token) {
		this.users = users;
		this.clubs = clubs;
		this.token = token;
	}

	@RequestMapping(method = POST, path = "/users")
	public String create(@RequestParam String email, @RequestParam String name, @RequestParam String password) {
		User user = new User();
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		user.setPassword(encryptedPassword);
		user.setEmail(email);
		user.setName(name);
		users.save(user);
		return token.generateTokenFromEmail(email);
	}

	//TODO seria melhor dizer que o caminho principal seria clubs/users e que esse seria com query? clubs?userId=x
	@RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/clubs", produces = "application/json")
	public List<Club> listClubs(@PathVariable long userId) {
		return clubs.findByOwnerIdOrMembersUserId(userId, userId);
	}

}
