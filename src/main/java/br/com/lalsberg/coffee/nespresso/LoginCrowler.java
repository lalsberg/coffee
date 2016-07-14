package br.com.lalsberg.coffee.nespresso;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class LoginCrowler {

	private static final String url = "https://www.nespresso.com/br/pt/home";
	private static final String email = "leandro_alsberg2@hotmail.com";
	private static final String password = "passn3spr3ss0";

	public void login() {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements emailInput = doc.select("ta-header-username");
			emailInput.val(email);
			Elements passwordInput = doc.select("ta-header-password");
			passwordInput.val(password);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
