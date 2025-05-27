package com.activitytracker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ActivityTracker {

    private final Map<String, Deque<Long>> userActivitiesTimestamps;

    public ActivityTracker() {
        this.userActivitiesTimestamps = new HashMap<>();
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
        while (!timestamps.isEmpty() && currentTimeMillis - timestamps.peekFirst() > seconds * 1000L) {
            timestamps.pollFirst();
        }
        return timestamps.size();
    }

    ; // returns how many actions the user performed in the last `seconds`

    public static void main(String[] args) throws InterruptedException {
        ActivityTracker activityTracker = new ActivityTracker();
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