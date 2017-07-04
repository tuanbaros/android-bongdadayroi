package app.com.bongdadayroi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuan on 28/03/2016.
 */
public class MyVideo implements Serializable {
    @SerializedName("post_id")
    private String post_id;
    @SerializedName("title")
    private String title;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("avatar_medium")
    private String avatar_medium;
    @SerializedName("description")
    private String description;
    @SerializedName("link")
    private String link;
    @SerializedName("author")
    private String author;
    @SerializedName("category_name")
    private String category_name;
    @SerializedName("num_view")
    private String num_view;
    @SerializedName("num_like")
    private String num_like;
    @SerializedName("video_url")
    private String video_url;
    @SerializedName("list_video")
    private ListVideo[] list_video;
    @SerializedName("link_streamming")
    private String[] link_streamming;

    public String[] getLink_streamming() {
        return link_streamming;
    }

    public void setLink_streamming(String[] link_streamming) {
        this.link_streamming = link_streamming;
    }

    public ListVideo[] getList_video() {
        return list_video;
    }

    public void setList_video(ListVideo[] list_video) {
        this.list_video = list_video;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNum_view() {
        return num_view;
    }

    public void setNum_view(String num_view) {
        this.num_view = num_view;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getNum_like() {
        return num_like;
    }

    public void setNum_like(String num_like) {
        this.num_like = num_like;
    }

    public String getAvatar_medium() {
        return avatar_medium;
    }

    public void setAvatar_medium(String avatar_medium) {
        this.avatar_medium = avatar_medium;
    }
}
