loadMembers();

function loadMembers() {
	var clubId = jQuery.parseJSON(Cookies.get('club')).id;

	$.get("http://localhost:8080/clubs/" + clubId + "/members", function(members) {
		$(members).each(function() {
			var memberHtmlElement = $(".member").first().clone();
			memberHtmlElement.find("label").text(this.name);
			memberHtmlElement.show();
			$("#members").append(memberHtmlElement);
		});
	});
}
