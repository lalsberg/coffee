package br.com.lalsberg.coffee.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Users extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

}
