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
		Cookies.set('userId', response.userId);
		Cookies.set('jwtToken', response.token);
		setSelectedClub(response.userId);
		window.location.replace("/pages/index.html");
	});

	function setSelectedClub(userId) {
		$.ajax({
			url: "http://localhost:8080/users/" + userId + "/clubs", 
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
