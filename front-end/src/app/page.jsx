import { SidebarProvider } from "@/components/ui/sidebar";
import { AppSidebar } from "@/components/app-sidebar";
import Header from "@/components/app-header";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

const Page = () => {
  return (
    <>
      <Header />
      <SidebarProvider>
        <AppSidebar />
        {/* <SidebarTrigger /> */}
        <main>
          <div>test</div>
          <Input />
          <Button>Hello</Button>
        </main>
      </SidebarProvider>
    </>
  );
};

export default Page;
