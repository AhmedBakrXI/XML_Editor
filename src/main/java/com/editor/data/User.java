package com.editor.data;

import com.editor.xml_editor.FileHandler;
import com.editor.xml_editor.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String username;
    private List<User> followers;
    private List<Integer> followersID;
    private List<Post> posts;

    private String userXML;

    public User() {
        followers = new ArrayList<>();
        posts = new ArrayList<>();
        followersID = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {
        String xml = FileHandler.readFile("example.xml");
//        xml.replaceAll();
        Parser parser = new Parser();
        parser.parseXML(xml);
        List<String> xmlList = parser.getXmlParsed();
        parser.correctXML();
        List<String> list1 = parser.getUserList();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            User user = new User();
            user.setUserXML(list1.get(i));
            user.parseData();
            users.add(user);
        }
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setFollowers(users);
        }

        System.out.println(users.get(0).getUsername());
    }

    public String getUserXML() {
        return userXML;
    }

    public void setUserXML(String userXML) {
        this.userXML = userXML;
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

    public void setFollowers(List<User> users) {
        for (Integer followerID : this.followersID) {
            for (User user : users) {
                if (user.getId() == followerID) {
                    followers.add(user);
                }
            }
        }
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id + "\n" +
                ", username='" + username + '\'' + "\n" +
                ", followers=" + followers + "\n" +
                ", followersID=" + followersID + "\n" +
                ", posts=" + posts + "\n" +
                '}';
    }

    public void parseData() {
        String[] user = userXML.split("\n");
        for (int i = 1; i < user.length - 1; i++) {
            if (user[i].equals("<id>")) {
                i++;
                this.id = Integer.parseInt(user[i]);
                i += 2;
            }
            if (user[i].equals("<name>")) {
                i++;
                this.username = user[i];
                i += 2;
            }
            if (user[i].equals("<posts>")) {
                while (!user[i].equals("</posts>")) {
                    i++;
                    if (user[i].equals("<post>")) {
                        Post post = new Post();
                        i++;
                        if (user[i].equals("<body>")) {
                            i++;
                            post.setBody(user[i]);
                            i += 2;
                        }
                        if (user[i].equals("<topics>")) {
                            while (!user[i].equals("</topics>")) {
                                i++;
                                if (user[i].equals("<topic>")) {
                                    i++;
                                    String topic = user[i];
                                    post.getTopics().add(topic);
                                    i++;
                                }
                            }
                        }
                        posts.add(post);
                    }
                }
            }
            if (user[i].equals("<followers>")) {
                while (!user[i].equals("</followers>")) {
                    i++;
                    if (user[i].equals("<follower>")) {
                        Integer followerID = new Integer(-1);
                        i++;
                        if (user[i].equals("<id>")) {
                            i++;
                            followerID = Integer.parseInt(user[i]);
                            i++;
                        }
                        this.followersID.add(followerID);
                    }
                }
            }
        }
    }
}
