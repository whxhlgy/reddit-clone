import { HEADER_HEIGHT } from "@/lib/consts";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout, { loader as headerLoader } from "@/app/layout";
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
import CommLayout from "@/app/comm-layout";
import CommentsIndex, {
  action as commentAction,
  loader as commentLoader,
} from "@/app/comment";
import { action as signupAction, SignUpForm } from "@/components/signup-form";

const router = createBrowserRouter([
  {
    path: "/login",
    action: loginAction,
    element: <LoginPage />,
  },
  {
    path: "/signup",
    action: signupAction,
    element: <SignUpForm />,
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
                element: <CommLayout />,
                children: [
                  // This is for community feed
                  {
                    path: "r/:communityName",
                    action: feedAction,
                    loader: communityLoader,
                    element: <CommunityHome />,
                  },
                  {
                    path: "r/:communityName/submit",
                    element: <Submit />,
                    loader: feedSubmitLoader,
                  },
                  {
                    path: "r/:communityName/comments/:postId",
                    element: <CommentsIndex />,
                    loader: commentLoader,
                    action: commentAction,
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
