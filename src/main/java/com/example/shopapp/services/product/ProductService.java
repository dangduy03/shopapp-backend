package com.example.shopapp.services.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Favorite;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.models.User;
import com.example.shopapp.repositorys.CategoryRepository;
import com.example.shopapp.repositorys.FavoriteRepository;
import com.example.shopapp.repositorys.ProductImageRepository;
import com.example.shopapp.repositorys.ProductRepository;
import com.example.shopapp.repositorys.UserRepository;
import com.example.shopapp.responses.product.ProductResponse;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
	
    private final ProductRepository productRepository;
    
    private final UserRepository userRepository;
    
    private final CategoryRepository categoryRepository;
    
    private final ProductImageRepository productImageRepository;
    
    private final FavoriteRepository favoriteRepository;
    
    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find category with id: "+productDTO.getCategoryId()));

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long productId) throws Exception {
        Optional<Product> optionalProduct = productRepository.getDetailProduct(productId);
        if(optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new DataNotFoundException("Cannot find product with id =" + productId);
    }
    
    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId, 
                                                PageRequest pageRequest) {
        Page<Product> productsPage;
        productsPage = productRepository.searchProducts(keyword, categoryId, pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }
    
    @Override
    @Transactional
    public Product updateProduct(
            long id,
            ProductDTO productDTO
    )
            throws Exception {
        Product existingProduct = getProductById(id);
        if(existingProduct != null) {
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find category with id: "+productDTO.getCategoryId()));
            if(productDTO.getName() != null && !productDTO.getName().isEmpty()) {
                existingProduct.setName(productDTO.getName());
            }

            existingProduct.setCategory(existingCategory);
            if(productDTO.getPrice() >= 0) {
                existingProduct.setPrice(productDTO.getPrice());
            }
            
            if(productDTO.getDescription() != null &&
                    !productDTO.getDescription().isEmpty()) {
                existingProduct.setDescription(productDTO.getDescription());
            }
            
            if(productDTO.getThumbnail() != null &&
                    !productDTO.getThumbnail().isEmpty()) {
                existingProduct.setDescription(productDTO.getThumbnail());
            }
            
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
    
    @Override
    @Transactional
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find product with id: "+productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        
        int size = productImageRepository.findByProductId(productId).size();
        
        if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException(
                    "Number of images must be <= "
                    +ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        
        if (existingProduct.getThumbnail() == null ) {
            existingProduct.setThumbnail(newProductImage.getImageUrl());
        }
        
        productRepository.save(existingProduct);
        return productImageRepository.save(newProductImage);
    }

    @Override
    @Transactional
    public Product likeProduct(Long userId, Long productId) throws Exception {
        if (!userRepository.existsById(userId) || !productRepository.existsById(productId)) {
            throw new DataNotFoundException("User or product not found");
        }

        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
        } else {
            Favorite favorite = Favorite.builder()
                    .product(productRepository.findById(productId).orElse(null))
                    .user(userRepository.findById(userId).orElse(null))
                    .build();
            favoriteRepository.save(favorite);
        }
        return productRepository.findById(productId).orElse(null);
    }
    
    @Override
    @Transactional
    public Product unlikeProduct(Long userId, Long productId) throws Exception {
        if (!userRepository.existsById(userId) || !productRepository.existsById(productId)) {
            throw new DataNotFoundException("User or product not found");
        }

        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId);
            favoriteRepository.delete(favorite);
        }
        return productRepository.findById(productId).orElse(null);
    }
    
    @Override
    @Transactional
    public List<ProductResponse> findFavoriteProductsByUserId(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found with ID: " + userId);
        }

        List<Product> favoriteProducts = productRepository.findFavoriteProductsByUserId(userId);

        return favoriteProducts.stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void generateFakeLikes() throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        List<User> users = userRepository.findByRoleId(1L);
        List<Product> products = productRepository.findAll();
        final int totalRecords = 1_000;
        final int batchSize = 100;
        List<Favorite> favorites = new ArrayList<>();
        for (int i = 0; i < totalRecords; i++) {

            User user = users.get(random.nextInt(users.size()));
            Product product = products.get(random.nextInt(products.size()));

            if(!favoriteRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
                Favorite favorite = Favorite.builder()
                        .user(user)
                        .product(product)
                        .build();
                favorites.add(favorite);
            }
            if(favorites.size() >= batchSize) {
                favoriteRepository.saveAll(favorites);
                favorites.clear();
            }
        }
    }
}
