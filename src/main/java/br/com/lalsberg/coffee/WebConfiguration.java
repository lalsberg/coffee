package br.com.lalsberg.coffee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import br.com.lalsberg.coffee.User.Users;
import br.com.lalsberg.coffee.login.AuthenticationInterceptor;
import br.com.lalsberg.coffee.login.Token;

@Configuration
class WebConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private Users users;
	@Autowired
	private Token token;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(authenticationInterceptor());
	}

	@Bean
	@Autowired
	public AuthenticationInterceptor authenticationInterceptor() {
	    return new AuthenticationInterceptor(users, token);
	}

}
