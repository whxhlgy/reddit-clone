import { SidebarProvider } from "@/components/ui/sidebar";
import { AppSidebar } from "@/components/app-sidebar";
import Header from "@/components/app-header";
import { Outlet } from "react-router";

const Layout = () => {
  return (
    <>
      <Header />
      <SidebarProvider>
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
