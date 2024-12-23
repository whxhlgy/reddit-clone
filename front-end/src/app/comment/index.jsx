import { useLoaderData } from "react-router-dom";
import Post from "@/components/post";
import { Separator } from "@/components/ui/separator";
import CommentForm from "@/components/create-comment-form";
import { createComment, findAllByPostId } from "@/api/comment";
import { getPostById } from "@/api/post";
import Comments from "@/app/comment/comments";
import { likeComment, likePost } from "@/api/like";

export async function loader({ params }) {
  const postId = params.postId;
  console.log(`find all comments for ${postId}`);
  const comments = await findAllByPostId(postId);
  const originPost = await getPostById(postId);
  return { comments, post: originPost };
}

export async function action({ params, request }) {
  const formData = Object.fromEntries(await request.formData());
  console.log(`get formData: ${JSON.stringify(formData)}`);

  if (!formData.intend) {
    throw new Error("unintended action!");
  }
  const intend = formData.intend;
  delete formData.intend;

  switch (intend) {
    case "add":
      console.debug(`add comment with postId: ${params.postId}`);
      await createComment(params.postId, formData);
      break;
    case "like":
      console.debug(
        `give reaction: ${formData.reaction} to comment with id: ${formData.commentId}`,
      );
      await likeComment(formData.commentId, formData.reaction);
      break;
    case "postLike":
      console.debug(
        `give reaction: ${formData.reaction} to post with id: ${formData.postId}`,
      );
      await likePost(formData.postId, formData.reaction);
      break;
  }
}

const CommentsIndex = () => {
  const { comments, post } = useLoaderData();
  return (
    <div className="space-y-10">
      {/* header */}
      <Post post={post} />

      <CommentForm />

      <Separator />

      {/* comments */}
      <Comments comments={comments} />
    </div>
  );
};

export default CommentsIndex;
