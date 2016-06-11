package br.com.lalsberg.coffee.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

	public String getTokenFromEmail(String email) {
		return Jwts.builder().setSubject(email).signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
	}

}
