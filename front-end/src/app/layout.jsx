import Header from "@/components/app-header";
import { SidebarProvider } from "@/components/ui/sidebar";
import { Outlet, useLoaderData } from "react-router-dom";
import { whoami } from "@/api/user";
import { createContext, useContext } from "react";
import { UserContext } from "@/lib/context";

export const loader = async () => {
  const user = await whoami();
  localStorage.setItem("username", user.username);
  return user;
};

const Layout = () => {
  const user = useLoaderData();
  return (
    <>
      <SidebarProvider>
        <UserContext.Provider value={user}>
          <Header />
          <Outlet />
        </UserContext.Provider>
      </SidebarProvider>
    </>
  );
};

export default Layout;
