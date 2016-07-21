package br.com.lalsberg.coffee.company;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.lalsberg.coffee.order.Order;
import br.com.lalsberg.coffee.user.User;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	@JsonBackReference
	@OneToMany(mappedBy = "company", cascade = CascadeType.MERGE)
	private List<User> members = new ArrayList<User>();

	@OneToMany(mappedBy = "company", cascade = CascadeType.MERGE)
	private List<Order> orders = new ArrayList<Order>();

	public Company(String name) {
		this.name = name;
	}

	public Company() {}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getMembers() {
		return members;
	}

	public void addMember(User user) {
		members.add(user);
	}

}
