local community = KEYS[1]
local user = KEYS[2]

local diff = redis.call("ZREVRANGE", community, 0, 49, "WITHSCORES")

if #diff > 0 then
	local news = {}
	for i = 1, #diff, 2 do
		table.insert(news, diff[i + 1])
		table.insert(news, diff[i])
	end
	redis.call("ZADD", user, "NX", unpack(news))
end
return true

