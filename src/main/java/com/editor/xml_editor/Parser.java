package com.editor.xml_editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    private List<String> userList;
    private List<String> xmlParsed;
    private List<Integer> xmlErrors;
    private List<String> correctedXML;
    private String replaceError;

    Parser() {
        userList = new ArrayList<>();
        xmlErrors = new ArrayList<>();
        xmlParsed = new ArrayList<>();
        replaceError = new String();
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

    public List<String> getUserList() {
        for (int i = 0; i < correctedXML.size(); i++) {
            if (correctedXML.get(i).equals("<user>")) {
                StringBuilder user = new StringBuilder();
                while (!correctedXML.get(i).equals("</user>")) {
                    user.append(correctedXML.get(i)).append("\n");
                    i++;
                }
                user.append("</user>");
                userList.add(user.toString());
            }
        }
        System.out.println(userList);
        return userList;
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
                    int endTagBegin = xml.indexOf("</", startTagEnd);
                    if (endTagBegin != -1) {
                        String s = xml.substring(startTagEnd + 1, endTagBegin);
                        if (!s.contains("<") && !s.contains(">")) {
                            xmlParsed.add(s.trim());
                        } else {
                            xmlParsed.add(s.substring(0, s.indexOf("<")).trim());
                        }
                    }
                }
                index = startTagEnd + 1;
            }
        }

        for (int i = 0; i < xmlParsed.size(); i++) {
            if (xmlParsed.get(i).equals("")) xmlParsed.remove(i);
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
                        replaceError = "</" + openingTag + ">";
                        System.out.println(replaceError);
                        return false;
                    }
                }
            }
            i++;
        }
        return tagStack.isEmpty();
    }

    public List<String> correctXML() {
        correctedXML = new ArrayList<>();
        correctedXML.addAll(xmlParsed);

        while (!checkConsistency(correctedXML)) {
            String tag = replaceError.substring(replaceError.indexOf("</") + 2, replaceError.indexOf(">"));
            for (int i = 0; i < correctedXML.size(); i++) {
                if (correctedXML.get(i).equals("<" + tag + ">")) {
                    if ((tag.equals("id") || tag.equals("name") || tag.equals("body") || tag.equals("topic"))
                            && !correctedXML.get(i + 2).equals("</" + tag + ">")) {
                        correctedXML.add(i + 2, "</" + tag + ">");
                    } else if (tag.equals("follower") && !correctedXML.get(i + 4).equals("</" + tag + ">")) {
                        correctedXML.add(i + 4, "</" + tag + ">");
                    } else if (tag.equals("user") || tag.equals("topics") || tag.equals("followers") || tag.equals("posts")) {
                        // to be implemented
                    }
                }
            }
        }

        return correctedXML;
    }

}
