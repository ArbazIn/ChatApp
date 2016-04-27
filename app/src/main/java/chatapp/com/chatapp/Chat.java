package chatapp.com.chatapp;

/**
 * @author greg
 * @since 6/21/13
 */
public class Chat {

    public String message;
    public String author;
    public String chat_time;
    public String time_stamp;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Chat() {
    }

    public Chat(String message, String author, String chat_time,String time_stamp) {
        this.message = message;
        this.author = author;
        this.chat_time = chat_time;
        this.time_stamp = time_stamp;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getChat_time() {
        return chat_time;
    }

    public String getTime_stamp() {
        return time_stamp;
    }


}
