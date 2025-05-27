# 🚀 Problem: Recent Activity Tracker
Scenario:
You are building a backend service to track recent user activity. Each time a user performs an action, you log it with a timestamp. The system should be able to answer:

"How many actions has this user performed in the last N seconds?"

## 🧩 Requirements:
Implement a class ActivityTracker with the following methods:

```java
void record(String userId); // called every time a user does something
int countRecent(String userId, int seconds); // returns how many actions the user performed in the last `seconds`
```
## 💡 Constraints:
All timestamps are based on System.currentTimeMillis().

You can assume record() and countRecent() are called frequently.

Optimize for time and space.

Multiple users are supported.

Bonus: make it thread-safe (optional for now).

## ⏱️ Example:
```java
ActivityTracker tracker = new ActivityTracker();

tracker.record("u1"); // t = 1000
Thread.sleep(1000);
tracker.record("u1"); // t = 2000
Thread.sleep(1000);
tracker.record("u1"); // t = 3000

tracker.countRecent("u1", 2); // should return 2 (last 2 seconds: t=2000 and t=3000)
tracker.countRecent("u1", 5); // should return 3
```