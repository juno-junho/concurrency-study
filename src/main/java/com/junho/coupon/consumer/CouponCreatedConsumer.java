package com.junho.coupon.consumer;

import com.junho.coupon.domain.Coupon;
import com.junho.coupon.repository.CouponRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;

    public CouponCreatedConsumer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {
//        System.out.println("userId = " + userId);
        couponRepository.save(new Coupon(userId));
    }

}
