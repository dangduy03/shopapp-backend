package com.example.shopapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ResponseObject;
import com.example.shopapp.services.product.ProductService;
import com.example.shopapp.services.product.image.IProductImageService;
import com.example.shopapp.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/product_images")
//@Validated
//Dependency Injection
@RequiredArgsConstructor
public class ProductImageController {
	
    private final IProductImageService productImageService;
    
    private final ProductService productService;
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> delete(
            @PathVariable Long id
    ) throws Exception {
        ProductImage productImage = productImageService.deleteProductImage(id);
        if(productImage != null){
            FileUtils.deleteFile(productImage.getImageUrl());
        }
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Delete product image successfully")
                        .data(productImage)
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
