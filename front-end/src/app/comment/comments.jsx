import Comment from "@/app/comment/comment";

const Comments = ({ comments }) => {
  return (
    <>
      {comments && comments.length > 0 && (
        <div>
          {comments.map((comment) => (
            <Comment comment={comment} key={comment.id} />
          ))}
        </div>
      )}
    </>
  );
};

export default Comments;
