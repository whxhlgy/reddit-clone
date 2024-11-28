import { SidebarProvider } from "@/components/ui/sidebar";
import AppSidebar from "@/components/app-sidebar";
import Header from "@/components/app-header";
import { Outlet } from "react-router-dom";
import { useLoaderData } from "react-router-dom";
import { getAllCommunities, createCommunity } from "@/api/community";
import { toast, Toaster } from "sonner";

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
    const res = await createCommunity(body);
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
