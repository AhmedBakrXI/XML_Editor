package com.editor.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLTree {
    public Node root;

    public XMLTree(String xml) {
        this.buildTree(xml);
    }

    private static String extractDataSingleTag(String line) {
        line = line.trim();

        int dataStartIndex = line.indexOf('>') + 1;
        int dataEndIndex = line.indexOf("</");

        String dataField;

        if (dataEndIndex == -1) {
            dataField = line.substring(dataStartIndex);
        } else {
            dataField = line.substring(dataStartIndex, dataEndIndex);
        }

        return dataField;
    }



    public void buildTree(String xml) {
        Node currentNode = null;
        int nodeLevel = 0;

        String tagRegex = "<([^<>]+)>";
        Pattern pattern = Pattern.compile(tagRegex);

        try (StringReader reader = new StringReader(xml)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;


            while ((line = bufferedReader.readLine()) != null) {
                Matcher tagMatcher = pattern.matcher(line);

                while (tagMatcher.find()) {
                    String tag = tagMatcher.group();

                    if (tag.charAt(1) != '/') {
                        String dataField = extractDataSingleTag(line);

                        String tagName = tag.substring(1, tag.length() - 1);

                        if (currentNode == null) {
                            currentNode = new Node(tagName, dataField);
                            this.root = currentNode;
                        } else {
                            Node newNode = new Node(tagName, dataField);
                            currentNode.getChildren().add(newNode);
                            newNode.setParent(currentNode);
                            currentNode = newNode;
                        }

                        currentNode.setDepth(nodeLevel);
                        nodeLevel++;
                    } else {
                        nodeLevel--;
                        assert currentNode != null;
                        currentNode = currentNode.getParent();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
