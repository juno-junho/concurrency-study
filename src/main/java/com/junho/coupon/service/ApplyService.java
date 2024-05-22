package com.junho.coupon.service;

import com.junho.coupon.domain.Coupon;
import com.junho.coupon.producer.CouponCreateProducer;
import com.junho.coupon.repository.CouponCountRepository;
import com.junho.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;

    // kafka
    private final CouponCreateProducer couponCreateProducer;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
    }

    /**
     * 여러 thread에서 접근 시,
     * coupon이 발급되는 개수 확인 시점 / 쿠폰 발급 시점 차이 발생에 따른 정합성 깨짐
     * -> race condition 발생
     */
    public void apply(Long userId) {
        // coupon 조회
//        long count = couponRepository.count();
        Long count = couponCountRepository.increment();

        // 100개 이상 발급하지 않도록 제한
        if (count > 100) {
            return;
        }

        // 쿠폰 발급
//        couponRepository.save(new Coupon(userId));
        couponCreateProducer.create(userId);
    }

}
