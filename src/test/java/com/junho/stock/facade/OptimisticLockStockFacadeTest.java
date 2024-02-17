package com.junho.stock.facade;

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

/**
 * Optimistic lock -> version을 통해 update 제어
 * 장점 : 별도의 lock 없어 성능 향상
 * 단점 : 실패 시 재시도 로직을 개발자가 작성해줘야 하는 번거로움 존재
 * 충돌이 빈번하게 일어나지 않는 경우에 추천
 * 충돌 빈번하게 일어날 경우 -> Pessimistic lock 추천
 */
@SpringBootTest
class OptimisticLockStockFacadeTest {

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

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
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optimisticLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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

/**
 * Named Lock: 이름을 가진 메타데이터 락
 * 이름 가진 락 획득 후 해제할 때 까지 다른 세션은 이 락을 획득할 수 없게 된다.
 * 주의해야 할 점 : 트랜잭션 종료 시 lock이 자동으로 해제 안되어
 * 별도 명령으로 해제하거나 선점 시간 끝나야 락 해제됨
 *
 * MySQL에서는 gap lock 명령으로 락 획득 할 수 있고
 * release lock으로 락 해제 가능
 * stock에 거는게 아니라 별도의 공간에 건
 */
