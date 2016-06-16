package br.com.lalsberg.coffee.userorder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.lalsberg.coffee.order.Order;
import br.com.lalsberg.coffee.user.User;

public class UserOrdersImpl implements UserOrdersCustom {

	@Autowired
	private UserOrders userOrders;

	@Override
	public UserOrder findOrCreateByUserAndOrder(User user, Order order) {
		Optional<UserOrder> existingUserOrder = userOrders.findByUserIdAndOrderId(user.getId(), order.getId());

		if(existingUserOrder.isPresent()) {
			return existingUserOrder.get();
		} else {
			UserOrder userOrder = new UserOrder();
			userOrder.setUser(user);
			userOrder.setOrder(order);
			return userOrder;
		}
	}

}
