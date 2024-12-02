import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { COMMUNITY_URL } from "@/lib/consts";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import {
  redirect,
  useFetcher,
  useLoaderData,
  useLocation,
  useSubmit,
} from "react-router-dom";
import { z } from "zod";

export function loader({ params }) {
  const communityName = params.communityName;
  return { communityName };
}

export default function Submit() {
  const { communityName } = useLoaderData();

  const postSchema = z.object({
    title: z.string().min(1, {
      message: "Title is required",
    }),
    content: z.string().min(1, {
      message: "Content is required",
    }),
  });
  const form = useForm({
    resolver: zodResolver(postSchema),
    defaultValues: {
      title: "",
      content: "",
    },
  });

  const submit = useSubmit();

  const onSubmit = (post) => {
    submit(
      {
        ...post,
        username: "anonymous",
      },
      {
        method: "post",
        action: `/${COMMUNITY_URL}/${communityName}`,
        encType: "application/json",
      },
    );
  };
  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 p-8">
        <FormField
          control={form.control}
          name="title"
          render={({ field }) => {
            return (
              <FormItem>
                <FormLabel>Title</FormLabel>
                <FormControl>
                  <Input placeholder="Title" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            );
          }}
        />
        <FormField
          control={form.control}
          name="content"
          render={({ field }) => {
            return (
              <FormItem>
                <FormControl>
                  <Textarea placeholder="Body" {...field} />
                </FormControl>
              </FormItem>
            );
          }}
        />
        <Button type="submit">Submit</Button>
      </form>
    </Form>
  );
}
