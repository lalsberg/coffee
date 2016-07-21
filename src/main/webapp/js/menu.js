var user = jQuery.parseJSON(Cookies.get('user'));
$(".username").text(user.name);
$(".company").text(user.company.name);

$("#menuCompany").show();

$("#logout").click(function() {
	Cookies.remove('jwtToken');
	Cookies.remove('user');
});