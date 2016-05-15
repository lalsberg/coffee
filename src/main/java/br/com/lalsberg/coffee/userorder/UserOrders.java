package br.com.lalsberg.coffee.userorder;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserOrders extends CrudRepository<UserOrder, Long> {

	Optional<UserOrder> findByUserIdAndOrderId(long userId, long orderId);

}
