package com.editor.Json;

import java.util.LinkedList;

public class Node {
    private final String name;
    private final String data;
    private boolean visited;
    private int depth;
    private final int index;
    private static int index_counter;
    private final LinkedList<Node> children;
    private Node parent;

    Node(String name, String data) {
        this.name = name;
        this.data = data;
        this.index = index_counter;
        index_counter++;
        this.depth = 0;
        visited = false;
        children = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getIndex() {
        return index;
    }

    public LinkedList<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int similarBrothers() {
        if (parent == null) {
            return 0;
        }

        int similarBrothersCount = 0;
        for (int i = 0; i < getBrothersSize(); i++) {
            if (name.equals(parent.children.get(i).name) && index != parent.children.get(i).index) {
                similarBrothersCount++;
            }
        }

        return similarBrothersCount;
    }

    public boolean isSimilarVisited() {
        if (parent == null) {
            return true;
        }

        for (int i = 0; i < getBrothersSize(); i++) {
            if (parent.children.get(i).similarBrothers() != 0) {
                return !parent.children.get(i).visited;
            }
        }

        return !visited;
    }

    public int lastSimilarBrotherIndex() {
        if (parent == null) {
            return 0;
        }

        int lastSimilarIndex = index;
        for (int i = 0; i < getBrothersSize(); i++) {
            if (name.equals(parent.children.get(i).name)) {
                lastSimilarIndex = parent.children.get(i).index;
            }
        }
        return lastSimilarIndex;
    }


    public int getBrothersSize() {
        int count = 1;
        if (parent == null) {
            return count;
        }
        count = parent.children.size();
        return count;
    }

    public boolean isLastChild() {
        if(parent == null) {
            return true;
        }
        int size = getBrothersSize();
        return index == parent.children.get(size - 1).index;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", visited=" + visited +
                ", depth=" + depth +
                ", index=" + index +
                ", children=" + children +
                ", parent=" + parent +
                '}';
    }
}
