package br.com.lalsberg.coffee.user;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.club.Club;
import br.com.lalsberg.coffee.club.Clubs;

@RestController
public class UserController {

	private Users users;
	private Clubs clubs;

	@Autowired
	public UserController(Users users, Clubs clubs) {
		this.users = users;
		this.clubs = clubs;
	}

	@RequestMapping(method = POST, path = "/users")
	public void create(@RequestBody User user) {
		String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(encryptedPassword);
		users.save(user);
	}

	//TODO seria melhor dizer que o caminho principal seria clubs/users e que esse seria com query? clubs?userId=x
	@RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/clubs", produces = "application/json")
	public List<Club> listClubs(@PathVariable long userId) {
		return clubs.findByOwnerIdOrMembersUserId(userId, userId);
	}

}
