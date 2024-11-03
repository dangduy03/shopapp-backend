package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	@NotBlank(message = "Title is	 required")
	@Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
	private String name;

	@Min(value = 0, message = "Price  must be greater than or equal to 0")
	@Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
	private Float price;

	private String thumbnail;

	private String description;

	@NotNull(message = "Category ID is required ")
	@JsonProperty("category_id")
	private Long categoryId;

//	@NotNull(message = "Color ID is required ")
//	@JsonProperty("color_id")
//	private Long colorId;


}
