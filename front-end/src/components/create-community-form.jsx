import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
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
import { Button } from "@/components/ui/button";
import { useFetcher } from "react-router-dom";
import { Loader2 } from "lucide-react";
import { useEffect } from "react";

const CreateCommunityForm = ({ closeDialog }) => {
  const formSchema = z.object({
    name: z
      .string()
      .min(2, {
        message: "Name must be at least 2 characters",
      })
      .max(50),
    description: z.string().min(1, {
      message: "Description cannot be empty",
    }),
  });

  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      description: "no desc",
    },
  });

  // const submit = useSubmit();
  const fetcher = useFetcher();
  const { data, state } = fetcher;

  const onSubmit = async (value) => {
    await fetcher.submit(value, {
      method: "post",
      encType: "application/json",
    });
  };

  useEffect(() => {
    if (state === "idle" && data?.ok) {
      closeDialog();
    } else if (state === "idle" && data?.ok === false) {
      if (data.error.status === 409) {
        form.setError(
          "name",
          {
            type: "manual",
            message: "Name already exist",
          },
          {
            shouldFocus: true,
          },
        );
      }
    }
  }, [state, data]);

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
        <div className="space-y-2">
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Name</FormLabel>
                <FormControl>
                  <Input placeholder="name" {...field} />
                </FormControl>
                <FormDescription>This is your Community name</FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="description"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Description</FormLabel>
                <FormControl>
                  <Input {...field} />
                </FormControl>
                <FormDescription>Your community description</FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <Button {...(state !== "idle" && { disabled: true })} type="submit">
          {state !== "idle" && <Loader2 className="animate-spin" />}
          {state === "idle" ? "Sumbit" : "Sumbitting..."}
        </Button>
      </form>
    </Form>
  );
};

export default CreateCommunityForm;
