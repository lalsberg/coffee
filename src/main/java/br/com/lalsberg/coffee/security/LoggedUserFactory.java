package br.com.lalsberg.coffee.security;

import br.com.lalsberg.coffee.User.User;

public class LoggedUserFactory {

	public static LoggedUser create(User user) {
		return new LoggedUser(user.getId(), user.getEmail(), user.getPassword(), user.getEmail(),
				// user.getLastPasswordReset(),
				null,
				// AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities())
				null);
	}

}