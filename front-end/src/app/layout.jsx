import Header from "@/components/app-header";
import { SidebarProvider } from "@/components/ui/sidebar";
import { Outlet, useLoaderData } from "react-router-dom";
import * as user from "@/api/user";

export const loader = async () => {
  return await user.whoami();
};

const Layout = () => {
  const user = useLoaderData();
  return (
    <>
      <SidebarProvider>
        <Header user={user} />
        <Outlet />
      </SidebarProvider>
    </>
  );
};

export default Layout;
