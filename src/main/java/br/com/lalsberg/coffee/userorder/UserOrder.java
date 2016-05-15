package br.com.lalsberg.coffee.userorder;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.lalsberg.coffee.User.User;
import br.com.lalsberg.coffee.coffee.Coffee;
import br.com.lalsberg.coffee.order.Order;

@Entity
@Table
public class UserOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne()
	private User user;

	@OneToOne()
	private Order order;

	@OneToMany
	@JoinColumn(name="coffee_id")
	private List<Coffee> coffees;

	
	public void addCoffees(List<Coffee> coffees) {
		this.coffees.addAll(coffees);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public long getId() {
		return id;
	}

}
