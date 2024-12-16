import { Outlet } from "react-router-dom";

const CommLayout = () => {
  return (
    <>
      <div className="w-2/3">
        <Outlet />
      </div>
      <div className="w-1/3 bg-blue-50">SIDEBAR</div>
    </>
  );
};

export default CommLayout;
