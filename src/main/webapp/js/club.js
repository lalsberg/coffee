$("#btnCreateClub").click(function() {
	$.post("http://localhost:8080/clubs", {
		name : $("#name").val(),
	}, function() {
		window.location.replace("/pages/index.html");
	});
});
