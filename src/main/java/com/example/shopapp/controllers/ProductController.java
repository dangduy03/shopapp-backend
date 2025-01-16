package com.example.shopapp.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.shopapp.components.LocalizationUtils;
import com.example.shopapp.components.SecurityUtils;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.User;
import com.example.shopapp.responses.ResponseObject;
import com.example.shopapp.responses.product.ProductListResponse;
import com.example.shopapp.responses.product.ProductResponse;
import com.example.shopapp.services.MinioService;
import com.example.shopapp.services.product.IProductRedisService;
import com.example.shopapp.services.product.IProductService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
    private final IProductService productService;
    
    private final LocalizationUtils localizationUtils;
    
    private final IProductRedisService productRedisService;
    
    private final SecurityUtils securityUtils;
	
	@Autowired
    private MinioService minioService;

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String response = minioService.uploadFile(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/upload-multiple/{id}")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files,@PathVariable("id") Long productId ) {
        try {
            List<String> response = minioService.uploadFiles(files,productId);
            return ResponseEntity.ok("Uploaded files: " + String.join(", ", response));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    	
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) throws Exception {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(String.join("; ", errorMessages))
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }
        Product newProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Create new product successfully")
                        .status(HttpStatus.CREATED)
                        .data(newProduct)
                        .build());
    }
	
    @GetMapping("")
    public ResponseEntity<ResponseObject> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) throws JsonProcessingException {
        int totalPages = 0;
        //productRedisService.clear();
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );
        logger.info(String.format("keyword = %s, category_id = %d, page = %d, limit = %d",
                keyword, categoryId, page, limit));
        List<ProductResponse> productResponses = productRedisService
                .getAllProducts(keyword, categoryId, pageRequest);
        if (productResponses!=null && !productResponses.isEmpty()) {
            totalPages = productResponses.get(0).getTotalPages();
        }
        if(productResponses == null) {
            Page<ProductResponse> productPage = productService
                    				.getAllProducts(keyword, categoryId, pageRequest);
            totalPages = productPage.getTotalPages();
            productResponses = productPage.getContent();

            for (ProductResponse product : productResponses) {
                product.setTotalPages(totalPages);
            }
            productRedisService.saveAllProducts(
                    productResponses,
                    keyword,
                    categoryId,
                    pageRequest
            );
        }
        ProductListResponse productListResponse = ProductListResponse
                .builder()
                .products(productResponses)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get products successfully")
                .status(HttpStatus.OK)
                .data(productListResponse)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProductById(
            @PathVariable("id") Long productId
    ) throws Exception {
        Product existingProduct = productService.getProductById(productId);
        return ResponseEntity.ok(ResponseObject.builder()
                        .data(ProductResponse.fromProduct(existingProduct))
                        .message("Get detail product successfully")
                        .status(HttpStatus.OK)
                .build());

    }
    
    @GetMapping("/by-ids")
    public ResponseEntity<ResponseObject> getProductsByIds(@RequestParam("ids") String ids) {

        List<Long> productIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        
        List<Product> products = productService.findProductsByIds(productIds);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(products.stream().map(product -> ProductResponse.fromProduct(product)).toList())
                .message("Get products successfully")
                .status(HttpStatus.OK)
                .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation
    (security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(null)
                .message(String.format("Product with id = %d deleted successfully", id))
                .status(HttpStatus.OK)
                .build());
    }
    //@PostMapping("/generateFakeProducts")
    private ResponseEntity<ResponseObject> generateFakeProducts() throws Exception {
        Faker faker = new Faker();
        for (int i = 0; i < 1_000_000; i++) {
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 90_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long)faker.number().numberBetween(2, 5))
                    .build();
            productService.createProduct(productDTO);
        }
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Insert fake products succcessfully")
                        .data(null)
                        .status(HttpStatus.OK)
                .build());
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<ResponseObject> updateProduct(
            @PathVariable long id,
            @RequestBody ProductDTO productDTO) throws Exception {
        Product updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(updatedProduct)
                .message("Update product successfully")
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/like/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> likeProduct(@PathVariable Long productId) throws Exception {
        User loginUser = securityUtils.getLoggedInUser();
        Product likedProduct = productService.likeProduct(loginUser.getId(), productId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(ProductResponse.fromProduct(likedProduct))
                .message("Like product successfully")
                .status(HttpStatus.OK)
                .build());
    }
    
    @PostMapping("/unlike/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> unlikeProduct(@PathVariable Long productId) throws Exception {
        User loginUser = securityUtils.getLoggedInUser();
        Product unlikedProduct = productService.unlikeProduct(loginUser.getId(), productId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(ProductResponse.fromProduct(unlikedProduct))
                .message("Unlike product successfully")
                .status(HttpStatus.OK)
                .build());
    }
    
    @PostMapping("/favorite-products")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> findFavoriteProductsByUserId() throws Exception {
        User loginUser = securityUtils.getLoggedInUser();
        List<ProductResponse> favoriteProducts = productService.findFavoriteProductsByUserId(loginUser.getId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(favoriteProducts)
                .message("Favorite products retrieved successfully")
                .status(HttpStatus.OK)
                .build());
    }
    
    @PostMapping("/generateFakeLikes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> generateFakeLikes() throws Exception {
        productService.generateFakeLikes();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Insert fake likes succcessfully")
                .data(null)
                .status(HttpStatus.OK)
                .build());
    }
}
