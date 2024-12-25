import { getCommunityByName, subscribeCommunityByName } from "@/api/community";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { Outlet, useFetcher, useLoaderData, useParams } from "react-router-dom";

export async function loader({ params }) {
  const { communityName } = params;
  console.debug(`get info of community: ${communityName}`);
  const community = await getCommunityByName(communityName);
  console.debug(`get community: ${JSON.stringify(community)}`);
  return community;
}

const CommLayout = () => {
  const { name, description } = useLoaderData();
  const fetcher = useFetcher();
  const onSubscription = () => {
    fetcher.submit({ name, intend: "join" }, { method: "post" });
  };
  return (
    <>
      <div className="w-2/3">
        <Outlet />
      </div>
      <div className="ml-4 sticky top-[--header-height] w-1/3 bg-gray-100 rounded-lg flex flex-col gap-2">
        <Card>
          <CardHeader>
            <CardTitle>Community: {name}</CardTitle>
            <CardDescription>{description}</CardDescription>
          </CardHeader>
          <CardContent></CardContent>
          <CardFooter>
            <Button onClick={onSubscription}>Join</Button>
          </CardFooter>
        </Card>
      </div>
    </>
  );
};

export default CommLayout;
