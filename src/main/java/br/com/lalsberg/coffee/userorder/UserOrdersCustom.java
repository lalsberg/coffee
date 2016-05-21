package br.com.lalsberg.coffee.userorder;

import br.com.lalsberg.coffee.User.User;
import br.com.lalsberg.coffee.order.Order;

public interface UserOrdersCustom {

	UserOrder findOrCreateByUserAndOrder(User user, Order order);

}
