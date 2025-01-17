import LikeButton from "@/components/like-button";
import { AvatarFallback, Avatar } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { UserContext } from "@/lib/context";
import { getInitials } from "@/lib/utils";
import { ArrowLeft, Eye } from "lucide-react";
import { useContext } from "react";
import { Link, useFetcher, useParams } from "react-router-dom";

const Post = ({ post }) => {
  const fetcher = useFetcher();
  const { username } = useContext(UserContext);
  const onReaction = (reaction) => {
    fetcher.submit(
      { intend: "postLike", postId: post.id, reaction, username },
      {
        method: "post",
      },
    );
  };

  const { communityName } = useParams();
  console.log(`post: ${JSON.stringify(post)}`);
  return (
    <div className="flex flex-col items-start gap-2">
      <div className="flex flex-row justify-between w-full gap-2">
        <div className="flex flex-row">
          <Button size="icon" asChild>
            <Link to={`/r/${communityName}/`}>
              <ArrowLeft />
            </Link>
          </Button>
          <Avatar>
            <AvatarFallback>{getInitials(post.username)}</AvatarFallback>
          </Avatar>
          <div className="flex flex-col justify-center text-xs">
            <p className="font-bold">{communityName}</p>
            <p>{post.username}</p>
          </div>
        </div>

        <div className="flex flex-row mr-4 items-center gap-2">
          <Eye size={16} />
          <p>{post.viewCount}</p>
        </div>
      </div>

      <h1 className="text-3xl">{post.title}</h1>
      <p>{post.content}</p>

      <LikeButton
        reaction={post.reaction}
        likeCount={post.likeCount}
        onReaction={onReaction}
        size={"md"}
      />
    </div>
  );
};

export default Post;
