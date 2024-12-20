import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import classNames from "classnames";
import { useState } from "react";

const Comment = ({ comment }) => {
  const [over, setOver] = useState(false);
  const [expand, setExpand] = useState(true);

  const onExpand = () => {
    console.log(`expand: ${expand}`);
    setExpand(!expand);
  };
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
        <div className="absolute top-0 left-0 w-6 h-full bg-thread bg-no-repeat bg-center bg-[length:1px]" />

        {/* content */}
        <div className="contents">
          <div />
          <p>{comment.content}</p>
        </div>

        {/* children */}
        <div className="contents">
          {comment.children && comment.children.length > 0 && (
            <>
              {comment.children.map((child, index) => (
                <div className="contents" key={child.id}>
                  {
                    <>
                      {/* branch line */}
                      <div
                        className={classNames("relative flex justify-end", {
                          "bg-feed-background": index == children.length - 1,
                        })}
                      >
                        <div className="absolute w-3 h-3 border-solid border-l-[1px] border-b-[1px] border-b-thread-normal border-l-thread-normal rounded-bl-sm" />
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

const ThreadLine = ({ className, over, setOver, onClick }) => {
  return (
    <div
      className={classNames(
        className,
        "bg-thread bg-no-repeat bg-center bg-[length:1px] cursor-pointer",
        {
          "bg-threadDark": over === true,
        },
      )}
      onMouseEnter={() => setOver(true)}
      onMouseOut={() => setOver(false)}
      onClick={onClick}
    />
  );
};

const ThreadPointer = ({ over, setOver, onClick }) => {
  return (
    <div
      className={classNames(
        "w-5 h-5 cursor-pointer absolute left-[11.5px] border-solid border-l-[1px] border-b-[1px] border-b-thread-normal border-l-thread-normal rounded-bl-lg",
        {
          "border-b-thread-dark": over === true,
          "border-l-thread-dark": over === true,
        },
      )}
      onMouseEnter={() => setOver(true)}
      onMouseOut={() => setOver(false)}
      onClick={onClick}
    ></div>
  );
};

export default Comment;
