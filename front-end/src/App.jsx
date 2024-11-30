import { HEADER_HEIGHT } from "@/lib/consts";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout, {
  action as layoutAction,
  loader as layoutLoader,
} from "@/app/layout";
import Home from "@/app/home";
import "./App.css";
import CommunityHome, { loader as communityLoader } from "@/app/community/home";
import ErrorPage from "@/app/error-page";

const router = createBrowserRouter([
  {
    element: <Layout />,
    path: "/",
    loader: layoutLoader,
    action: layoutAction,
    errorElement: <ErrorPage />,
    children: [
      {
        errorElement: <ErrorPage />,
        children: [
          {
            index: true,
            element: <Home />,
          },
          {
            loader: communityLoader,
            path: "r/:communityName",
            element: <CommunityHome />,
          },
        ],
      },
    ],
  },
]);

function App() {
  return (
    <div
      style={{
        "--header-height": HEADER_HEIGHT,
      }}
    >
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
