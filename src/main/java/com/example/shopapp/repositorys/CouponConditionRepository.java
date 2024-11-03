package com.example.shopapp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopapp.models.CouponCondition;
import java.util.List;

public interface CouponConditionRepository extends JpaRepository<CouponCondition, Long> {
    List<CouponCondition> findByCouponId(long couponId);
}
