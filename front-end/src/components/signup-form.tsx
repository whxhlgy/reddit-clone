import { useFetcher } from "react-router-dom";

import { signup } from "@/api/auth";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { ActionFunctionArgs, redirect } from "react-router-dom";
import { toast } from "sonner";
import { z } from "zod";

export const action = async ({ request }: ActionFunctionArgs) => {
  try {
    const formData = Object.fromEntries(await request.formData());
    const res = await signup(formData);
    console.log(`sign up res: ${res}`);
    // return { ok: true, error: null };
    return redirect(`/home`);
  } catch (error) {
    console.debug(error);
    toast.error(error.message);
    return { ok: false, errorCode: error.errorCode };
  }
};

export function SignUpForm() {
  const fetcher = useFetcher();
  const formSchema = z.object({
    username: z.string().min(2).max(20, {
      message: "username too long(20)",
    }),
    password: z.string().min(1).max(20, {
      message: "password too long(20)",
    }),
  });

  const {
    register,
    formState: { errors },
    handleSubmit,
    setValue,
    setFocus,
  } = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  });

  // login submit
  const onSubmit = async (value: z.infer<typeof formSchema>) => {
    await fetcher.submit(value, {
      method: "post",
    });
  };

  useEffect(() => {
    if (fetcher.data) {
      if (!fetcher.data.ok) {
        const errorCode = fetcher.data.errorCode;
        if (errorCode === "BAD_CREDENTIALS") {
          setValue("password", "");
          setFocus("password");
        }
      }
    }
  }, [fetcher.data]);

  return (
    <Card className="mx-auto max-w-sm">
      <CardHeader>
        <CardTitle className="text-2xl">Signup</CardTitle>
      </CardHeader>
      <CardContent>
        <form className="grid gap-4" onSubmit={handleSubmit(onSubmit)}>
          <div className="grid gap-2">
            <Label htmlFor="username">Username</Label>
            <Input
              id="username"
              type="username"
              placeholder="m@example.com"
              required
              {...register("username")}
            />
            {errors.username && (
              <p className="error-message">{errors.username?.message}</p>
            )}
          </div>
          <div className="grid gap-2">
            <div className="flex items-center">
              <Label htmlFor="password">Password</Label>
            </div>
            <Input
              id="password"
              type="password"
              required
              {...register("password")}
            />
            {errors.password && (
              <p className="error-message">{errors.password?.message}</p>
            )}
          </div>
          <Button type="submit" className="w-full">
            SignUp
          </Button>
        </form>
      </CardContent>
    </Card>
  );
}
