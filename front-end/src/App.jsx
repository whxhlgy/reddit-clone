import { HEADER_HEIGHT } from "@/lib/consts";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout, {
  action as layoutAction,
  loader as layoutLoader,
} from "@/app/layout";
import Home from "@/app/home";
import "./App.css";
import CommunityHome, {
  action as feedAction,
  loader as communityLoader,
} from "@/app/community/home";
import ErrorPage from "@/app/error-page";
import Submit, { loader as feedSubmitLoader } from "@/app/community/submit";
import { default as LoginPage } from "@/app/login/page";

const router = createBrowserRouter([
  {
    path: "/login",
    element: <LoginPage />,
  },
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
            path: "home",
            element: <Home />,
          },
          {
            loader: communityLoader,
            action: feedAction,
            path: "r/:communityName",
            element: <CommunityHome />,
          },
          {
            path: "r/:communityName/submit",
            element: <Submit />,
            loader: feedSubmitLoader,
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
