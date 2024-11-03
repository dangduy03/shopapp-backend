package com.example.shopapp.services.product.image;

import com.example.shopapp.models.ProductImage;

public interface IProductImageService {
    ProductImage deleteProductImage(Long id) throws Exception;
}
