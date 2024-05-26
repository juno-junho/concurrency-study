package com.junho.stock.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

public class AtomicIntegerTest {

    private Integer integer = 0;
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    void atomic_integer_test() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> atomicInteger.incrementAndGet());
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("atomicInteger = " + atomicInteger); // 1000
        assertThat(threadCount).isEqualTo(atomicInteger.get());
    }

    @Test
    void testRegularInteger() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> this.integer++);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("integer = " + integer); // 990
        assertThat(threadCount).isNotEqualTo(integer);
    }

}
