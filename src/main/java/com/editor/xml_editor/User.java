package com.editor.xml_editor;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String username;
    private List<User> followers;
    private List<String> posts;

    public User() {
        followers = new ArrayList<>();
        posts = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", followers=" + followers +
                ", posts=" + posts +
                '}';
    }
}
