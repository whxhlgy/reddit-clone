import LikeButton from "@/components/like-button";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { getInitials } from "@/lib/utils";
import { Link } from "react-router-dom";

export const PostCardWrapper = ({ children, postId }) => {
  return (
    <div className="border-t-[1px] last:border-b-[1px] py-1">
      <div className="hover:bg-accent1 rounded-[--radius]">
        <Link to={`comments/${postId}`}>{children}</Link>
      </div>
    </div>
  );
};

const PostCard = ({ post }) => {
  return (
    <PostCardWrapper postId={post.id}>
      <div className="rounded-xl flex flex-col">
        {/* Header */}
        <div className="flex flex-row items-center gap-2">
          <Avatar>
            <AvatarFallback>{getInitials(post.username)}</AvatarFallback>
          </Avatar>
          <p className="text-sm">{post.username}</p>
        </div>

        {/* Content */}
        <div>
          <p className="font-bold text-lg">{post.title}</p>
          <p>{post.content}</p>
        </div>

        {/* Footer */}
        <div className="flex justify-start">
          <LikeButton post={post} />
        </div>
      </div>
    </PostCardWrapper>
  );
};

export default PostCard;
