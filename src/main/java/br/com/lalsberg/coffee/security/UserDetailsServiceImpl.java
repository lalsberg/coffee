package br.com.lalsberg.coffee.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.lalsberg.coffee.User.User;
import br.com.lalsberg.coffee.User.Users;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private Users users;

	@Autowired
	public UserDetailsServiceImpl(Users users) {
		this.users = users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = users.findByEmail(username);

		if (!user.isPresent()) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return LoggedUserFactory.create(user.get());
		}
	}

}