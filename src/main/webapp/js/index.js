$(function() {

	var userId = Cookies.get('userId');

	$.get("http://localhost:8080/club/1/orders/user/" + userId, function(userOrder) {
		var itemsHtml = "";
		$(userOrder.coffees).each(function() {
			itemsHtml += 
				"<li class='list-group-item'>" +
					"<input type='text' name='coffeeQuantity' value='" + this.quantity + "'></input> " +
					"<label class='coffeeName' style='font-weight:normal'>" + this.coffee.name + " </label>" +
					"<label style='font-weight:normal; margin-left: 5px'>R$ </label>" +
					"<label class='coffeePrice' style='font-weight:normal'>" + this.coffee.price + "</label>" +
					"<button type='button' class='btn btn-danger btn-block' style='width: 80px'>Remover</button>" +
					"<input type='hidden' name='coffeeId' value='" + this.coffee.id + "'>" +
				"</li>";
		});

		$("#orderedCoffees").append(itemsHtml);
		$(".cartPrice").text(userOrder.price);
	});

	$.get("http://localhost:8080/club/1/orders/user/" + userId + "/unorderedCoffees", function(coffees) {
		var itemsHtml = "";
		$(coffees).each(function() {
			itemsHtml += 
				"<li class='list-group-item'> " +
					"<input type='text' name='coffeeQuantity' value='0'></input> " +
					"<label class='coffeeName' style='font-weight:normal'>" + this.name + " </label>" +
					"<label style='font-weight:normal; margin-left: 5px'>R$ </label>" +
					"<label class='coffeePrice' style='font-weight:normal'>" + this.price + "</label>" +
					"<input type='hidden' name='coffeeId' value='" + this.id + "'>" +
				"</li>";
		});
		$("#coffees").append(itemsHtml);
	});

	$("#coffees").on("change", ".list-group-item", function() {
		var coffeeId = $(this).find("input[name='coffeeId']").val();
		var coffeeQuantityInput = $(this).find("input[name='coffeeQuantity']");
		var coffeeName = $(this).find(".coffeeName").text();
		var coffeePrice = $(this).find(".coffeePrice").text();
		var coffeeQuantity = coffeeQuantityInput.val();

		var coffeeOrder = {
			coffee: {
				id: coffeeId
			},
			quantity: coffeeQuantity
		};

		$.ajax("http://localhost:8080/club/1/orders/user/" + userId, { 
			data: JSON.stringify(coffeeOrder),
			contentType : 'application/json', 
			type : 'POST', 
			success: function(updatedCoffeeOrder) {
				coffeeQuantityInput.parent().remove();

				var orderedCoffeeHtml = 
					"<li class='list-group-item'>" +
						"<input type='text' name='coffeeQuantity' value='" + coffeeQuantity + "'></input> " + 
						"<label class='coffeeName' style='font-weight:normal'>" + coffeeName + "</label>" +
						"<label style='font-weight:normal; margin-left:5px'>R$</label>" +
						"<label class='coffeePrice' style='font-weight:normal'>" + coffeePrice + "</label>" +
						"<button type='button' class='btn btn-danger btn-block' style='width: 80px'>Remover</button>" +
						"<input type='hidden' name='coffeeId' value='" + coffeeId + "'>" +
					"</li>";

				$("#orderedCoffees").append(orderedCoffeeHtml);
				updateCartPrice()
			}
		});
	});

	$("#orderedCoffees").on("change", ".list-group-item", function() {
		var coffeeId = $(this).find("input[name='coffeeId']").val();
		var coffeeQuantityInput = $(this).find("input[name='coffeeQuantity']");
		var coffeeName = $(this).find(".coffeeName").text();
		var coffeePrice = $(this).find(".coffeePrice").text();
		var coffeeQuantity = coffeeQuantityInput.val();

		var coffeeOrder = {
			coffee: {
				id: coffeeId
			},
			quantity: coffeeQuantity
		};

		$.ajax("http://localhost:8080/club/1/orders/user/" + userId, { 
			data: JSON.stringify(coffeeOrder),
			contentType : 'application/json', 
			type : 'PUT', 
			success: function() {
				if(coffeeQuantity == 0) {
					coffeeQuantityInput.parent().remove();

					var removedCoffeeHtml = 
						"<li class='list-group-item'>" +
							"<input type='text' name='coffeeQuantity' value='0'></input> " + 
							"<label class='coffeeName' style='font-weight:normal'>" + coffeeName + "</label>" +
							"<label style='font-weight:normal; margin-left:5px'>R$</label>" +
							"<label class='coffeePrice' style='font-weight:normal'>" + coffeePrice + "</label>" +
							"<input type='hidden' name='coffeeId' value='" + coffeeId + "'>" +
						"</li>";

					$("#coffees").append(removedCoffeeHtml);
				}
				updateCartPrice();
			}
		});
	});

	$("#orderedCoffees").on("click", ".btn-danger", function() {
		var removeButton = $(this);
		var coffeeId = removeButton.siblings("input[name='coffeeId']").val();
		var coffeeName = $(this).siblings(".coffeeName").text();
		var coffeePrice = $(this).siblings(".coffeePrice").text();
		var coffeeQuantity = $(this).siblings("input[name='coffeeQuantity']").val();

		$.ajax("http://localhost:8080/club/1/orders/user/" + userId + "/coffee/" + coffeeId, {
			type : 'DELETE',
			success: function() {
				removeButton.parent().remove();

				var removedCoffeeHtml = 
					"<li class='list-group-item'>" +
						"<input type='text' name='coffeeQuantity' value='0'></input> " + 
						"<label class='coffeeName' style='font-weight:normal'>" + coffeeName + "</label>" +
						"<label style='font-weight:normal; margin-left:5px'>R$</label>" +
						"<label class='coffeePrice' style='font-weight:normal'>" + coffeePrice + "</label>" +
						"<input type='hidden' name='coffeeId' value='" + coffeeId + "'>" +
					"</li>";

				$("#coffees").append(removedCoffeeHtml);
				updateCartPrice()
			}
		});
	});

	function updateCartPrice() {
		$.get("http://localhost:8080/club/1/orders/user/" + userId + "/price", function(price) {
			$(".cartPrice").text(price);
		});
	}

});