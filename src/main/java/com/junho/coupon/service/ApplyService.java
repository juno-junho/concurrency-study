package com.junho.coupon.service;

import com.junho.coupon.domain.Coupon;
import com.junho.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;

    public ApplyService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    /**
     * 여러 thread에서 접근 시,
     * coupon이 발급되는 개수 확인 시점 / 쿠폰 발급 시점 차이 발생에 따른 정합성 깨짐
     * -> race condition 발생
     */
    public void apply(Long userId) {
        // coupon 조회
        long count = couponRepository.count();

        // 100개 이상 발급하지 않도록 제한
        if (count > 100) {
            return;
        }

        // 쿠폰 발급
        couponRepository.save(new Coupon(userId));
    }

}
