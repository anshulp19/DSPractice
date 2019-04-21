/*
    https://leetcode.com/problems/design-twitter/

    Design a simplified version of Twitter where users can post tweets,
    follow/unfollow another user and is able to see the 10 most recent tweets
    in the user's news feed. Your design should support the following methods:

        1. postTweet(userId, tweetId): Compose a new tweet.
        2. getNewsFeed(userId): Retrieve the 10 most recent tweet ids in the user's
            news feed. Each item in the news feed must be posted by users who the user followed or by
            the user herself. Tweets must be ordered from most recent to least recent.
        3. follow(followerId, followeeId): Follower follows a followee.
        4. unfollow(followerId, followeeId): Follower unfollows a followee.


        Example:

        Twitter twitter = new Twitter();

// User 1 posts a new tweet (id = 5).
        twitter.postTweet(1, 5);

// User 1's news feed should return a list with 1 tweet id -> [5].
        twitter.getNewsFeed(1);

// User 1 follows user 2.
        twitter.follow(1, 2);

// User 2 posts a new tweet (id = 6).
        twitter.postTweet(2, 6);

// User 1's news feed should return a list with 2 tweet ids -> [6, 5].
// Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
        twitter.getNewsFeed(1);

// User 1 unfollows user 2.
        twitter.unfollow(1, 2);

// User 1's news feed should return a list with 1 tweet id -> [5],
// since user 1 is no longer following user 2.
        twitter.getNewsFeed(1);


            * Your Twitter object will be instantiated and called as such:
            * Twitter obj = new Twitter();
            * obj.postTweet(userId,tweetId);
            * List<Integer> param_2 = obj.getNewsFeed(userId);
            * obj.follow(followerId,followeeId);
            * obj.unfollow(followerId,followeeId);
*/

import java.util.*;

public class Twitter {
    private static int timeStamp = 0;
    private Map<Integer, User> userMap;

    private class Tweet {
        public int id;
        public int time;
        public String msg;
        public Tweet next;

        public Tweet(int id) {
            this.id = id;
            time = timeStamp++;
            msg = null;
            next = null;
        }
    }

    public class User {
        public int id;
        public Set<Integer> followed;
        public Tweet tweet_head;

        public User(int id) {
            this.id = id;
            followed = new HashSet<>();
            follow(id);
            tweet_head = null;
        }

        public void follow(int id) {
            followed.add(id);
        }

        public void unfollow(int id) {
            followed.remove(id);
        }

        public void post(int id) {
            Tweet t = new Tweet(id);
            t.next = tweet_head;
            tweet_head = t;
        }
    }

    /** Initialize your data structure here. */
    public Twitter() {
        userMap = new HashMap<Integer, User>();
    }

    /** Compose a new tweet. */
    public void postTweet(int userId, int tweetId) {
        if (!userMap.containsKey(userId)) {
            User u = new User(userId);
            userMap.put(userId, u);
        }
        userMap.get(userId).post(tweetId);
    }

    /** Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent. */
    public List<Integer> getNewsFeed(int userId) {
        List<Integer> res = new LinkedList<>();

        if (!userMap.containsKey(userId))
            return res;

        Set<Integer> users = userMap.get(userId).followed;
        PriorityQueue<Tweet> pq = new PriorityQueue<>(users.size(), (a, b) -> (b.time - a.time));

        for (int user: users) {
            Tweet t = userMap.get(user).tweet_head;
            if (t != null)
                pq.add(t);
        }
        int n = 0;
        while (!pq.isEmpty() && n < 10) {
            Tweet t = pq.poll();
            res.add(t.id);
            if (t.next != null)
                pq.add(t.next);
        }

        return res;
    }

    /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
    public void follow(int followerId, int followeeId) {
        if (!userMap.containsKey(followerId)) {
            User u = new User(followerId);
            userMap.put(followerId, u);
        }
        if (!userMap.containsKey(followeeId)) {
            User u = new User(followeeId);
            userMap.put(followeeId, u);
        }
        userMap.get(followerId).follow(followeeId);

    }

    /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
    public void unfollow(int followerId, int followeeId) {
        if (!userMap.containsKey(followerId) || followeeId == followerId)
            return;
        userMap.get(followerId).unfollow(followeeId);

    }
}
