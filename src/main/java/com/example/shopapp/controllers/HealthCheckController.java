package com.example.shopapp.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopapp.models.Category;
import com.example.shopapp.responses.ResponseObject;
import com.example.shopapp.services.category.CategoryService;

import java.util.List;
import java.net.InetAddress;

@RestController
@RequestMapping("${api.prefix}/healthcheck")
@AllArgsConstructor
public class HealthCheckController {
	
    private final CategoryService categoryService;
    
    @GetMapping("/health")
    public ResponseEntity<ResponseObject> healthCheck() throws Exception{
        List<Category> categories = categoryService.getAllCategories();

        String computerName = InetAddress.getLocalHost().getHostName();
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("ok, Computer Name: " + computerName)
                .status(HttpStatus.OK)
                .build());
    }
}
