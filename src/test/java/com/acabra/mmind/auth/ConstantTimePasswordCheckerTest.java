package com.acabra.mmind.auth;


import com.acabra.mmind.UtilsTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LongSummaryStatistics;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class ConstantTimePasswordCheckerTest {
    private Random random = new Random();

    @Test
    public void avgTimeChecking() {
        LongSummaryStatistics lss = new LongSummaryStatistics();
        String[] passwords = UtilsTest.readPasswordsFromFile("timecheck_test_input.txt");
        int bound = passwords.length;
        for (int i = 0; i < 1000; ++i) {
            String p1 = passwords[random.nextInt(bound)];
            String p2 = passwords[random.nextInt(bound)];
            long t1 = System.nanoTime();
            ConstantTimePasswordChecker.check(p1, p2);
            lss.accept(System.nanoTime() - t1);
        }
        long averageMillis = TimeUnit.NANOSECONDS.toMillis(Double.valueOf(lss.getAverage()).longValue());
        System.out.println("millis ->" + averageMillis);
        System.out.println("nano ->" + lss.getAverage());
        Assertions.assertThat(averageMillis).isCloseTo(200L, Assertions.within(5L));
    }

}