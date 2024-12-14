import Header from "@/components/app-header";
import { SidebarProvider } from "@/components/ui/sidebar";
import { Outlet } from "react-router-dom";

const Layout = () => {
  return (
    <>
      <SidebarProvider>
        <Header />
        <Outlet />
      </SidebarProvider>
    </>
  );
};

export default Layout;
