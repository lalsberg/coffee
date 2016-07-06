loadCoffees();

$("#btnAddCoffee").click(function() {
	$.post("http://localhost:8080/coffees", {
		name : $("#newCoffeeName").val(),
		price : $("#newCoffeePrice").val()
	}, function(coffee) {
		addToCoffeelist(coffee.id, coffee.name, coffee.price);
		$("#newCoffeeName").val(""),
		$("#newCoffeePrice").val("")
	});
});

$("#coffees").on("click", ".editCoffeeLink", function() {
	$(this).parent().parent().find(".coffeeName").prop("disabled", false);
	$(this).parent().parent().find(".coffeePrice").prop("disabled", false);
	$(this).parent().parent().find(".editCoffeeLink").hide();
	$(this).parent().parent().find(".saveCoffeeLink").show();
	$(this).parent().parent().find(".coffeeName").focus();
});

$("#coffees").on("click", ".saveCoffeeLink", function() {
	var clickedCoffee = $(this).parent().parent();

	var coffee = {
		id: clickedCoffee.find("input[name='coffeeId']").val(),
		name: $(this).parent().parent().find(".coffeeName").val(),
		price : $(this).parent().parent().find(".coffeePrice").val()
	};

	$.ajax("http://localhost:8080/coffees/" + coffee.id, {
		data: JSON.stringify(coffee),
		contentType : 'application/json',
		type : 'PUT', 
		success: function() {
			clickedCoffee.find(".coffeeName").prop("disabled", true);
			clickedCoffee.find(".coffeePrice").prop("disabled", true);
			clickedCoffee.find(".saveCoffeeLink").hide();
			clickedCoffee.find(".editCoffeeLink").show();
		}
	});

});

function loadCoffees() {
	$.get("http://localhost:8080/coffees", function(coffees) {
		$(coffees).each(function() {
			addToCoffeelist(this.id, this.name, this.price);
		});
	});
}

function addToCoffeelist(id, name, price) {
	var coffeeHtmlElement = $(".coffee").first().clone();
	coffeeHtmlElement.find("input[name='coffeeId']").val(id);
	coffeeHtmlElement.find(".coffeeName").val(name);
	coffeeHtmlElement.find(".coffeePrice").val(price);
	coffeeHtmlElement.show();
	$("#coffees").append(coffeeHtmlElement);
}
