package br.com.lalsberg.coffee.club;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.lalsberg.coffee.user.User;

@Entity
@Table(name = "club_user")
@IdClass(ClubUserId.class)
public class ClubUser {

	@Id
	@ManyToOne
	@JoinColumn(name = "club_id")
	private Club club;

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public ClubUser(Club club, User user) {
		this.club = club;
		this.user = user;
	}
	
	public ClubUser() {}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
