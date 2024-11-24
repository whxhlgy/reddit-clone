import Page from "@/app/page";
import "./App.css";
import { HEADER_HEIGHT } from "@/utils/consts";
import { createBrowserRouter, RouterProvider } from "react-router";
import Layout from "@/app/layout";
import Home from "@/app/home";

const router = createBrowserRouter([
  {
    element: <Layout />,
    children: [
      {
        path: "/",
        element: <Home />,
        // children: [
        //   {
        //     path: "explore",
        //     element:
        //   }
        // ]
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
