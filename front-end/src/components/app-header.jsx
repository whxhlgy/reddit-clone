import SearchInput from "@/components/search-input";
import Logo from "/reddit.svg";
import { SidebarTrigger } from "@/components/ui/sidebar";
import UserDropDownMenu from "@/components/user-drop-menu";
import { useContext } from "react";
import { UserContext } from "@/lib/context";

const Header = () => {
  const user = useContext(UserContext);
  return (
    <header className="fixed w-[100vw] top-0 right-0 left-0 bg-white z-10 h-[--header-height] border">
      <nav className="flex justify-between items-center pr-6">
        <div className="flex items-center">
          <SidebarTrigger className="md:hidden" />
          <img src={Logo} alt="Reddit Logo" className="h-16" />
        </div>
        <SearchInput />

        {/* right section */}
        <div>
          <UserDropDownMenu user={user} />
        </div>
      </nav>
    </header>
  );
};

export default Header;
