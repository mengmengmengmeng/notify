package me.notify;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class AnnouncementList {
    private String id;
    private String title;
    private String message;
    private String created_at;

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getMessage() {
        return message;
    }
    public String getCreated_at(){
        return created_at;
    }

    public void setAnnouncement(String id, String title, String message, String created_at) {

        this.id = id;
        this.title = title;
        this.message = message;
        this.created_at = created_at;

    }

}