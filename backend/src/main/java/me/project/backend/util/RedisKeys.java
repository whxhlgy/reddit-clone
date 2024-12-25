package me.project.backend.util;

public class RedisKeys {
    static public String getPostViewKey(long postId){
        return "view:post:" + postId;
    }

    static public String getPostKey(long postId){
        return "cache:post:" + postId;
    }

    static public String getTimelineKey(String username) {
        return "timeline:" + username;
    }

    static public String getCommTimelineKey(String communityName) {
        return "CommTimeline:" + communityName;
    }
}
