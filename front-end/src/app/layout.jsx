import { SidebarProvider } from "@/components/ui/sidebar";
import { AppSidebar } from "@/components/app-sidebar";
import Header from "@/components/app-header";
import { Outlet } from "react-router";
import { getCookie } from "@/utils/cookies";

const Layout = () => {
  const defaultOpen = getCookie("sidebar:state") === "true";
  return (
    <>
      <SidebarProvider>
        <Header />
        <AppSidebar />
        <main className="bg-red-100 relative z-0 top-[--header-height] scroll-mt-[--header-height]">
          <div className="flex flex-col justify-start">
            <Outlet />
          </div>
        </main>
      </SidebarProvider>
    </>
  );
};

export default Layout;
