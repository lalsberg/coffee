package br.com.lalsberg.coffee.company;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Companies extends JpaRepository<Company, Long> {

}
