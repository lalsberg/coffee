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
		Cookies.set('username', response.username);
		Cookies.set('jwtToken', response.token);
		setSelectedClub();
		window.location.replace("/pages/index.html");
	});

	function setSelectedClub() {
		$.ajax({
			url: "http://localhost:8080/users/me/clubs", 
			async: false,
			success: function(clubs) {
				if(clubs.length > 0) {
					var selectedClub = {
						name: clubs[0].name,
						id: clubs[0].id
					};
	
					Cookies.set('club', selectedClub);
				} else {
					Cookies.remove('club');
				}
			}
		});
	}

});
