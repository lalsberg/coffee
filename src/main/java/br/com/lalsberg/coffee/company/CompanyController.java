package br.com.lalsberg.coffee.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.lalsberg.coffee.security.LoggedUser;
import br.com.lalsberg.coffee.user.User;
import br.com.lalsberg.coffee.user.Users;

@RestController
public class CompanyController {

	private Companies companies;
	private Users users;

	@Autowired
	public CompanyController(Companies companies, Users users) {
		this.companies = companies;
		this.users = users;
	}

	//TODO not used yet
	@RequestMapping(method= RequestMethod.POST, path = "/companies", produces = "application/json")
	public ResponseEntity<Company> create(@RequestParam String name) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User owner = new User(loggedUser.getId());
		Company company = new Company(name);
		company.addMember(owner);
		//TODO set role owner
		Company persistedCompany = companies.save(company);

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(persistedCompany);
	}

	@RequestMapping(method= RequestMethod.POST, value = "/companies/{companyId}/members", produces = "application/json")
	public ResponseEntity<Void> addMembers(@PathVariable long companyId, @RequestParam String email) {
		Company company = companies.findOne(companyId);

//		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		//TODO
//		if(!company.isOwner(loggedUser.getId())) {
//			return ResponseEntity.notFound().build();
//		}

		company.addMember(users.findByEmail(email).get());

		companies.save(company);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(method= RequestMethod.GET, value = "/companies/{companyId}/members")
	public List<User> getMembers(@PathVariable long companyId) {
		Company company = companies.findOne(companyId);
		return company.getMembers();
	}

}
