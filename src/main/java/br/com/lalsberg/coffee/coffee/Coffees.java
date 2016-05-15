package br.com.lalsberg.coffee.coffee;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface Coffees extends PagingAndSortingRepository<Coffee, Long> {

	List<Coffee> findByName(@Param("name") String name);

}
