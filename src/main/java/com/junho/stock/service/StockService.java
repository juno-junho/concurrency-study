package com.junho.stock.service;

import com.junho.stock.domain.Stock;
import com.junho.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

//    @Transactional
//    public synchronized void decreaseStock(Long id, Long quantity) {
//        // stock 조회
//        Stock stock = stockRepository.findById(id).orElseThrow();
//
//        // 재고 감소
//        stock.decreaseQuantity(quantity);
//
//        // 갱신 값 저장
//        stockRepository.saveAndFlush(stock);
//    }

    @Transactional(propagation = REQUIRES_NEW) // + connection pool size 변경
    public void decrease(Long id, Long quantity) {
        // stock 조회
        Stock stock = stockRepository.findById(id).orElseThrow();

        // 재고 감소
        stock.decreaseQuantity(quantity);

        // 갱신 값 저장
        stockRepository.saveAndFlush(stock);
    }

}

/**
 * Synchronized 문제점 :
 * 하나의 프로세스 안에서만 mutual exclusion 보장 -> 여러 스레드에서 접근하면 실패함.
 * 서버가 한대일 때는 데이터 접근을 한대만 해서 괜찮지만
 * 서버가 2대 이상일 경우 데이터 접근 여러곳에서 가능함.
 * -> mysql이 지원해주는 방법으로 해결하기
 */
