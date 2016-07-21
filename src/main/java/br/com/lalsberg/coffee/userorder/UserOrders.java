package br.com.lalsberg.coffee.userorder;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserOrders extends CrudRepository<UserOrder, Long>, UserOrdersCustom {

	Optional<UserOrder> findByUserIdAndOrderId(long userId, long orderId);

	Optional<UserOrder> findByOrderActiveTrueAndUserId(long userId);
}
