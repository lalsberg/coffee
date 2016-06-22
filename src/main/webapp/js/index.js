$(function() {

	var userClubsIds = [];
	$.get("http://localhost:8080/users/1/clubs", function(clubs) {
		$(clubs).each(function() {
			userClubsIds.push(this.id)
		});
	});

	$.get("http://localhost:8080/club/1/orders/user/1", function(items) {
		var itemsHtml = "";
		$(items).each(function() {
			itemsHtml += 
				"<li class='list-group-item'>" +
					"<input type='text' name='coffeeQuantity' value='" + this.quantity + "'></input> " + 
					this.coffee.name + 
					"<button type='button' class='btn btn-danger btn-block' style='width: 80px'>Remover</button>" +
					"<input type='hidden' name='coffeeId' value='" + this.coffee.id + "'>" +
				"</li>";
		});

		$("#orderedCoffees").append(itemsHtml);

		$("#orderedCoffees .btn-danger").click(function() {
			var removeButton = $(this);
			var coffeeId = removeButton.siblings("input[name='coffeeId']").val();

			$.ajax("http://localhost:8080/club/1/orders/user/1/coffee/" + coffeeId, {
				type : 'DELETE',
				success: function() {
					removeButton.parent().remove();
				}
			});
		});
	});

	$.get("http://localhost:8080/coffees", function(coffees) {
		var itemsHtml = "";
		$(coffees).each(function() {
			itemsHtml += 
				"<li class='list-group-item'> " +
					"<input type='text' name='coffeeQuantity' value='0'></input> " + 
					this.name + 
					"<input type='hidden' name='coffeeId' value='" + this.id + "'>" +
				"</li>";
		});
		$("#coffees").append(itemsHtml);

		$("#coffees .list-group-item").change(function() {
			var coffeeId = $(this).find("input[name='coffeeId']").val();
			var coffeeQuantityInput = $(this).find("input[name='coffeeQuantity']");
			var coffeeQuantity = coffeeQuantityInput.val();

			var coffeeOrder = {
				coffee: {
					id: coffeeId
				},
				quantity: coffeeQuantity
			};

			$.ajax("http://localhost:8080/club/1/orders/user/1", { 
				data: JSON.stringify(coffeeOrder),
				contentType : 'application/json', 
				type : 'POST', 
				success: function(updatedCoffeeOrder) {
					coffeeQuantityInput.val(0);

					$("#orderedCoffees")
						.find("input[name='coffeeId'][value='" + updatedCoffeeOrder.coffee.id +"']")
						.siblings("input[name='coffeeQuantity']")
						.val(updatedCoffeeOrder.quantity);
				}
			});
		});
	});

});