import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { getInitials } from "@/lib/utils";
import classNames from "classnames";
import { ArrowBigDown, ArrowBigUp } from "lucide-react";

export const PostCardWrapper = ({ children }) => {
  return (
    <div className="border-t-[1px] last:border-b-[1px] py-1">
      <div className="hover:bg-accent1 rounded-[--radius]">{children}</div>
    </div>
  );
};

const PostCard = ({ post }) => {
  return (
    <PostCardWrapper>
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
          <div
            className={classNames(
              "rounded-[--radius] flex items-center space-x-1",
              {
                "bg-[#FF4500] text-white": post.reaction === "like",
                "bg-[#1E90FF] text-white": post.reaction === "dislike",
                "bg-accent ": post.reaction === null,
              },
            )}
          >
            <Button
              variant={classNames({
                ghost_red: post.reaction === "like",
                ghost_blue: post.reaction === "dislike",
                ghost_deeper: post.reaction == null,
              })}
              size="icon"
            >
              <ArrowBigUp
                className={classNames({
                  "fill-white": post.reaction === "like",
                })}
              />
            </Button>
            <span className="text-xs">{post.likesCount}</span>
            <Button
              variant={classNames({
                ghost_red: post.reaction === "like",
                ghost_blue: post.reaction === "dislike",
                ghost_deeper: post.reaction == null,
              })}
              size="icon"
            >
              <ArrowBigDown
                className={classNames({
                  "fill-white": post.reaction === "dislike",
                })}
              />
            </Button>
          </div>
        </div>
      </div>
    </PostCardWrapper>
  );
};

export default PostCard;
