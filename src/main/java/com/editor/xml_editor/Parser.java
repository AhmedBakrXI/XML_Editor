package com.editor.xml_editor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.sound.sampled.*;

public class Parser {
    private List<String> userList;
    private List<String> xmlParsed;
    private List<Integer> xmlErrors;

    public List<String> getCorrectedXML() {
        return correctedXML;
    }

    private List<String> correctedXML;
    private String replaceError;
    private int errorCount;

    public Parser() {
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
            } else {
                index = xml.length();
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

    public List<Integer> correctXML() {
        correctedXML = new ArrayList<>();
        List<Integer> errorTagsIndex = new ArrayList<>();
        correctedXML.addAll(xmlParsed);
        int counter = 0;
        while (!checkConsistency(correctedXML)) {
            String tag = replaceError.substring(replaceError.indexOf("</") + 2, replaceError.indexOf(">"));
            for (int i = 0; i < correctedXML.size(); i++) {
                if (correctedXML.get(i).equals("<" + tag + ">")) {
                    if ((tag.equals("id") || tag.equals("name") || tag.equals("body") || tag.equals("topic"))
                            && !correctedXML.get(i + 2).equals("</" + tag + ">")) {
                        errorTagsIndex.add(i + 2);
                        correctedXML.add(i + 2, "</" + tag + ">");
                        break;
                    } else if (tag.equals("follower") && !correctedXML.get(i + 4).equals("</" + tag + ">")) {
                        errorTagsIndex.add(i + 4);
                        correctedXML.add(i + 4, "</" + tag + ">");
                        break;
                    } else if (tag.equals("user")) {
                        int j;
                        for (j = i + 1; j < correctedXML.size(); j++) {
                            if (correctedXML.get(j).equals("<user>") && !correctedXML.get(j - 1).equals("</user>")) {
                                break;
                            } else if (correctedXML.get(j).equals("</users>") && !correctedXML.get(j - 1).equals("</user>")) {
                                break;
                            }
                        }
                        correctedXML.add(j, "</" + tag + ">");
                        errorTagsIndex.add(j);
                        break;
                    } else if (tag.equals("posts")) {
                        int j;
                        for (j = i + 1; j < correctedXML.size(); j++) {
                            if ((correctedXML.get(j).equals("<user>") || correctedXML.get(j).equals("<followers>")) && !correctedXML.get(j - 1).equals("</posts>")) {
                                break;
                            }
                        }
                        correctedXML.add(j, "</" + tag + ">");
                        errorTagsIndex.add(j);
                        break;
                    } else if (tag.equals("followers")) {
                        int j;
                        for (j = i + 1; j < correctedXML.size(); j++) {
                            if (correctedXML.get(j).equals("<user>") && !correctedXML.get(j - 1).equals("</followers>")) {
                                break;
                            }
                        }
                        correctedXML.add(j, "</" + tag + ">");
                        errorTagsIndex.add(j);
                        break;
                    } else if (tag.equals("topics")) {
                        int j;
                        for (j = i + 1; j < correctedXML.size(); j++) {
                            if (correctedXML.get(j).equals("</post>") && !correctedXML.get(j - 1).equals("</topics>")) {
                                break;
                            }
                        }
                        correctedXML.add(j, "</" + tag + ">");
                        errorTagsIndex.add(j);
                        break;
                    }
                }
            }
            counter++;
            if (counter == 10) {
                System.out.println("Can't correct");
                try {
                    runAlarm();
                } catch (Exception e) {
                    System.out.println(e);
                }
//                correctedXML = xmlParsed;
                break;
            }
        }
        errorCount = correctedXML.size() - xmlParsed.size();
        System.out.println("Error Count = " + errorCount);
        System.out.println("Error indices: " + errorTagsIndex);
        System.out.println(correctedXML);
        return errorTagsIndex;
    }

    public static String listToString(List<String> list) {
        StringBuilder xmlString = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
                xmlString.append(list.get(i)).append("\n");
        }
        return xmlString.toString();
    }

    private void runAlarm() throws UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {
        File audioFile = new File(getClass().getResource("/audio/alarm.wav").toURI());
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

//    public static void main(String[] args) {
//        String s = FileHandler.readFile("test.xml");
//        Parser parser = new Parser();
//        parser.parseXML(s);
//        parser.correctXML();
//        List<String> xml = parser.getCorrectedXML();
//    }
}
