import CommentLike from "@/app/comment/comment-like";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import classNames from "classnames";
import { CircleMinus, CirclePlus } from "lucide-react";
import { useState } from "react";

const Comment = ({ comment }) => {
  const [over, setOver] = useState(false);
  const expandable = comment.children && comment.children.length > 0;
  const [expand, setExpand] = useState(expandable === true);

  const children = comment.children;

  return (
    <div>
      {/* header */}
      <div className="flex items-center">
        <Avatar>
          <AvatarFallback>AN</AvatarFallback>
        </Avatar>
        <p className="text-xs">{comment.username}</p>
      </div>

      <div className={`relative grid ml-2 grid-cols-[24px_1fr]`}>
        {/* main thread */}
        <div
          className={classNames(
            "absolute top-0 left-0 w-6 h-full bg-thread bg-no-repeat bg-center bg-[length:1px]",
            {
              "bg-threadDark": over === true,
              hidden: expandable === false,
            },
          )}
        ></div>
        {/* clickable main thread */}
        <div
          className={classNames(
            "absolute top-0 left-0 w-6 h-full cursor-pointer z-10",
            {
              hidden: expandable === false,
            },
          )}
          onClick={() => setExpand(!expand)}
          onMouseEnter={() => setOver(true)}
          onMouseOut={() => setOver(false)}
        ></div>

        {/* content */}
        <div className="contents">
          <div />
          <p>{comment.content}</p>
        </div>

        {/* action row */}
        <div className="contents">
          {!expandable ? (
            <div />
          ) : expand ? (
            <CircleMinus
              size="16px"
              className="justify-self-center self-center z-0 bg-feed-background"
            />
          ) : (
            <CirclePlus
              size="16px"
              className="justify-self-center self-center z-0 bg-feed-background"
            />
          )}
          <div className="justify-self-start items-center">
            <CommentLike comment={comment} />
          </div>
        </div>

        {/* children */}
        <div
          className={classNames("contents", {
            hidden: expand === false,
          })}
        >
          {comment.children && comment.children.length > 0 && (
            <>
              {comment.children.map((child, index) => (
                <div className="contents" key={child.id}>
                  {
                    <>
                      {/* branch line */}
                      <div
                        className={classNames("relative flex justify-end", {
                          "bg-feed-background z-20":
                            index == children.length - 1,
                        })}
                      >
                        <div
                          className={classNames(
                            "absolute w-3 h-3 border-solid border-l-[1px] border-b-[1px] rounded-bl-sm",
                            {
                              "border-thread-dark": over === true,
                            },
                          )}
                        />
                      </div>
                      {/* child */}
                      <Comment key={child.id} comment={child} />
                    </>
                  }
                </div>
              ))}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Comment;
