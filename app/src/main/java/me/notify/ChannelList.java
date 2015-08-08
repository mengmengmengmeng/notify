package me.notify;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class ChannelList {
    private String id;
    private String title;
    private String description;
    private String status;
    private String category;
    private String logo;
    private String subscribers_count;
    private String owner_id;

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getStatus(){
        return status;
    }
    public String getCategory() {
        return category;
    }
    public String getLogo() {
        return logo;
    }
    public String getOwner_id() {
        return owner_id;
    }
    public String getSubscribers_count() {
        return subscribers_count;
    }
    public void setChannel(String id, String title, String description, String status, String category,
                           String logo, String subscribers_count, String owner_id) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.category = category;
        this.logo = logo;
        this.subscribers_count = subscribers_count;
        this.owner_id = owner_id;

    }

}