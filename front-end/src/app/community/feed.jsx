import { getPostByCommunityName } from "@/api/post";
import PostCard from "@/components/post-card";
import { useEffect, useState } from "react";
import InfiniteScroll from "react-infinite-scroll-component";
import { useParams } from "react-router-dom";

const Feed = () => {
  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const { communityName } = useParams();
  const fetchMorePosts = async () => {
    const postData = await getPostByCommunityName(communityName, {
      page,
      size: 10,
    });
    const newPosts = postData.data;
    const pagination = postData.pagination;
    setPage(pagination.page + 1);
    setHasMore(pagination.page < pagination.totalPages - 1);
    setPosts([...posts, ...newPosts]);
  };

  useEffect(() => {
    console.log("hello");
    fetchMorePosts();
  }, []);

  return (
    <InfiniteScroll
      dataLength={posts.length}
      next={fetchMorePosts}
      hasMore={hasMore}
      loader={<p>Loading...</p>}
    >
      {posts.map((post, index) => (
        <PostCard key={post.id} post={post} />
      ))}
    </InfiniteScroll>
  );
};

export default Feed;
