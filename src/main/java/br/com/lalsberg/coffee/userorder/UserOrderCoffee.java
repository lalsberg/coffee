package br.com.lalsberg.coffee.userorder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.lalsberg.coffee.coffee.Coffee;

@Entity
public class UserOrderCoffee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne()
	@JoinColumn(name="coffee_id")
	@JsonProperty("coffee")
	private Coffee coffee;

	@JsonProperty("quantity")
	private int quantity;

	private boolean deleted;

	public Coffee getCoffee() {
		return coffee;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		this.deleted = true;
	}

}
