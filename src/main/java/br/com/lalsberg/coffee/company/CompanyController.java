package br.com.lalsberg.coffee.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lalsberg.coffee.security.LoggedUser;
import br.com.lalsberg.coffee.security.Token;
import br.com.lalsberg.coffee.user.User;
import br.com.lalsberg.coffee.user.Users;

@RestController
public class CompanyController {

	private Companies companies;
	private Users users;
	private Token token;

	@Autowired
	public CompanyController(Companies companies, Users users, Token token) {
		this.companies = companies;
		this.users = users;
		this.token = token;
	}

	@RequestMapping(method= RequestMethod.POST, path = "/companies")
	public Map<String, String> create(@RequestParam String email, @RequestParam String name, 
			@RequestParam String password, @RequestParam String companyName) {

		Company company = new Company(companyName);
		Company persistedCompany = companies.save(company);

		User user = new User();
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		user.setPassword(encryptedPassword);
		user.setEmail(email);
		user.setName(name);

		user.setCompany(persistedCompany);
		User persistedUser = users.save(user);

		Map<String, String> response = new HashMap<String, String>();
		try {
			response.put("user", new ObjectMapper().writeValueAsString(persistedUser));
		} catch (JsonProcessingException e) {
			throw new RuntimeException();
		}
		response.put("token", token.generateTokenFromEmail(email));
		return response;
	}

	@RequestMapping(method= RequestMethod.POST, value = "/companies/{companyId}/members", produces = "application/json")
	public ResponseEntity<Void> addMembers(@PathVariable long companyId, @RequestParam String email, @RequestParam String name, @RequestParam String password) {

		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Company company = companies.findOne(companyId);

		if(!company.getMembers().stream().anyMatch(member -> member.getId() == loggedUser.getId())) {
			return ResponseEntity.notFound().build();
		}

		User user = new User();
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		user.setPassword(encryptedPassword);
		user.setEmail(email);
		user.setName(name);

		user.setCompany(company);
		users.save(user);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method= RequestMethod.GET, value = "/companies/{companyId}/members")
	public List<User> getMembers(@PathVariable long companyId) {
		Company company = companies.findOne(companyId);
		return company.getMembers();
	}

}
