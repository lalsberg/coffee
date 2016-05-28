package br.com.lalsberg.coffee.club;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.lalsberg.coffee.User.User;

@Entity
public class Club {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="owner_id")
	private User owner;

	@OneToMany(mappedBy = "club", cascade = CascadeType.MERGE)
	private List<ClubUser> members = new ArrayList<ClubUser>();

	public Club(User owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	public Club() {}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getOwner() {
		return owner;
	}

	public List<ClubUser> getMembers() {
		return members;
	}

	public void addMember(ClubUser clubUser) {
		members.add(clubUser);
	}

}
