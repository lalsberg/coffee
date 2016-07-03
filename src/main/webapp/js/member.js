loadMembers();

$("#btnAddMember").click(function() {
	var clubId = jQuery.parseJSON(Cookies.get('club')).id;

	$.post("http://localhost:8080/clubs/" + clubId + "/members", {
		email : $("#newMemberEmail").val()
	}, function() {
		addToMemberlist($("#newMemberEmail").val());
	});
});

function loadMembers() {
	var clubId = jQuery.parseJSON(Cookies.get('club')).id;

	$.get("http://localhost:8080/clubs/" + clubId + "/members", function(members) {
		$(members).each(function() {
			addToMemberlist(this.email);
		});
	});
}

function addToMemberlist(email) {
	var memberHtmlElement = $(".member").first().clone();
	memberHtmlElement.find("label").text(email);
	memberHtmlElement.show();
	$("#members").append(memberHtmlElement);
}
