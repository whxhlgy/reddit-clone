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
        {/* thread line */}
        <ThreadLine
          className="row-start-1"
          over={over}
          setOver={setOver}
          onClick={onExpand}
        />

        {/* content */}
        <div className="max-h-10 col-start-auto row-start-1 row-end-1">
          <p>{comment.content}</p>
        </div>

        {/* children */}
        <>
          {comment.children && comment.children.length > 0 && (
            <>
              {comment.children.map((child, index) => (
                <>
                  {index !== comment.children.length - 1 && (
                    <ThreadLine
                      className={classNames("col-start-1", {
                        hidden: expand === false,
                      })}
                      over={over}
                      setOver={setOver}
                      onClick={onExpand}
                    />
                  )}
                  <div
                    className={classNames("col-start-2", {
                      hidden: expand === false,
                    })}
                  >
                    <ThreadPointer
                      over={over}
                      setOver={setOver}
                      onClick={onExpand}
                    />
                    <Comment key={child.id} comment={child} />
                  </div>
                </>
              ))}
            </>
          )}
        </>
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
