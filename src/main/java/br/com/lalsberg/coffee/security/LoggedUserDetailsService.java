package br.com.lalsberg.coffee.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.lalsberg.coffee.user.User;
import br.com.lalsberg.coffee.user.Users;

@Service
public class LoggedUserDetailsService implements UserDetailsService {

	private Users users;

	@Autowired
	public LoggedUserDetailsService(Users users) {
		this.users = users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = users.findByEmail(username);

		if (!userOptional.isPresent()) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			User user = userOptional.get();
			return new LoggedUser(user.getId(), user.getEmail(), user.getPassword(), user.getEmail(),
					// theUser.getLastPasswordReset(),
					null,
					// AuthorityUtils.commaSeparatedStringToAuthorityList(theUser.getAuthorities())
					null);
		}
	}

}