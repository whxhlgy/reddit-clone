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
  action as SideBarAction,
  loader as SideBarLoader,
} from "@/app/sidebar-layout";

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
    path: "/",
    children: [
      {
        element: <SideBarLayout />,
        loader: SideBarLoader,
        action: SideBarAction,
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
