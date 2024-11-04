package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
	
	@Min(value = 1, message = "Order's ID must be > 0")
	@JsonProperty("order_id")
	private Long orderId;
	
	@Min(value = 1, message = "Product's ID must be > 0")
	@JsonProperty("product_id")
	private Long productId;
	
	@Min(value = 0, message = "Price's ID must be >= 0")
	@JsonProperty("price")
	private Float price;
	
	@Min(value = 1, message = "number_of_productsmust be >= 1")
	@JsonProperty("number_of_products")
	private int numberOfProducts;
	
	@Min(value = 0, message = "total_money must be >= 0")
	@JsonProperty("total_money")
	private Float totalMoney;
	
	@JsonProperty("color")
	private String color;
}
