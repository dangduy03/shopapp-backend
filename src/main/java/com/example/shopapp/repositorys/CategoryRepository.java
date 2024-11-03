package com.example.shopapp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shopapp.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
}


