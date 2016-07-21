$("#btnRegister").click(function() {

	$.post("http://localhost:8080/companies", {
		email : $("#email").val(),
		name : $("#name").val(),
		password : $("#password").val(),
		companyName : $("#companyName").val(),
	}, function(response) {
		Cookies.set('user', response.user);
		Cookies.set('jwtToken', response.token);
		window.location.replace("/pages/index.html");
	});
});
