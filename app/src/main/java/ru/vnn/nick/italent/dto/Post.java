package ru.vnn.nick.italent.dto;

import com.google.gson.annotations.SerializedName;

public class Post {

    private long id;
    private String message;
    //    private Author author;
    private String category;
    @SerializedName("post_image")
    private String postImage;
    private int commentsCount;
    private boolean liked;
    private int likesCount;
    private String published_at;

    public Post() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public Author getAuthor() {
//        return author;
//    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", category='" + category + '\'' +
                ", postImage='" + postImage + '\'' +
                ", commentsCount=" + commentsCount +
                ", liked=" + liked +
                ", likesCount=" + likesCount +
                ", published_at='" + published_at + '\'' +
                '}';
    }
//
//    public void setAuthor(Author author) {
//        this.author = author;
//    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImageUrl) {
        this.postImage = postImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }
}
