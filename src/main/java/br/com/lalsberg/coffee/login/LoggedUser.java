package br.com.lalsberg.coffee.login;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import br.com.lalsberg.coffee.User.User;

public class LoggedUser implements Authentication {

	private static final long serialVersionUID = 1L;
	private boolean authenticated = true;
	private long id;
	private String email;

	public LoggedUser(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
	}

	public long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	//TODO ??
	@Override
	public String getName() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}

}
