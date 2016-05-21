package br.com.lalsberg.coffee.userorder;

import static javax.persistence.FetchType.EAGER;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.lalsberg.coffee.User.User;
import br.com.lalsberg.coffee.order.Order;

@Entity
@Table(name = "user_order")
public class UserOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne()
	private User user;

	@ManyToOne()
	@JoinColumn(name="order_id")
	@JsonIgnore
	private Order order;

	@OneToMany(fetch = EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="user_order_id", nullable=false)
	private List<UserOrderCoffee> coffees = new ArrayList<UserOrderCoffee>();


	public void addCoffees(List<UserOrderCoffee> coffees) {
		for (UserOrderCoffee orderingCoffee : coffees) {
			boolean coffeeAlreadyOrdered = false;

			for (UserOrderCoffee existingCoffee : this.coffees) {
				if(existingCoffee.getCoffee().getId() == orderingCoffee.getCoffee().getId()) {
					coffeeAlreadyOrdered = true;
					existingCoffee.add(orderingCoffee.getQuantity());
				}
			}

			if(!coffeeAlreadyOrdered) {
				this.coffees.add(orderingCoffee);
			}
		}
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

	public List<UserOrderCoffee> getCoffees() {
		return coffees;
	}

	@Override
	public String toString() {
		return "UserOrder [id=" + id + ", user=" + user + ", order=" + order.getId() + ", coffees=" + coffees + "]";
	}

}
