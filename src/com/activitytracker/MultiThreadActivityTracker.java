package com.activitytracker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

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
        synchronized (timestamps){
            while (!timestamps.isEmpty() && currentTimeMillis - timestamps.peekFirst() > seconds * 1000L) {
                timestamps.pollFirst();
            }
            return timestamps.size();
        }
    }

    ; // returns how many actions the user performed in the last `seconds`

    public static void main(String[] args) throws InterruptedException {
        MultiThreadActivityTracker activityTracker = new MultiThreadActivityTracker();
        activityTracker.record("user1");
        activityTracker.record("user1");
        activityTracker.record("user1");
        Thread.sleep(2000); // Simulate a delay of 2 seconds
        activityTracker.record("user2");
        activityTracker.record("user2");
        activityTracker.record("user2");
        Thread.sleep(2000); // Simulate a delay of 2 seconds
        System.out.println(activityTracker.countRecent("user1", 4));
        System.out.println(activityTracker.countRecent("user2", 5));
    }

}