package me.project.backend.util;

public class RedisKeys {
    static public String getPostViewKey(long postId){
        return "view:post:" + postId;
    }
}
