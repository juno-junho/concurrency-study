package com.junho.stock.facade;

import com.junho.stock.service.StockService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantLock;

@Component
public class ReentrantLockStockFacade {

    private final StockService stockService;

    public ReentrantLockStockFacade(StockService stockService) {
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long id, Long quantity) throws InterruptedException {
        final ReentrantLock lock = new ReentrantLock();

        boolean lockAcquired = false;

        while (!lockAcquired) {
            lockAcquired = lock.tryLock();
            if (!lockAcquired) {
                Thread.sleep(100);
            }
        }
        try {
            stockService.decrease(id, quantity);
        } finally {
            lock.unlock();
        }

        // lock 시도 -> 실패시 재시도
//        lock.lock();
//        try {
//            while (!condition.)
//            stockService.decrease(id, quantity);
//            lock.lock();
//            try {
//                stockService.decrease(id, quantity);
//            }finally {
//                lock.unlock();
//            }
//        } finally {
//            lock.unlock();
//        }
//        while (lock.tryLock()) {
//            try {
//                lock.lock();
//                stockService.decrease(id, quantity);
//                break;
//            } catch (Exception e) {
//                // retry
//                Thread.sleep(50);
//            }finally {
//                lock.unlock();
//            }
//        }
    }

}
