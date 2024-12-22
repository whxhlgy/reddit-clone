import { likeComment } from "@/api/like";
import LikeButton from "@/components/like-button";
import { useFetcher } from "react-router-dom";

const CommentLike = ({ comment }) => {
  const fetcher = useFetcher();

  const onReaction = async (reaction) => {
    fetcher.submit(
      { commentId: comment.id, reaction, indent: "like" },
      {
        method: "post",
      },
    );
  };

  return (
    <LikeButton
      reaction={comment.reaction}
      likeCount={comment.likeCount}
      onReaction={onReaction}
      size={"sm"}
    />
  );
};

export default CommentLike;
