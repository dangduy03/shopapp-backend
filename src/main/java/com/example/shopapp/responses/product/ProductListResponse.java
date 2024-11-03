package com.example.shopapp.responses.product;

import java.util.List;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class ProductListResponse {
	
	private List<ProductResponse> products;
	
	private int totalPages;

}
