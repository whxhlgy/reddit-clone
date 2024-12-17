import LikeButton from "@/components/like-button";
import { AvatarFallback, Avatar } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { ArrowLeft } from "lucide-react";
import { Link, useParams } from "react-router-dom";

const Post = ({ post }) => {
  const { communityName } = useParams();
  return (
    <div className="flex flex-col items-start gap-2">
      <div className="flex flex-row justify-start gap-2">
        <Button size="icon" asChild>
          <Link to={`/r/${communityName}/`}>
            <ArrowLeft />
          </Link>
        </Button>
        <Avatar>
          <AvatarFallback>CN</AvatarFallback>
        </Avatar>
        <div className="flex flex-col justify-center text-xs">
          <p className="font-bold">comm name</p>
          <p>{post.username}</p>
        </div>
      </div>

      <h1 className="text-3xl">{post.title}</h1>
      <p>{post.content}</p>

      <LikeButton post={post} />
    </div>
  );
};

export default Post;
