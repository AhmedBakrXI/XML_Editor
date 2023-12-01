package com.editor.xml_editor;

public class Formatting {
    public static String indentation(String xml){
        StringBuilder indentedXML=new StringBuilder();
        int level=0;
        int n=xml.length();
        for (int i=0;i<n;i++){
            char c=xml.charAt(i);
            if(c=='<') {
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
            indentedXML.append(c);
        }
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
    private static String generateSpaces(int level) {
        StringBuilder spaces= new StringBuilder();
        for(int i=0; i<level; i++){
            spaces.append("    ");
        }
        return spaces.toString();
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
        System.out.println("XML before indentation: \n"+xml);
        String result =indentation(xml);
        System.out.println("XML After indendation: \n"+result);
}
}
