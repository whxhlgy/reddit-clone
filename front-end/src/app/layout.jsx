import { SidebarProvider } from "@/components/ui/sidebar";
import AppSidebar from "@/components/app-sidebar";
import Header from "@/components/app-header";
import { Outlet } from "react-router-dom";
import { useLoaderData } from "react-router-dom";
import { getAllCommunities, createCommunity } from "@/api/community";

export async function loader() {
  return getAllCommunities();
}

export async function action({ request }) {
  const body = await request.json();
  const res = await createCommunity(body);
  console.log(res);
}

const Layout = () => {
  const communities = useLoaderData();
  return (
    <>
      <SidebarProvider>
        <Header />
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
