package com.junho.stock.study;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;

public class ConcurrencyHashMapTest {

    private final Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> regularHashMap = new HashMap<>();

    @Test
    void concurrent_hashmap_test() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < threadCount; i++) {
            final String key = "element: " + i;
            final int value = i;
            executorService.execute(() -> concurrentHashMap.put(key, value));
        }
        executorService.shutdown();
        executorService.awaitTermination(1, MINUTES);

        System.out.println("concurrentHashMap = " + concurrentHashMap);
        System.out.println("concurrentHashMap.size() = " + concurrentHashMap.size()); // 1000
        assertThat(threadCount).isEqualTo(concurrentHashMap.size());
    }

    @Test
    void regular_hashmap_test() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < threadCount; i++) {
            final String key = "element: " + i;
            final int value = i;
            executorService.execute(() -> regularHashMap.put(key, value));
        }
        executorService.shutdown();
        executorService.awaitTermination(1, MINUTES);

        System.out.println("regularHashMap = " + regularHashMap);
        System.out.println("regularHashMap.size() = " + regularHashMap.size()); // 993
        assertThat(threadCount).isNotEqualTo(regularHashMap.size());
    }

}
