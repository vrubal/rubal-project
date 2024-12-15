package com.rubal.transformers.schedulers;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;

@SpringBootApplication
public class TestScheduledExecutor {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        Runnable beep = ()->{
            System.out.println("beep");
            //return true;
        };
        final ScheduledFuture<?> beephandler = scheduledExecutorService.scheduleWithFixedDelay(beep, 2,2, TimeUnit.SECONDS);
        System.out.println(beephandler.get());
    }
}
