$("#btnLogin").click(function() {

	$.post("http://localhost:8080/authenticate", 
			{ 
			  email : $("#email").val(), 
			  password : $("#password").val() 
			}, function(token) {
				Cookies.set('jwtToken', token);
				window.location.replace("/index.html");
			});
});
