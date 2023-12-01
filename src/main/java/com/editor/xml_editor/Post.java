package com.editor.xml_editor;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String body;
    private List<String> topics;

    Post() {
        topics = new ArrayList<>();
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "Post{" +
                "body='" + body + '\'' +
                ", topics=" + topics +
                '}';
    }
}
