package com.example.shopapp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shopapp.models.Coupon;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String couponCode);
}
