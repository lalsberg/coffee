$(function() {

	if(Cookies.get('club') != undefined) {
		var clubId = jQuery.parseJSON(Cookies.get('club')).id;
	
		getCurrentOrder(clubId, function(order) {
			if(order != "") {
				//TODO tudo dentro usa clubId + userId. posso usar apenas o orderId
				showCurrentOrder();
			} else {
				$(".no-order").show();
			}
		});

		function showCurrentOrder() {
			$.get("http://localhost:8080/club/" + clubId + "/orders/user/me", function(userOrder) {
				var itemsHtml = "";
				$(userOrder.coffees).each(function() {
					itemsHtml += 
						"<li class='list-group-item'>" +
							"<input type='text' name='coffeeQuantity' value='" + this.quantity + "'></input> " +
							"<label class='coffeeName' style='font-weight:normal'>" + this.coffee.name + " </label>" +
							"<label style='font-weight:normal; margin-left: 5px'>R$ </label>" +
							"<label class='coffeePrice' style='font-weight:normal'>" + this.coffee.price + "<a href='#'><span class='label label-danger' style='margin-left:5px'>X</span></a></label>" +
							"<input type='hidden' name='coffeeId' value='" + this.coffee.id + "'>" +
						"</li>";
				});
		
				$("#orderedCoffees").append(itemsHtml);
				$(".cartPrice").text(userOrder.price);
			});
		
			$.get("http://localhost:8080/club/" + clubId + "/orders/user/me/unorderedCoffees", function(coffees) {
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

			$(".user-order-panel").show();
		}

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
	
			$.ajax("http://localhost:8080/club/" + clubId + "/orders/user/me", { 
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
							"<label class='coffeePrice' style='font-weight:normal'>" + coffeePrice + "<a href='#'><span class='label label-danger' style='margin-left:5px'>X</span></a></label>" +
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
			var coffeePrice = $(this).find(".coffeePrice").text().substring(0, $(this).find(".coffeePrice").text().length -1);
			var coffeeQuantity = coffeeQuantityInput.val();
	
			var coffeeOrder = {
				coffee: {
					id: coffeeId
				},
				quantity: coffeeQuantity
			};
	
			$.ajax("http://localhost:8080/club/" + clubId + "/orders/user/me", { 
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
	
		$("#orderedCoffees").on("click", ".label-danger", function() {
			var removeButton = $(this);
			var coffeeId = removeButton.parent().parent().siblings("input[name='coffeeId']").val();
			var coffeeName = $(this).parent().parent().siblings(".coffeeName").text();
			var coffeePrice = $(this).parent().parent().text().substring(0, $(this).parent().parent().text().length -1);
			var coffeeQuantity = $(this).parent().parent().siblings("input[name='coffeeQuantity']").val();
	
			$.ajax("http://localhost:8080/club/" + clubId + "/orders/user/me/coffee/" + coffeeId, {
				type : 'DELETE',
				success: function() {
					removeButton.parent().parent().parent().remove();
	
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
			$.get("http://localhost:8080/club/" + clubId + "/orders/user/me/price", function(price) {
				$(".cartPrice").text(price);
			});
		}

		$(".btn-init-order").click(function() {
			$.ajax({
				url: "http://localhost:8080/clubs/" + clubId + "/orders",
				type: "PUT",
				success: function(order) {
					$(".no-order").hide();
					showCurrentOrder();
				}
			});
		});

	} else {
		$(".no-club").show();
	}
});

function getCurrentOrder(clubId, callback) {
	$.get("http://localhost:8080/clubs/" + clubId + "/order", function(order) {
		callback(order);
	});
}
