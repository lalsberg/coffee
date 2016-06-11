package br.com.lalsberg.coffee.User;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface Users extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);

}
