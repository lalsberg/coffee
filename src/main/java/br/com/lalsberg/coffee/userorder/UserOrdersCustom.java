package br.com.lalsberg.coffee.userorder;

import br.com.lalsberg.coffee.order.Order;
import br.com.lalsberg.coffee.user.User;

public interface UserOrdersCustom {

	UserOrder findOrCreateByUserAndOrder(User user, Order order);

}
