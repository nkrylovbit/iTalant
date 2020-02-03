package ru.vnn.nick.italent.dto;

public class UserJSON {
    private long id;
    private String username;
    private String avatarLink;
    private String backgroundLink;
    private String firstname;
    private String lastname;
    private String description;
    private int postcounts;
    private int followers;
    private int following;

    public UserJSON() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public String getBackgroundLink() {
        return backgroundLink;
    }

    public void setBackgroundLink(String backgroundLink) {
        this.backgroundLink = backgroundLink;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPostcounts() {
        return postcounts;
    }

    public void setPostcounts(int postcounts) {
        this.postcounts = postcounts;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "UserJSON{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", avatarLink='" + avatarLink + '\'' +
                ", backgroundLink='" + backgroundLink + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", description='" + description + '\'' +
                ", postcounts=" + postcounts +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }
}
