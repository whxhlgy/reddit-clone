import "./App.css";
import { HEADER_HEIGHT } from "@/utils/consts";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout, {
  action as layoutAction,
  loader as layoutLoader,
} from "@/app/layout";
import Home from "@/app/home";
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
            // children: [
            //   {
            //     path: "explore",
            //     element:
            //   }
            // ]
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
