import { Link, redirect, useFetcher } from "react-router-dom";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect } from "react";

export function LoginForm() {
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
      encType: "application/json",
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
        <CardTitle className="text-2xl">Login</CardTitle>
        <CardDescription>
          Enter your Username below to login to your account
        </CardDescription>
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
              <Link
                to="/reset"
                className="ml-auto inline-block text-sm underline"
              >
                {/* TODO: not implement yet */}
                Forgot your password?
              </Link>
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
            Login
          </Button>
        </form>
        <div className="mt-4 text-center text-sm">
          Don&apos;t have an account?{" "}
          <Link to="/signup" className="underline">
            Sign up
          </Link>
        </div>
      </CardContent>
    </Card>
  );
}
