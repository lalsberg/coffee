$("#btnRegister").click(function() {

	$.post("http://localhost:8080/users", {
		email : $("#email").val(),
		name : $("#name").val(),
		password : $("#password").val()
	}, function(token) {
		Cookies.set('jwtToken', token);
		window.location.replace("/pages/index.html");
	});
});
