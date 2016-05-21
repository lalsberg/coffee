package br.com.lalsberg.coffee.order;

import static javax.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.lalsberg.coffee.userorder.UserOrder;

@Entity
@Table(name = "\"order\"")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private boolean active;

	@OneToMany(fetch = LAZY, mappedBy = "order")
	private List<UserOrder> userOrders = new ArrayList<UserOrder>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<UserOrder> getUserOrders() {
		return userOrders;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", active=" + active + ", userOrders=" + userOrders + "]";
	}

}
