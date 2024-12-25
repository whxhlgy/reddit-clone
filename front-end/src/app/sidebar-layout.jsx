import AppSidebar from "@/components/app-sidebar";
import { Outlet, useLoaderData } from "react-router-dom";
import {
  createCommunity,
  getAllCommunities,
  getCommunityByUsername,
  subscribeCommunityByName,
} from "@/api/community";
import { toast } from "sonner";

export async function loader() {
  try {
    const res = await getCommunityByUsername(
      localStorage.getItem("username") || "Anonymous",
    );
    return res;
  } catch (error) {
    console.error(error);
    return [];
  }
}

export async function action({ request }) {
  try {
    const data = Object.fromEntries(await request.formData());
    console.debug(`community action get formData : ${JSON.stringify(data)}`);

    const intend = data.intend;
    delete data.intend;
    switch (intend) {
      case "join":
        await subscribeCommunityByName(data.name);
        break;
      case "create":
        await createCommunity(data);
        break;
      default:
        throw new Error("unintend action not allowed");
    }
    return { ok: true };
  } catch (error) {
    console.error(error);
    toast.error("Failed to create community");
    return { ok: false, error };
  }
}

export default function SideBarLayout() {
  const communities = useLoaderData();
  return (
    <>
      <AppSidebar communities={communities} />
      <main className="bg-feed-background relative w-full z-0 top-[--header-height] scroll-mt-[--header-height] px-4 py-2 flex justify-center items-start">
        <div className="relative max-w-screen-xl flex-grow flex justify-center items-start">
          <Outlet />
        </div>
      </main>
    </>
  );
}
