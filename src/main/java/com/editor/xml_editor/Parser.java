package com.editor.xml_editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    private List<User> userList;
    private List<String> xmlParsed;
    private List<Integer> xmlErrors;

    Parser() {
        userList = new ArrayList<>();
        xmlErrors = new ArrayList<>();
        xmlParsed = new ArrayList<>();
    }

    public static boolean isOpeningTag(String tag) {
        boolean result = false;

        if (tag.startsWith("<") && tag.endsWith(">") && !tag.contains("/"))
            result = true;

        return result;
    }

    public static boolean isClosingTag(String tag) {
        boolean result = false;

        String s = tag.substring(1, 2);
        if (tag.startsWith("<") && tag.endsWith(">") && s.equals("/"))
            result = true;

        return result;
    }

    public static boolean isTag(String tag) {
        boolean result = false;

        if (tag.startsWith("<") && tag.endsWith(">"))
            result = true;

        return result;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<String> getXmlParsed() {
        return xmlParsed;
    }

    public void setXmlParsed(List<String> xmlParsed) {
        this.xmlParsed = xmlParsed;
    }

    public List<Integer> getXmlErrors() {
        return xmlErrors;
    }

    public void setXmlErrors(List<Integer> xmlErrors) {
        this.xmlErrors = xmlErrors;
    }

    public void parseXML(String xml) {
        int index = 0;

        while (index < xml.length()) {
            int startTagBegin = xml.indexOf('<', index);
            int startTagEnd = xml.indexOf('>', startTagBegin);

            if (startTagEnd != -1 && startTagBegin != -1) {
                String tag = xml.substring(startTagBegin + 1, startTagEnd);

                if (tag.startsWith("/")) {
                    xmlParsed.add("<" + tag.trim() + ">");

                } else {
                    xmlParsed.add("<" + tag.trim() + ">");
                    int endTagBegin = xml.indexOf("</" + tag + ">", startTagEnd);
                    if (endTagBegin != -1) {
                        String s = xml.substring(startTagEnd + 1, endTagBegin);
                        if (!s.contains("<") && !s.contains(">")) {
                            xmlParsed.add(s.trim());
                        }
                    }
                }
                index = startTagEnd + 1;
            }
        }
    }

    public boolean checkConsistency(List<String> xml) {
        int i = 0;
        Stack<String> tagStack = new Stack<>();
        while (i != xml.size()) {
            String tag = xml.get(i);
            if (isTag(tag)) {
                if (isOpeningTag(tag)) {
                    tagStack.push(tag);
                } else if (isClosingTag(tag)) {
                    String openingTag = tagStack.pop();
                    openingTag = openingTag.substring(openingTag.indexOf("<") + 1, openingTag.indexOf(">"));
                    String closingTag = tag.substring(tag.indexOf("</") + 2, tag.indexOf(">"));
                    if (!openingTag.equals(closingTag)) {
                        return false;
                    }
                }
            }
            i++;
        }
        return tagStack.isEmpty();
    }
}
