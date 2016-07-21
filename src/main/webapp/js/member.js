loadMembers();

$("#btnAddMember").click(function() {
	var companyId = jQuery.parseJSON(Cookies.get('user')).company.id;

	$.post("http://localhost:8080/companies/" + companyId + "/members", {
		email : $("#newMemberEmail").val()
	}, function() {
		addToMemberlist($("#newMemberEmail").val());
	});
});

function loadMembers() {
	var companyId = jQuery.parseJSON(Cookies.get('user')).company.id;

	$.get("http://localhost:8080/companies/" + companyId + "/members", function(members) {
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
