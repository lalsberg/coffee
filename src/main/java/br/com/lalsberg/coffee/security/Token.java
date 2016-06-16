package br.com.lalsberg.coffee.security;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Token {

	private String jwtSecret;

	@Autowired
	public Token(@Value("${jwt.secret}") String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	public String getEmailFromToken(String jwtToken) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody().getSubject();
	}

	public String generateTokenFromEmail(String email) {
		return Jwts.builder().setSubject(email).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
	}

	public Optional<String> getTokenFromRequest(HttpServletRequest httpRequest) {
		String jwtToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

		if (jwtToken != null) {
			return Optional.of(jwtToken);
		}

		Cookie cookie = WebUtils.getCookie(httpRequest, "jwtToken");
		if(cookie != null) {
			return Optional.of(cookie.getValue());
		}

		return Optional.empty();
	}

}
