local type = ARGV[1]
local id = ARGV[2]
local reaction = ARGV[3]
local username = ARGV[4]

local likeKey = "like:" .. type .. ":" .. id
local dislikeKey = "dislike:" .. type .. ":" .. id

if reaction == "1" then
	redis.call("SADD", likeKey, username)
	redis.call("SREM", dislikeKey, username)
elseif reaction == "-1" then
	redis.call("SADD", dislikeKey, username)
	redis.call("SREM", likeKey, username)
else
	redis.call("SREM", dislikeKey, username)
	redis.call("SREM", likeKey, username)
end

return true

