package com.example.shopapp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopapp.models.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    
    Favorite findByUserIdAndProductId(Long userId, Long productId);
}
