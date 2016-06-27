package br.com.lalsberg.coffee.club;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.lalsberg.coffee.security.LoggedUser;
import br.com.lalsberg.coffee.user.User;
import br.com.lalsberg.coffee.user.Users;

@RestController
public class ClubController {

	private Clubs clubs;
	private Users users;

	@Autowired
	public ClubController(Clubs clubs, Users users) {
		this.clubs = clubs;
		this.users = users;
	}

	@RequestMapping(method= RequestMethod.POST, path = "/clubs", produces = "application/json")
	public ResponseEntity<Club> create(@RequestParam String name) {
		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User owner = new User(loggedUser.getId());
		Club club = clubs.save(new Club(owner, name));

		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).body(club);
	}

	@RequestMapping(method= RequestMethod.POST, value = "/clubs/{clubId}/members", produces = "application/json")
	public ResponseEntity<Void> addMembers(@PathVariable long clubId, @RequestBody List<Long> membersIds) {
		Club club = clubs.findOne(clubId);

		LoggedUser loggedUser = (LoggedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(!club.isOwner(loggedUser.getId())) {
			return ResponseEntity.notFound().build();
		}

		membersIds.forEach(memberId -> {
			club.addMember(users.getOne(memberId));
		});

		clubs.save(club);
		return ResponseEntity.ok().build();
	}
}
