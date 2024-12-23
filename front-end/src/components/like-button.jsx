import classNames from "classnames";
import { ArrowBigDown, ArrowBigUp } from "lucide-react";
import { useState } from "react";

const LikeButton = ({ reaction, likeCount, onReaction, size }) => {
  const [isLike, setIsLike] = useState(reaction);
  const [count, setCount] = useState(likeCount);
  console.log(`reaction: ${isLike}`);
  const onClickReaction = (newReaction) => {
    return (e) => {
      e.preventDefault();
      onReaction(newReaction);
      setIsLike(newReaction);
      setCount(count - isLike + newReaction);
    };
  };
  const like = onClickReaction(1);
  const dislike = onClickReaction(-1);
  const noneLike = onClickReaction(0);
  let buttonSize;
  switch (size) {
    case "sm":
      buttonSize = 24;
      break;
    case "md":
    default:
      buttonSize = 24;
      break;
  }

  return (
    <div
      className={classNames(
        "rounded-[--radius] flex items-center space-x-2",
        {
          "bg-buttonRed-normal text-white": size === "md" && isLike === 1,
          "bg-buttonBlue-normal text-white": size === "md" && isLike === -1,
          "bg-primary": size === "md" && isLike === 0,
        },
        {
          "bg-feed-background": size === "sm",
        },
      )}
    >
      <Button
        onClick={isLike === 1 ? noneLike : like}
        reaction={isLike}
        size={size}
      >
        <ArrowBigUp
          strokeWidth={1.25}
          size={buttonSize}
          color={
            size === "sm"
              ? isLike === 1
                ? "#D63E18"
                : "black"
              : isLike === 1 || isLike === -1
                ? "#fff"
                : "black"
          }
          className={classNames({
            "fill-white": size === "md" && isLike === 1,
            "fill-none": size === "md" && isLike === -1,
            "fill-buttonRed-normal": size === "sm" && isLike === 1,
          })}
        />
      </Button>
      <span className="text-xs font-semibold">{count}</span>
      <Button
        onClick={isLike === -1 ? noneLike : dislike}
        reaction={isLike}
        size={size}
      >
        <ArrowBigDown
          color={
            size === "sm"
              ? isLike === -1
                ? "#695DF8"
                : "black"
              : isLike === 1 || isLike === -1
                ? "#fff"
                : "black"
          }
          strokeWidth={1.25}
          size={buttonSize}
          className={classNames({
            "fill-white": size === "md" && isLike === -1,
            "fill-none": size === "md" && isLike === 1,
            "fill-buttonBlue-normal": size === "sm" && isLike === -1,
          })}
        />
      </Button>
    </div>
  );
};

function Button({ reaction, onClick, children, size }) {
  return (
    <button
      onClick={onClick}
      className={classNames("rounded-[--radius] p-0", {
        "hover:bg-buttonGray-lg":
          (size === "md" && reaction === 0) || size === "sm",
        "hover:bg-buttonRed-active": size === "md" && reaction === 1,
        "hover:bg-buttonBlue-active": size === "md" && reaction === -1,
      })}
    >
      {children}
    </button>
  );
}

export default LikeButton;
