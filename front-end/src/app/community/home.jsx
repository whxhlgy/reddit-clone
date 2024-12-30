import { likePost } from "@/api/like";
import { createPost, getPostByCommunityName } from "@/api/post";
import Feed from "@/app/community/feed";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { Link, useLoaderData } from "react-router-dom";

export function loader({ params, request }) {
  console.log("🧸");
  // const url = new URL(request.url);
  // const page = url.searchParams.get("page");
  // const size = url.searchParams.get("size");
  // console.debug(
  //   `fetch post page: ${url.searchParams.get("page")}, size: ${url.searchParams.get("size")}`,
  // );
  // const { communityName } = params;
  // const posts = getPostByCommunityName(communityName, { page, size });
  // return posts;
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
      await likePost(data.postId, data);
      break;
    default:
      throw new Error("unintended action");
  }

  return { ok: true };
}

const Home = () => {
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
        <Feed />
      </div>
    </div>
  );
};

export default Home;
