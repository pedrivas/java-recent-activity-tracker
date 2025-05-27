package com.activitytracker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadActivityTracker {

    private final ConcurrentHashMap<String, Deque<Long>> userActivitiesTimestamps;

    public MultiThreadActivityTracker() {
        this.userActivitiesTimestamps = new ConcurrentHashMap<>();
    }

    public void record(String userId) {
        long currentTimeMillis = System.currentTimeMillis();
        userActivitiesTimestamps.putIfAbsent(userId, new ArrayDeque<>());
        Deque<Long> timestamps = userActivitiesTimestamps.get(userId);
        synchronized (timestamps) {
            timestamps.addLast(currentTimeMillis);

        }
        System.out.println("Recorded activity for user: " + userId + " at " + currentTimeMillis);
    }

    ; // called every time a user does something

    public int countRecent(String userId, int seconds) {
        long currentTimeMillis = System.currentTimeMillis();
        Deque<Long> timestamps = userActivitiesTimestamps.getOrDefault(userId, new ArrayDeque<>());
        synchronized (timestamps) {
            while (!timestamps.isEmpty() && currentTimeMillis - timestamps.peekFirst() > seconds * 1000L) {
                timestamps.pollFirst();
            }
            return timestamps.size();
        }
    }

    ; // returns how many actions the user performed in the last `seconds`

    public static void main(String[] args) throws InterruptedException {
        MultiThreadActivityTracker activityTracker = new MultiThreadActivityTracker();
        int threadCount = 10;
        int actionsPerThread = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(
                    () -> {
                        for (int j = 0; j < actionsPerThread; j++) {
                            activityTracker.record("user1");
                        }
                    }
            );
        }

        executorService.shutdown();
        boolean finished = executorService.awaitTermination(1, TimeUnit.MINUTES);

        int expected = threadCount * actionsPerThread;
        int actual = activityTracker.countRecent("user1", 60);

        System.out.println(actual);

    }

}