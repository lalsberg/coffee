package br.com.lalsberg.coffee.userorder;

import static javax.persistence.FetchType.EAGER;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import br.com.lalsberg.coffee.coffee.Coffee;
import br.com.lalsberg.coffee.order.Order;
import br.com.lalsberg.coffee.user.User;

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


	public void addCoffee(UserOrderCoffee orderingCoffee) {
		this.coffees.add(orderingCoffee);
	}

	public void changeCoffeeQuantity(UserOrderCoffee orderingCoffee) {
		for (UserOrderCoffee existingCoffee : getCoffees()) {
			if(existingCoffee.getCoffee().getId() == orderingCoffee.getCoffee().getId()) {
				existingCoffee.setQuantity(orderingCoffee.getQuantity());
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
		//TODO: nao consegui buscar somente ativos pelo metodo do spring data jpa
		List<UserOrderCoffee> activeCoffees = new ArrayList<UserOrderCoffee>();
		for (UserOrderCoffee coffee : coffees) {
			if(!coffee.isDeleted()) {
				activeCoffees.add(coffee);
			}
		}

		return activeCoffees;
	}

	public Optional<UserOrderCoffee> getCoffee(Coffee coffee) {
		return getCoffees().stream().filter(findCoffee -> findCoffee.getCoffee().getId() == coffee.getId()).findAny();
	}

	public void removeCoffee(Coffee coffee) {
		UserOrderCoffee userOrderCoffee = getCoffees().stream().filter(findCoffee -> findCoffee.getCoffee().getId() == coffee.getId()).findAny().get();
		userOrderCoffee.delete();
	}

	@Override
	public String toString() {
		return "UserOrder [id=" + id + ", user=" + user + ", order=" + order.getId() + ", coffees=" + coffees + "]";
	}

}
