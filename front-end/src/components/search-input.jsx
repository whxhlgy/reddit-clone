import { Input } from "@/components/ui/input";
import { Search } from "lucide-react";

const SearchInput = () => {
  return (
    <div className="flex items-center">
      <Search />
      <Input />
    </div>
  );
};

export default SearchInput;
