import Feed from "@/app/community/feed";
import { useLoaderData } from "react-router-dom";

export function loader() {
  return [
    {
      id: 1,
      username: "钟俊杰",
      title: "我是标题",
      content:
        "A community dedicated to all things web development: both front-end and back-end. For more design-related questions, try /r/web_design.",
      reaction: null,
      likesCount: 100,
    },
  ];
}

const Home = () => {
  const posts = useLoaderData();
  console.log(posts);
  return (
    <>
      <div className="w-2/3">
        <Feed posts={posts} />
      </div>
      <div className="w-1/3 bg-blue-50">SIDEBAR</div>
    </>
  );
};

export default Home;
