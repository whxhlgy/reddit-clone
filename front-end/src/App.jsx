import { HEADER_HEIGHT } from "@/lib/consts";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "@/app/layout";
import Home from "@/app/home";
import "./App.css";
import CommunityHome, {
  action as feedAction,
  loader as communityLoader,
} from "@/app/community/home";
import ErrorPage from "@/app/error-page";
import Submit, { loader as feedSubmitLoader } from "@/app/community/submit";
import { action as loginAction, default as LoginPage } from "@/app/auth/login";
import { Toaster } from "@/components/ui/sonner";
import SideBarLayout, {
  action as sideBarAction,
  loader as sideBarLoader,
} from "@/app/sidebar-layout";
import { loader as headerLoader } from "@/components/app-header";
import CommLayout from "@/app/comm-layout";

const router = createBrowserRouter([
  {
    path: "/login",
    action: loginAction,
    element: <LoginPage />,
  },
  {
    path: "/signup",
    element: <LoginPage />,
  },
  {
    element: <Layout />,
    loader: headerLoader,
    children: [
      {
        element: <SideBarLayout />,
        path: "/",
        loader: sideBarLoader,
        action: sideBarAction,
        // errorElement: <ErrorPage />,
        children: [
          {
            errorElement: <ErrorPage />,
            children: [
              {
                index: true,
                path: "home",
                element: <Home />,
              },
              // This is for community info
              {
                path: "r/:communityName",
                element: <CommLayout />,
                children: [
                  // This is for community feed
                  {
                    loader: communityLoader,
                    action: feedAction,
                    // path: "home",
                    index: true,
                    element: <CommunityHome />,
                  },
                  {
                    path: "submit",
                    element: <Submit />,
                    loader: feedSubmitLoader,
                  },
                  {
                    path: "comments/:postId",
                    // element:
                  },
                ],
              },
            ],
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
      <Toaster position="top-center" richColors />
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
