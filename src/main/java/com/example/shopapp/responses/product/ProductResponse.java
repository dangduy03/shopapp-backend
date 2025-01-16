package com.example.shopapp.responses.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.shopapp.models.Comment;
import com.example.shopapp.models.Favorite;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.BaseResponse;
import com.example.shopapp.responses.comment.CommentResponse;
import com.example.shopapp.responses.favorite.FavoriteResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse {
	
    private Long id;
    
    private String name;
    
    private Float price;
    
    private String thumbnail;
    
    private String description;

    private int totalPages;

    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();

    @JsonProperty("comments")
    private List<CommentResponse> comments = new ArrayList<>();

    @JsonProperty("favorites")
    private List<FavoriteResponse> favorites = new ArrayList<>();

    @JsonProperty("category_id")
    private Long categoryId;
    
    public static ProductResponse fromProduct(Product product) {
        List<Comment> comments = product.getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .collect(Collectors.toList());
        
        List<Favorite> favorites = product.getFavorites();
        
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .comments(comments.stream().map(CommentResponse::fromComment).toList())
                .favorites(favorites.stream().map(FavoriteResponse::fromFavorite).toList())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .productImages(product.getProductImages())
                .totalPages(0)
                .build();
        
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
