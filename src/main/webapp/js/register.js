$("#btnRegister").click(function() {

	$.post("http://localhost:8080/users", {
		email : $("#email").val(),
		name : $("#name").val(),
		password : $("#password").val()
	}, function(response) {
		Cookies.set('userId', response.userId);
		Cookies.set('jwtToken', response.token);
		Cookies.remove('club');
		window.location.replace("/pages/index.html");
	});
});
