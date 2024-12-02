import PostCard, { PostCardWrapper } from "@/components/post-card";

const Feed = ({ posts }) => {
  console.log(posts);
  return (
    <div>
      {posts.map((post) => (
        <PostCard key={post.id} post={post} />
      ))}
    </div>
  );
};

export default Feed;
