$("#btnRegister").click(function() {

	$.post("http://localhost:8080/users", {
		email : $("#email").val(),
		name : $("#name").val(),
		password : $("#password").val()
	}, function(response) {
		Cookies.set('jwtToken', response.token);
		Cookies.set('username', $("#name").val());
		Cookies.remove('club');
		window.location.replace("/pages/index.html");
	});
});
