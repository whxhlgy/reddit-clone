import { getPosts } from "@/api/post";
import Feed from "@/app/community/feed";
import { createPost } from "@/api/post";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import { Link, useLoaderData } from "react-router-dom";

export function loader() {
  const posts = getPosts();
  return posts;
}

export async function action({ request }) {
  const data = await request.json();
  await createPost(data);

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
