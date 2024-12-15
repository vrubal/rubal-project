package com.rubal.transformers.concurrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class MyCompletableFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException, FileNotFoundException {

        ExecutorService service = Executors.newFixedThreadPool(5);
        Future<List<Integer>> future = service.submit(()->{
            System.out.println("In future");
            return Arrays.asList(1,2,3,4);
        });
        future.get();
        service.shutdown();
        CompletableFuture<List<Integer>> completableFuture = CompletableFuture.supplyAsync(()-> Arrays.asList(1,2,3,4,5));
        //completableFuture.get();
        //completableFuture.complete(Arrays.asList(1,2,3,4));

        List<Integer> list = completableFuture.get();
        List<Integer> integers1 = CompletableFuture.supplyAsync(() -> list.stream().map(e -> e + 1).

                collect(Collectors.toList())).thenApply(integers -> integers.stream().map(a -> a * 6).collect(Collectors.toList())).get();

        list.stream().forEach(System.out::println);
        integers1.stream().forEach(System.out::println);    
        //completableFuture.thenAccept();
    }
}
