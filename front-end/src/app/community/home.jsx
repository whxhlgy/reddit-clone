import { getPosts } from "@/api/post";
import Feed from "@/app/community/feed";
import { createPost } from "@/api/post";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { Link, useLoaderData } from "react-router-dom";
import { likePost } from "@/api/like";

export function loader() {
  const posts = getPosts();
  return posts;
}

export async function action({ request }) {
  const data = Object.fromEntries(await request.formData());
  console.debug(`get formData: ${JSON.stringify(data)}`);

  const intend = data.intend;
  delete data.intend;

  switch (intend) {
    case "add":
      // TODO:
      console.debug(
        `add a post(id: ${data.id}) to community: ${"not implemented"}`,
      );
      await createPost(data);
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
