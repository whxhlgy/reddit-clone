import { SidebarProvider } from "@/components/ui/sidebar";
import AppSidebar from "@/components/app-sidebar";
import Header from "@/components/app-header";
import { Outlet, useLocation } from "react-router-dom";
import { useLoaderData } from "react-router-dom";
import { getAllCommunities, createCommunity } from "@/api/community";
import { toast, Toaster } from "sonner";
import { useEffect } from "react";

export async function loader() {
  try {
    const res = await getAllCommunities();
    return res;
  } catch (error) {
    console.error(error);
    return [];
  }
}

export async function action({ request }) {
  try {
    const body = await request.json();
    await createCommunity(body);
    return { ok: true };
  } catch (error) {
    console.error(error);
    toast.error("Failed to create community");
    return { ok: false, error };
  }
}

const Layout = () => {
  const communities = useLoaderData();
  return (
    <>
      <SidebarProvider>
        <Header />
        <Toaster position="top-center" richColors />
        <AppSidebar communities={communities} />
        <main className="bg-feed-background relative w-full z-0 top-[--header-height] scroll-mt-[--header-height] px-4 py-2 flex justify-center items-start">
          <div className="max-w-screen-xl flex-grow flex justify-center items-start">
            <div className="w-2/3">
              <Outlet />
            </div>
            <div className="w-1/3 bg-blue-50">SIDEBAR</div>
          </div>
        </main>
      </SidebarProvider>
    </>
  );
};

export default Layout;
