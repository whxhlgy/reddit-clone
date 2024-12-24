import { getPostByCommunityName } from "@/api/post";
import PostCard from "@/components/post-card";
import { useState } from "react";
import InfiniteScroll from "react-infinite-scroll-component";
import { useParams } from "react-router-dom";

const Feed = ({ posts: _posts }) => {
  const [posts, setPosts] = useState(_posts);
  const [page, setPage] = useState(1);
  const { communityName } = useParams();
  const fetchMorePosts = async () => {
    const newPosts = await getPostByCommunityName(communityName, {
      page,
      size: 10,
    });
    setPage(page + 1);
    setPosts([...posts, ...newPosts]);
  };

  return (
    <InfiniteScroll
      dataLength={posts.length}
      next={fetchMorePosts}
      hasMore={true}
      loader={<p>Loading...</p>}
    >
      {posts.map((post, index) => (
        <PostCard key={post.id} post={post} />
      ))}
    </InfiniteScroll>
  );
};

export default Feed;
