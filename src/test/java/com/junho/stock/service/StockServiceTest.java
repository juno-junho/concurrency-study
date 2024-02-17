package com.junho.stock.service;

import com.junho.stock.domain.Stock;
import com.junho.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        stockRepository.save(new Stock(1L, 100L));
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAllInBatch();
    }

    @Test
    void  재고감소() {
        // Given
        stockService.decrease(1L, 1L);

        // When
        Stock stock = stockRepository.findById(1L).orElseThrow();

        // Then
        assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @Test
    // 실패하는 이유 : 동시성 문제. race condition 발생
    // service의 decreaseStock 메서드에 synchronized 키워드를 추가해도 실패 -> @Transactional 프록시 때문
    // transaction commit 되기 전에 호출 가능. -> @Transactional 없애면 성공함
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }

            });

        }
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertThat(stock.getQuantity()).isEqualTo(0L);

    }


}
