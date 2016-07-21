package br.com.lalsberg.coffee.user;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lalsberg.coffee.company.Companies;
import br.com.lalsberg.coffee.company.Company;
import br.com.lalsberg.coffee.security.Token;

@RestController
public class UserController {

	private Users users;
	private Companies companies;
	private Token token;

	@Autowired
	public UserController(Users users, Companies companies, Token token) {
		this.users = users;
		this.companies = companies;
		this.token = token;
	}

	@RequestMapping(method = POST, path = "/users")
	public Map<String, String> create(@RequestParam String email, @RequestParam String name, @RequestParam String password) {
		User user = new User();
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		user.setPassword(encryptedPassword);
		user.setEmail(email);
		user.setName(name);

		//TODO company deve vir de um link de email
		Company company = new Company();
		company.setName("Teste");

		user.setCompany(companies.findOne(1l));

		users.save(user);

		Map<String, String> response = new HashMap<String, String>();
		response.put("token", token.generateTokenFromEmail(email));
		return response;
	}

}
