package br.com.lalsberg.coffee.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private Token token;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String jwtToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

		if (jwtToken == null) {
			Cookie[] cookies = httpRequest.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals("jwtToken")) {
						jwtToken = cookies[i].getValue();
						break;
					}
				}
			}
		}

		if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			String username = token.getEmailFromToken(jwtToken);
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			// if (this.tokenUtils.validateToken(authToken, userDetails)) {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// }
		}

		chain.doFilter(request, response);
	}

}