import { likePost } from "@/api/like";
import { createPost, getPostByCommunityName } from "@/api/post";
import Feed from "@/app/community/feed";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { Link, useLoaderData } from "react-router-dom";

export function loader({ params }) {
  const { communityName } = params;
  const posts = getPostByCommunityName(communityName);
  return posts;
}

export async function action({ params, request }) {
  const data = Object.fromEntries(await request.formData());
  const { communityName } = params;
  console.debug(`get formData: ${JSON.stringify(data)}`);

  const intend = data.intend;
  delete data.intend;

  switch (intend) {
    case "addPost":
      console.debug(
        `add a post(id: ${data.id}) to community: ${communityName}`,
      );
      await createPost(communityName, data);
      break;
    case "like":
      console.debug(`like a post(id: ${data.postId})`);
      await likePost(data.postId, data.reaction);
      break;
    default:
      throw new Error("unintended action");
  }

  return { ok: true };
}

const Home = () => {
  const posts = useLoaderData();
  return (
    <div className="flex flex-col gap-2">
      {/* header */}
      <div>
        <Button asChild variant="outline">
          <Link to="submit">
            <Plus />
            <span>Create Post</span>
          </Link>
        </Button>
      </div>
      {/* main content */}
      <div>
        <Feed posts={posts} />
      </div>
    </div>
  );
};

export default Home;
