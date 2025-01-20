import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import SockJS from "sockjs-client";
import { over } from "stompjs";
import SearchBar from "../components/other/SearchBar";
import axios from "axios";

var stompClient = null;

export const ChatPage2 = () => {
  const [selectedUser, setSelectedUser] = useState(null);
  const [receiver, setReceiver] = useState("");
  const [chatId, setChatId] = useState(1);
  const [message, setMessage] = useState("");
  const [media, setMedia] = useState("");
  const [tab, setTab] = useState("CHATROOM");
  const [publicChats, setPublicChats] = useState([]);
  const [privateChats, setPrivateChats] = useState(new Map());
  const [username] = useState(localStorage.getItem("chat-username"));
  const navigate = useNavigate();
  const connected = useRef(false);

  if (!username.trim()) {
    navigate("/login");
  }

  useEffect(() => {
    if (!connected.current) {
      connect();
    }
    return () => {
      if (stompClient) {
        stompClient.disconnect();
        connected.current = false;
      }
    };
  }, []);

  const handlePrivateMessage = (user) => {
    setSelectedUser(user);
    setReceiver(user.username);

    if (!privateChats.has(user.username)) {
      privateChats.set(user.username, []);
      setPrivateChats(new Map(privateChats));
    }
  };

  const onMessageReceived = (payload) => {
    const payloadData = JSON.parse(payload.body);
    console.log("Public message received:", payloadData);
    switch (payloadData.type) {
      case "JOIN":
        if (payloadData.sender !== username) {
          if (!privateChats.get(payloadData.sender)) {
            privateChats.set(payloadData.sender, []);
            setChatId(payloadData.chat.id);
            setPrivateChats(new Map(privateChats));
          }
        }
        break;
      case "LEAVE":
        if (payloadData.sender !== username) {
          if (privateChats.get(payloadData.sender)) {
            privateChats.delete(payloadData.sender);
            setChatId(payloadData.chatId);
            setPrivateChats(new Map(privateChats));
          }
        }
        break;
      case "CHAT":
        setPublicChats((prev) => [...prev, payloadData]);
        setChatId(payloadData.chat.id);
        break;
      default:
        console.warn("Unknown type received:", payloadData.type);
    }
  };

  const onPrivateMessage = (payload) => {
    const payloadData = JSON.parse(payload.body);
    console.log("Private message received:", payloadData);
    if (privateChats.has(payloadData.sender)) {
      privateChats.get(payloadData.sender).push(payloadData);
    } else {
      privateChats.set(payloadData.sender, [payloadData]);
    }
    setPrivateChats(new Map(privateChats));
  };

  const onConnect = () => {
    console.log("Connected to WebSocket");
    connected.current = true;
    stompClient.subscribe(`/topic/${chatId}`, onMessageReceived);

    userJoin();
  };

  const onError = (err) => {
    console.error("WebSocket connection error:", err);
  };

  const connect = () => {
    // let sock = new SockJS("http://localhost:8080/ws");
    let sock = new WebSocket("http://localhost:8080/ws")
    stompClient = over(sock);
    stompClient.connect({}, onConnect, onError);
  };

  const userJoin = () => {
    let chatMessage = {
      sender: username,
      type: "JOIN",
    };

    stompClient.send(`/app/chat/sendMessage/${chatId}`, {}, JSON.stringify(chatMessage));
  };

  const userLeft = () => {
    let chatMessage = {
      sender: username,
      type: "LEAVE",
    };

    stompClient.send(`/app/chat/sendMessage/${chatId}`, {}, JSON.stringify(chatMessage));
  };

  const handleLogout = () => {
    userLeft();
    localStorage.removeItem("chat-username");
    navigate("/login");
  };

  // Handle file conversion to base64
  const base64ConversionForImages = (e) => {
    if (e.target.files[0]) {
      getBase64(e.target.files[0]);
    }
  };

  const getBase64 = (file) => {
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => setMedia(reader.result);
    reader.onerror = (error) => console.error("Error converting file:", error);
  };

  const sendMessage = () => {
    if (message.trim().length > 0 || media) {
      stompClient.send(
        `/app/chat/sendMessage/${chatId}`,
        {},
        JSON.stringify({
          sender: username,
          chatId: chatId,
          content: message,
          type: "CHAT",
        })
      );
      setMessage("");
      setMedia("");
    }
  };

  const sendPrivate = () => {
    if (message.trim().length > 0 && chatId) {
      let chatMessage = {
        sender: username,
        chatId: chatId,
        content: message,
        type: "CHAT",
      };

      privateChats.get(chatMessage.sender).push(chatMessage);
      setPrivateChats(new Map(privateChats));

      stompClient.send(`/app/chat/sendMessage/${chatId}`, {}, JSON.stringify(chatMessage));

      setMessage("");
      setMedia("");
    }
  };

  const tabReceiverSet = (name) => {
    setReceiver(name);
    setTab(name);
  };

  const fetchChatHistory = async (chatId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/v1/messages/${chatId}`
      );

      if (response.status === 200) {
        setPrivateChats((prevChats) => {
          prevChats.set(tab, response.data);
          return new Map(prevChats);
        });
      } else {
        console.error("Failed to fetch chat history:", response.status);
      }
    } catch (error) {
      console.error("Error fetching chat history:", error);
    }
  };

  return (
    <div className="flex items-center justify-center h-screen w-[100%]">
      <div className="flex w-full h-full">
        {/* Member List */}
        <div className="flex flex-col p-3 w-[200px] h-[551px] bg-transparent">
          <ul className="list-none space-y-2">
            <li
              key={"o"}
              className={`p-2 cursor-pointer rounded ${
                tab === "CHATROOM" ? "bg-blue-500 text-white" : "bg-gray-200"
              }`}
              onClick={() => setTab("CHATROOM")}
            >
              <span>Chat Room</span>
            </li>
            {[...privateChats.keys()].map((name, index) => (
              <li
                key={index}
                onClick={() => {
                  tabReceiverSet(name);
                  fetchChatHistory(chatId);
                  setTab(name)
                }}
                className={`p-2 cursor-pointer rounded ${
                  tab === name ? "bg-blue-500 text-white" : "bg-gray-200"
                }`}
              >
                <span className="text-lg">{name}</span>
              </li>
            ))}
          </ul>
        </div>

        <div className="flex flex-col w-[50%] mt-3">
          {/* Chat Box */}
          <div
            className="p-3 flex-grow overflow-hidden bg-gray-300 border border-green-500 flex flex-col space-y-2 rounded-md"
            style={{ height: "500px" }}
          >
            {tab === "CHATROOM"
              ? publicChats.map((message, index) => (
                  <div
                    className={`flex ${
                      message.sender !== username
                        ? "justify-start"
                        : "justify-end"
                    }`}
                    key={index}
                  >
                    <div
                      className={`p-2 flex flex-col max-w-lg ${
                        message.sender !== username
                          ? "bg-white rounded-t-lg rounded-r-lg"
                          : "bg-blue-500 rounded-t-lg rounded-l-lg"
                      }`}
                    >
                      {message.sender !== username && (
                        <div className="rounded bg-blue-400 mb-2 p-1 text-white">
                          {message.sender}
                        </div>
                      )}
                      <div
                        className={
                          message.sender === username ? "text-white" : ""
                        }
                      >
                        {message.content}
                      </div>
                      {message.media &&
                        message.media
                          .split(";")[0]
                          .split("/")[0]
                          .split(":")[1] === "image" && (
                          <img src={message.media} alt="" width={"250px"} />
                        )}
                      {message.media &&
                        message.media
                          .split(";")[0]
                          .split("/")[0]
                          .split(":")[1] === "video" && (
                          <video width="320" height="240" controls>
                            <source src={message.media} type="video/mp4" />
                          </video>
                        )}
                    </div>
                  </div>
                ))
              : privateChats.get(tab).map((message, index) => (
                  <div
                    className={`flex ${
                      message.sender !== username
                        ? "justify-start"
                        : "justify-end"
                    }`}
                    key={index}
                  >
                    <div
                      className={`p-2 flex flex-col max-w-lg ${
                        message.sender !== username
                          ? "bg-white rounded-t-lg rounded-r-lg"
                          : "bg-blue-500 rounded-t-lg rounded-l-lg"
                      }`}
                    >
                      <div
                        className={
                          message.sender === username ? "text-white" : ""
                        }
                      >
                        {message.content}
                      </div>
                      {message.media &&
                        message.media
                          .split(";")[0]
                          .split("/")[0]
                          .split(":")[1] === "image" && (
                          <img src={message.media} alt="" width={"250px"} />
                        )}
                      {message.media &&
                        message.media
                          .split(";")[0]
                          .split("/")[0]
                          .split(":")[1] === "video" && (
                          <video width="320" height="240" controls>
                            <source src={message.media} type="video/mp4" />
                          </video>
                        )}
                    </div>
                  </div>
                ))}
          </div>

          {/* Message Box */}
          <div className="flex items-center p-2">
            <input
              className="flex-grow p-2 border outline-blue-600 rounded-l-lg"
              type="text"
              placeholder="Message"
              value={message}
              onKeyUp={(e) => {
                if (e.key === "Enter" || e.key === 13) {
                  tab === "CHATROOM" ? sendMessage() : sendPrivate();
                }
              }}
              onChange={(e) => setMessage(e.target.value)}
            />
            <label
              htmlFor="file"
              className="p-2 bg-blue-700 text-white rounded-r-none cursor-pointer"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="20"
                height="24"
                fill="currentColor"
                className="bi bi-paperclip"
                viewBox="0 0 16 16"
              >
                <path d="M4.5 3a2.5 2.5 0 0 1 5 0v9a1.5 1.5 0 0 1-3 0V5a.5.5 0 0 1 1 0v7a.5.5 0 0 0 1 0V3a1.5 1.5 0 1 0-3 0v9a2.5 2.5 0 0 0 5 0V5a.5.5 0 0 1 1 0v7a3.5 3.5 0 1 1-7 0V3z" />
              </svg>
            </label>
            <input
              id="file"
              type="file"
              onChange={(e) => base64ConversionForImages(e)}
              className="hidden"
            />
            <input
              type="button"
              className="ml-2 p-2 bg-blue-700 text-white rounded cursor-pointer"
              value="Send"
              onClick={tab === "CHATROOM" ? sendMessage : sendPrivate}
            />
            <input
              type="button"
              className="ml-2 p-2 bg-blue-700 text-white rounded cursor-pointer"
              value="Logout"
              onClick={handleLogout}
            />
          </div>
        </div>
        <div className="pl-4 pt-3">
          <SearchBar onUserSelect={handlePrivateMessage} />
        </div>
      </div>
    </div>
  );
};
