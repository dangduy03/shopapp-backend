package com.example.shopapp.services.product;

import org.springframework.data.domain.PageRequest;

import com.example.shopapp.responses.product.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IProductRedisService {
    void clear();
    
    List<ProductResponse> getAllProducts(
            String keyword,
            Long categoryId, PageRequest pageRequest) throws JsonProcessingException;
    
    void saveAllProducts(List<ProductResponse> productResponses,
                                String keyword,
                                Long categoryId,
                                PageRequest pageRequest) throws JsonProcessingException;

}
