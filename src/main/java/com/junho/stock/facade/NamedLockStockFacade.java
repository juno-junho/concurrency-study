package com.junho.stock.facade;

import com.junho.stock.repository.LockRepository;
import com.junho.stock.service.StockService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NamedLockStockFacade {

    private final LockRepository lockRepository;
    private final StockService stockService;

    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        String lockKey = id.toString();
        try{
            lockRepository.getLock(lockKey);
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(lockKey);
        }
    }


}
