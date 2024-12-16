import LikeButton from "@/components/like-button";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";

const Comment = ({ comment }) => {
  return (
    <div>
      <div className="flex items-center">
        <Avatar>
          <AvatarFallback>AN</AvatarFallback>
        </Avatar>
        <p className="text-xs">{comment.username}</p>
      </div>

      <p>{comment.content}</p>

      {/* action row */}
    </div>
  );
};

export default Comment;
