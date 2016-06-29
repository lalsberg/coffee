var userId = Cookies.get('userId');

var selectedClub = jQuery.parseJSON(Cookies.get('club'));
$(".selectedClubName").text(selectedClub.name);
$(".selectedClubName").siblings("input[name='clubId']").val(selectedClub.id);

$.get("http://localhost:8080/users/" + userId + "/clubs", function(clubs) {
	var first = true;
	$(clubs).each(function(index) {
		if(first){
			if(this.id != selectedClub.id) {
				first = false;
				$(".anotherClub").find("a").text(this.name);
				$(".anotherClub").find("input[name='clubId']").val(this.id);
			}
		} else {
			if(this.id != selectedClub.id) {
				var anotherClub = $(".anotherClub").first().clone();
				anotherClub.find("a").text(this.name);
				anotherClub.find("input[name='clubId']").val(this.id);
				$(".otherClubs").append(anotherClub);
			}
		}

		if(clubs.length == 1) {
			$("#uniqueClub").show();
		} else {
			$("#multipleClub").show();
		}

	});

	$("#multipleClub").on("click", ".anotherClub", function() {
		var club = {
			name: $(this).find("a").text(),
			id: $(this).find("input[name='clubId']").val()
		};

		$(".selectedClubName").text(club.name);
		$(".selectedClubName").siblings("input[name='clubId']").val(club.id);

		var oldClub = jQuery.parseJSON(Cookies.get('club'));
		$(this).find("a").text(oldClub.name);
		$(this).find("input[name='clubId']").val(oldClub.id);

		Cookies.set('club', club);
		window.location.reload();
	});
});
