import PostCard from "@/components/post-card";

const Feed = ({ posts }) => {
  return (
    <div className="w-full">
      {posts.map((post) => (
        <PostCard key={post.id} post={post} />
      ))}
    </div>
  );
};

export default Feed;
