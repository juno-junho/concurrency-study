package com.junho.stock.service;

import com.junho.stock.domain.Stock;
import com.junho.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    public PessimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decreaseStock(Long id, Long quantity) {
        // stock 조회
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);

        // 재고 감소
        stock.decreaseQuantity(quantity);

        // 갱신 값 저장
        stockRepository.saveAndFlush(stock);
    }

}
