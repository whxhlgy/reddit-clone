import classNames from "classnames";
import { Button } from "@/components/ui/button";
import { ArrowBigDown, ArrowBigUp } from "lucide-react";

const LikeButton = ({ post, className }) => {
  const reaction = null;
  return (
    <div
      className={classNames(
        className,
        "rounded-[--radius] flex items-center space-x-1",
        {
          "bg-[#FF4500] text-white": reaction === "like",
          "bg-[#1E90FF] text-white": reaction === "dislike",
          "bg-primary": reaction === null,
        },
      )}
    >
      <Button
        variant={classNames({
          ghost_red: reaction === "like",
          ghost_blue: reaction === "dislike",
          ghost_deeper: reaction == null,
        })}
        size="icon"
      >
        <ArrowBigUp
          className={classNames({
            "fill-white": reaction === "like",
          })}
        />
      </Button>
      <span className="text-xs">{post.likesCount}</span>
      <Button
        variant={classNames({
          ghost_red: reaction === "like",
          ghost_blue: reaction === "dislike",
          ghost_deeper: reaction == null,
        })}
        size="icon"
      >
        <ArrowBigDown
          className={classNames({
            "fill-white": reaction === "dislike",
          })}
        />
      </Button>
    </div>
  );
};

export default LikeButton;
