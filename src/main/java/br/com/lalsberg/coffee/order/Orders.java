package br.com.lalsberg.coffee.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface Orders extends CrudRepository<Order, Long> {

	Optional<Order> findByActive(boolean active);

	@Query("SELECT o FROM Order o LEFT JOIN FETCH o.userOrders WHERE o.active = (:active)")
	Optional<Order> findByActiveAndFetchUserOrdersEagerly(@Param("active") boolean active);

	@Query("SELECT o FROM Order o LEFT JOIN FETCH o.userOrders uo WHERE o.active = (:active) and o.company.id = (:companyId) and uo.user.id = (:userId)")
	Optional<Order> findByActiveAndCompanyIdAndUserIdAndFetchUserOrdersEagerly(@Param("active") boolean active,
			@Param("companyId") long companyId, @Param("userId") long userId);

	Optional<Order> findByActiveTrueAndCompanyId(long companyId);

}
