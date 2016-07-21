//$.ajaxSetup({
//  headers: {
//    'Authorization': Cookies.get('jwtToken')
//  }
//});

$("#btnLogin").click(function() {

	$.post("http://localhost:8080/authenticate", {
		email : $("#email").val(),
		password : $("#password").val()
	}, function(response) {
		Cookies.set('user', response.user);
		Cookies.set('jwtToken', response.token);
		window.location.replace("/pages/index.html");
	});

});
