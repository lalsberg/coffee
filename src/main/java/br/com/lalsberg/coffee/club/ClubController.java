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

@RestController
public class ClubController {

	private Clubs clubs;

	@Autowired
	public ClubController(Clubs clubs) {
		this.clubs = clubs;
	}

	@RequestMapping(method= RequestMethod.POST, value = "/clubs/user/{userId}", produces = "application/json")
	public ResponseEntity<Club> create(@PathVariable long userId, @RequestParam String name) {
		User owner = new User(userId);
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
			ClubUser clubUser = new ClubUser(club, new User(memberId));
			//TODO pq preciso setar o club no clubuser e depois usar ele pra addmember?
			club.addMember(clubUser);
		});

		clubs.save(club);
		return ResponseEntity.ok().build();
	}
}
