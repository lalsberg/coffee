$("#btnCreateClub").click(function() {
	$.post("http://localhost:8080/clubs", {
		name : $("#name").val(),
	}, function(club) {
		var selectedClub = {
			name: club.name,
			id: club.id
		};

		Cookies.set('club', selectedClub);
		window.location.replace("/pages/index.html");
	});
});
