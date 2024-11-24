import SearchInput from "@/components/search-input";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { SidebarTrigger } from "@/components/ui/sidebar";

const Header = () => {
  return (
    <header className="fixed w-[100vw] top-0 right-0 left-0 bg-white z-10 h-[--header-height] border">
      <nav className="flex justify-between items-center p-4">
        <div className="flex">
          <SidebarTrigger className="md:hidden" />
          <div>Logo</div>
        </div>
        <SearchInput />
        <div>User</div>
      </nav>
    </header>
  );
};

export default Header;
