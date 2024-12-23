import classNames from "classnames";
import { ArrowBigDown, ArrowBigUp } from "lucide-react";

const LikeButton = ({ reaction, likeCount, onReaction, size }) => {
  const onClickReaction = (reaction) => {
    return (e) => {
      e.preventDefault();
      onReaction(reaction);
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
          "bg-buttonRed-normal text-white": size === "md" && reaction === 1,
          "bg-buttonBlue-normal text-white": size === "md" && reaction === -1,
          "bg-primary": size === "md" && reaction === 0,
        },
        {
          "bg-feed-background": size === "sm",
        },
      )}
    >
      <Button
        onClick={reaction === 1 ? noneLike : like}
        reaction={reaction}
        size={size}
      >
        <ArrowBigUp
          strokeWidth={1.25}
          size={buttonSize}
          color={
            size === "sm"
              ? reaction === 1
                ? "#D63E18"
                : "black"
              : reaction === 1 || reaction === -1
                ? "#fff"
                : "black"
          }
          className={classNames({
            "fill-white": size === "md" && reaction === 1,
            "fill-none": size === "md" && reaction === -1,
            "fill-buttonRed-normal": size === "sm" && reaction === 1,
          })}
        />
      </Button>
      <span className="text-xs font-semibold">{likeCount}</span>
      <Button
        onClick={reaction === -1 ? noneLike : dislike}
        reaction={reaction}
        size={size}
      >
        <ArrowBigDown
          color={
            size === "sm"
              ? reaction === -1
                ? "#695DF8"
                : "black"
              : reaction === 1 || reaction === -1
                ? "#fff"
                : "black"
          }
          strokeWidth={1.25}
          size={buttonSize}
          className={classNames({
            "fill-white": size === "md" && reaction === -1,
            "fill-none": size === "md" && reaction === 1,
            "fill-buttonBlue-normal": size === "sm" && reaction === -1,
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
        "hover:bg-buttonGray-lg": size === "md" && reaction === 0,
        "hover:bg-buttonRed-active": size === "md" && reaction === 1,
        "hover:bg-buttonBlue-active": size === "md" && reaction === -1,
        "hover:bg-buttonGray-md": size === "sm",
      })}
    >
      {children}
    </button>
  );
}

export default LikeButton;
