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
					"<p>" + this.coffee.name + "</p>" +
					"<button type='button' class='btn btn-danger btn-block' style='width: 80px'>Remover</button>" +
					"<input type='hidden' name='coffeeId' value='" + this.coffee.id + "'>" +
				"</li>";
		});

		$("#orderedCoffees").append(itemsHtml);

		$("#orderedCoffees").on("change", ".list-group-item", function() {
//		$("#orderedCoffees .list-group-item").change(function() {
			var coffeeId = $(this).find("input[name='coffeeId']").val();
			var coffeeQuantityInput = $(this).find("input[name='coffeeQuantity']");
			var coffeeName = $(this).find("p").text();
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
				type : 'PUT', 
				success: function(updatedCoffeeOrder) {
					if(coffeeQuantity == 0) {
						coffeeQuantityInput.parent().remove();

						var removedCoffeeHtml = 
							"<li class='list-group-item'>" +
								"<input type='text' name='coffeeQuantity' value='0'></input> " + 
								"<p>" + coffeeName + "</p>" +
								"<input type='hidden' name='coffeeId' value='" + coffeeId + "'>" +
							"</li>";

						$("#coffees").append(removedCoffeeHtml);
					}
				}
			});
		});

		$("#orderedCoffees").on("click", ".btn-danger", function() {
			var removeButton = $(this);
			var coffeeId = removeButton.siblings("input[name='coffeeId']").val();
			var coffeeName = $(this).siblings("p").text();
			var coffeeQuantity = $(this).siblings("input[name='coffeeQuantity']").val();

			$.ajax("http://localhost:8080/club/1/orders/user/1/coffee/" + coffeeId, {
				type : 'DELETE',
				success: function() {
					removeButton.parent().remove();

					var removedCoffeeHtml = 
						"<li class='list-group-item'>" +
							"<input type='text' name='coffeeQuantity' value='0'></input> " + 
							"<p>" + coffeeName + "</p>" +
							"<input type='hidden' name='coffeeId' value='" + coffeeId + "'>" +
						"</li>";

					$("#coffees").append(removedCoffeeHtml);
				}
			});
		});
	});

	$.get("http://localhost:8080/club/1/orders/user/1/unorderedCoffees", function(coffees) {
		var itemsHtml = "";
		$(coffees).each(function() {
			itemsHtml += 
				"<li class='list-group-item'> " +
					"<input type='text' name='coffeeQuantity' value='0'></input> " +
					"<p>" + this.name + "</p>" +
					"<input type='hidden' name='coffeeId' value='" + this.id + "'>" +
				"</li>";
		});
		$("#coffees").append(itemsHtml);

//		$("#coffees .list-group-item").change(function() {
		$("#coffees").on("change", ".list-group-item", function() {
			var coffeeId = $(this).find("input[name='coffeeId']").val();
			var coffeeQuantityInput = $(this).find("input[name='coffeeQuantity']");
			var coffeeName = $(this).find("p").text();
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
					coffeeQuantityInput.parent().remove();

					var orderedCoffeeHtml = 
						"<li class='list-group-item'>" +
							"<input type='text' name='coffeeQuantity' value='" + coffeeQuantity + "'></input> " + 
							"<p>" + coffeeName + "</p>" +
							"<button type='button' class='btn btn-danger btn-block' style='width: 80px'>Remover</button>" +
							"<input type='hidden' name='coffeeId' value='" + coffeeId + "'>" +
						"</li>";

					$("#orderedCoffees").append(orderedCoffeeHtml);
				}
			});
		});
	});

});