package br.com.lalsberg.coffee.login;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.lalsberg.coffee.User.User;
import br.com.lalsberg.coffee.User.Users;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	private Users users;
	private Token token;

	@Autowired
	public AuthenticationInterceptor(Users users, Token token) {
		this.users = users;
		this.token = token;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {

			boolean permitRequest = ((HandlerMethod) handler).getMethod().isAnnotationPresent(PermitEndpoint.class);
			boolean isErrorPath = "/error".equals(request.getServletPath());

			if(permitRequest || isErrorPath) {
				return true;
			}

			String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (isEmpty(jwtToken)) {
				return false;
			}

			String email = token.getEmailFromToken(jwtToken);
			Optional<User> user = users.findByEmail(email);

			if (user.isPresent()) {
				LoggedUser loggedUser = new LoggedUser(user.get());
				SecurityContextHolder.getContext().setAuthentication(loggedUser);
				return true;
			}
		}

		response.setStatus(UNAUTHORIZED.value());
		return false;
	}

}
