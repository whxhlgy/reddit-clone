import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form";
import { Textarea } from "@/components/ui/textarea";
import { UserContext } from "@/lib/context";
import { zodResolver } from "@hookform/resolvers/zod";
import { useContext, useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useSubmit } from "react-router-dom";
import { z } from "zod";

const commentSchema = z.object({
  content: z.string().min(1, {
    message: "You have not input anything!",
  }),
  username: z.string(),
  intend: z.string(),
});

const CommentForm = () => {
  const user = useContext(UserContext);
  const form = useForm({
    resolver: zodResolver(commentSchema),
    defaultValues: {
      content: "",
      username: user.username,
      intend: "add",
    },
  });

  const submit = useSubmit();

  function onSubmit(values) {
    console.log(values);
    submit(values, {
      method: "post",
    });
    form.setValue("content", "");
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="content"
          render={({ field }) => (
            <FormItem>
              <FormControl>
                <Textarea {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <input type="hidden" {...form.register("username")} />
        <Button type="submit">Add comment</Button>
      </form>
    </Form>
  );
};

export default CommentForm;
