import { useLoaderData } from "react-router-dom";
import Post from "@/components/post";
import Comment from "@/app/comment/comment";
import { Separator } from "@/components/ui/separator";
import CommentForm from "@/components/create-comment-form";
import { createComment, findAllByPostId } from "@/api/comment";
import { getPostById } from "@/api/post";

export async function loader({ params }) {
  const postId = params.postId;
  console.log(`find all comments for ${postId}`);
  const comments = await findAllByPostId(postId);
  const originPost = await getPostById(postId);
  return { comments, post: originPost };
}

export async function action({ params, request }) {
  const comment = await request.json();
  console.log(`commit the comment of post of postId: ${params.postId}`);
  await createComment(params.postId, comment);
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
      {comments.map((comment) => (
        <Comment comment={comment} key={comment.id} />
      ))}
    </div>
  );
};

export default CommentsIndex;