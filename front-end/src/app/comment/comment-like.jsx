import { likeComment } from "@/api/like";
import LikeButton from "@/components/like-button";
import { UserContext } from "@/lib/context";
import { useContext } from "react";
import { useFetcher } from "react-router-dom";

const CommentLike = ({ comment }) => {
  const fetcher = useFetcher();
  const { username } = useContext(UserContext);

  const onReaction = async (reaction) => {
    fetcher.submit(
      { commentId: comment.id, reaction, intend: "like", username },
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
