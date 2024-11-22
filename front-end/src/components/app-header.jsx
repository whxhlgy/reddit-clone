import SearchInput from "@/components/search-input";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

const Header = () => {
  return (
    <header className="fixed top-0 right-0 left-0 bg-white z-10 h-[--header-height] border">
      <nav className="flex justify-between items-center p-4">
        <div>Logo</div>
        <SearchInput />
        <div>User</div>
      </nav>
    </header>
  );
};

export default Header;
