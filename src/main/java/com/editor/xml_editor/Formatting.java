package com.editor.xml_editor;

import java.util.ArrayList;
import java.util.Stack;

public class Formatting {
    public static String indentation(String xml){
        StringBuilder indentedXML=new StringBuilder();
        int level=0;
        int n=xml.length();
        boolean inbody=false;
        // loop on the given XML string and add indentation  based on whether its closing or opening tag
        for (int i=0;i<n;i++){
            char c=xml.charAt(i);
            if(c=='<') {
                // check if the coming tag is body to handle indentation of its text
                if (xml.regionMatches(i, "<body>", 0, 6)) {
                    indentedXML.append(generateSpaces(level));
                    level++;
                    indentedXML.append("<body>");
                    indentedXML.append('\n');
                    i+=7;
                    inbody=true;
                    continue;
                }
                char next=xml.charAt(i+1);
                if(next=='/'){
                    level--;
                    indentedXML.append("\n").append(generateSpaces(level));
                }
                else{
                    indentedXML.append(generateSpaces(level));
                    level++;
                }
            }
            // add indentation to the body of the text
            if(inbody){
                indentedXML.append(generateSpaces(level));
                inbody=false;
            }
            indentedXML.append(c);
            }
        // remove the empty lines
        int index = 0;
        while (index < indentedXML.length()) {
            char currentChar = indentedXML.charAt(index);
            if (currentChar == '\n' && (index == 0 || indentedXML.charAt(index - 1) == '\n')) {
                // Remove the current character (newline) if it's at the beginning or consecutive
                indentedXML.deleteCharAt(index);
            } else {
                // Move to the next character
                index++;
            }
        }

        return indentedXML.toString();
    }
    //generate indentation based on the level
    public static String generateSpaces(int level) {
        StringBuilder spaces= new StringBuilder();
        for(int i=0; i<level; i++){
            spaces.append("\t");
        }
        return spaces.toString();
    }
    // Minifying xml
    public static String minify(String xml){
        String result="";
        result=xml.replaceAll(">\n",">");
        result=xml.replaceAll("\n","");
        return result;
    }
    public static String xmlToJSON(String xml) {
        ArrayList<Node> arr = xmlToArray(xml);
        Node node = arrayToTree(arr);
        StringBuilder sb = new StringBuilder();
        treeToJSON(node, 0, sb);
        return "{\n" + sb + "\n}";
    }

    private static String repeated(String st, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(st);
        }
        return sb.toString();
    }

    private static void treeToJSON(Node node, int tab, StringBuilder p) {
        tab++;

        p.append(repeated("    ", tab));
        if (node.type == NodeType.root) {
            p.append("\"" + node.data + "\"");
            return;
        }

        if (node.type == NodeType.parent) {
            p.append("\"" + node.data + "\": \"" + node.children.get(0).data + "\"");
            return;
        }

        if (node.data != "") p.append("\"" + node.data + "\": ");

        if (node.type == NodeType.repeatedtag)
            p.append("[\n");
        else
            p.append("{\n");

        for (int i = 0; i < node.children.size(); i++) {
            treeToJSON(node.children.get(i), tab, p);

            if (i < node.children.size() - 1)
                p.append(", \n");
            else {
                p.append('\n');
                p.append(repeated("    ", tab));

                if (node.type == NodeType.repeatedtag)
                    p.append("]");
                else
                    p.append("}");
            }
        }
    }

    private static Node arrayToTree(ArrayList<Node> arr) {
        Stack<Node> stack = new Stack<>();
        for (Node current : arr) {
            if (current.type == NodeType.closetag) {
                Node temp = new Node(NodeType.child, current.data);
                Node top = stack.pop();
                while (top.type != NodeType.opentag) {
                    temp.children.add(top);
                    top = stack.pop();
                }
                top = stack.isEmpty() ? null : stack.peek();
                if (!stack.isEmpty() && top.data.equals(current.data)) {
                    top.type = NodeType.repeatedtag;
                    if (temp.children.size() == 1)
                        top.children.add(temp.children.get(0));
                    else {
                        temp.data = "";
                        if (top.duplicated)
                            top.children.add(temp);
                        else {
                            Node ele = new Node(NodeType.child, "");
                            ele.children = top.children;
                            top.children = new ArrayList<>();
                            top.children.add(ele);
                            top.children.add(temp);
                            top.duplicated = true;
                        }
                    }

                } else if (temp.children.size() == 1 && temp.children.get(0).type == NodeType.root) {
                    temp.type = NodeType.parent;
                    stack.push(temp);
                } else
                    stack.push(temp);

            } else {
                stack.push(current);
            }

        }
        return stack.pop();
    }

    private static ArrayList<Node> xmlToArray(String xml) {
        xml=indentation(xml);
        ArrayList<Node> arr = new ArrayList<>();
        for (int i = 0; i < xml.length(); i++) {
            if (xml.charAt(i) == ' ' || xml.charAt(i) == '\n')
                continue;
            StringBuilder sb = new StringBuilder();
            if (xml.charAt(i) == '<') {
                i++;
                boolean ct = false;
                if (xml.charAt(i) == '/') {
                    ct = true;
                    i++;
                }
                while (xml.charAt(i) != '>')
                    sb.append(xml.charAt(i++));
                Node n = new Node(ct ? NodeType.closetag : NodeType.opentag, sb.toString().trim());
                arr.add(n);
            } else {
                while (xml.charAt(i) != '<')
                    sb.append(xml.charAt(i++));
                Node n = new Node(NodeType.root, sb.toString().trim());
                arr.add(n);
                i--;
            }
        }
        return arr;
    }

    private enum NodeType {opentag, closetag, root, parent, child, repeatedtag}

    private static class Node {
        private NodeType type;
        private String data;
        private ArrayList<Node> children;
        private boolean duplicated = false;

        public Node(NodeType t, String d) {
            type = t;
            data = d;
            children = new ArrayList<>();
        }
    }
public static void main(String[] args){
    String xml="<users>\n" +
            "<user>\n" +
            "<id>1</id>\n" +
            "<name>Ahmed Ali</name>\n" +
            "<posts>\n" +
            "<post>\n" +
            "<body>\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
            "</body>\n" +
            "<topics>\n" +
            "<topic>economy</topic>\n" +
            "<topic>finance</topic>\n" +
            "</topics>\n" +
            "</post>\n" +
            "<post>\n" +
            "<body>\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
            "</body>\n" +
            "<topics>\n" +
            "<topic>solar_energy</topic>\n" +
            "</topics>\n" +
            "</post>\n" +
            "</posts>\n" +
            "<followers>\n" +
            "<follower>\n" +
            "<id>2</id>\n" +
            "</follower>\n" +
            "<follower>\n" +
            "<id>3</id>\n" +
            "</follower>\n" +
            "</followers>\n" +
            "</user>\n" +
            "<user>\n" +
            "<id>2</id>\n" +
            "<name>Yasser Ahmed</name>\n" +
            "<posts>\n" +
            "<post>\n" +
            "<body>\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
            "</body>\n" +
            "<topics>\n" +
            "<topic>education</topic>\n" +
            "</topics>\n" +
            "</post>\n" +
            "</posts>\n" +
            "<followers>\n" +
            "<follower>\n" +
            "<id>1</id>\n" +
            "</follower>\n" +
            "</followers>\n" +
            "</user>\n" +
            "<user>\n" +
            "<id>3</id>\n" +
            "<name>Mohamed Sherif</name>\n" +
            "<posts>\n" +
            "<post>\n" +
            "<body>\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
            "</body>\n" +
            "<topics>\n" +
            "<topic>sports</topic>\n" +
            "</topics>\n" +
            "</post>\n" +
            "</posts>\n" +
            "<followers>\n" +
            "<follower>\n" +
            "<id>1</id>\n" +
            "</follower>\n" +
            "</followers>\n" +
            "</user>\n" +
            "</users>";
        System.out.println("original XML: \n"+xml);
        System.out.println("\n\n");
        System.out.println("JSON\n"+xmlToJSON(xml));

}
}
