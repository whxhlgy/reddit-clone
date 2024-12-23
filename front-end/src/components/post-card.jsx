import LikeButton from "@/components/like-button";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { getInitials } from "@/lib/utils";
import React from "react";
import { Link, useFetcher, useParams } from "react-router-dom";

export const PostCardWrapper = React.forwardRef(({ children, postId }, ref) => {
  return (
    <div ref={ref} className="border-t-[1px] last:border-b-[1px] py-1">
      <div className="hover:bg-buttonGray-sm rounded-[--radius]">
        <Link to={`comments/${postId}`}>{children}</Link>
      </div>
    </div>
  );
});

const PostCard = React.forwardRef(({ post }, ref) => {
  const fetcher = useFetcher();

  const { communityName } = useParams();
  const onReaction = (reaction) => {
    fetcher.submit(
      { reaction, postId: post.id, intend: "like" },
      {
        method: "post",
        action: `/r/${communityName}`,
      },
    );
  };
  return (
    <PostCardWrapper postId={post.id} ref={ref}>
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
        <div className="flex justify-start z-10">
          <LikeButton
            reaction={post.reaction}
            likeCount={post.likeCount}
            onReaction={onReaction}
            size={"md"}
          />
        </div>
      </div>
    </PostCardWrapper>
  );
});

export default PostCard;
