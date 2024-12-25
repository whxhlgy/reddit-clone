import { login } from "@/api/auth";
import { LoginForm } from "@/components/login-form";
import { ActionFunctionArgs, redirect } from "react-router-dom";
import { toast } from "sonner";

export const action = async ({ request }: ActionFunctionArgs) => {
  try {
    console.debug("login action");
    const formData = await request.json();
    const res = await login(formData);
    // return { ok: true, error: null };
    return redirect(`/home`);
  } catch (error) {
    console.debug(error);
    toast.error(error.message);
    return { ok: false, errorCode: error.errorCode };
  }
};

export default function Page() {
  return (
    <div className="flex h-screen w-full items-center justify-center px-4">
      <LoginForm />
    </div>
  );
}
