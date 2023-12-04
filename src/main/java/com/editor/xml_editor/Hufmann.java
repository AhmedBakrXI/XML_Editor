package com.editor.xml_editor;/*
 * @author : Ahmed Khaled Abdelmaksod Ebrahim
 * @date   : 3 DEC 2023
 * @brief  : contains the class hufmann which contains the algorithm of the compression and decompression of the XML file
*/

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import static java.util.Objects.requireNonNull;

public class Hufmann {
    private Node root;
    private final String xmlCode;
    private Map<Character,Integer> charFreq;
    private Map<Character,String> hufmannCharsCodes;

    private void fillCharsFreqMap()
    {
        charFreq = new HashMap<>();
        for(char character : xmlCode.toCharArray())
        {
            Integer integer = charFreq.get(character);
            charFreq.put(character,integer != null ?integer+1:1);
        }
    }
    private void generateHufmannCodes(Node node,String code)
    {
        if (node instanceof Leaf) {
            hufmannCharsCodes.put(((Leaf) node).getChar(), code);
        } else if (node != null) {
            generateHufmannCodes(node.getLeftNode(), code.concat("0"));
            generateHufmannCodes(node.getRightNode(), code.concat("1"));
        }
    }
    private String getEncodedCode()
    {
        StringBuffer sb = new StringBuffer();
        for(char character : xmlCode.toCharArray())
        {
            sb.append(hufmannCharsCodes.get(character));
        }
        return sb.toString();
    }
    private static String repeat(String str, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }

    private static String binaryToString(String binaryString) {
        StringBuilder result = new StringBuilder();

        // Ensure the binary string length is a multiple of 8
        int length = binaryString.length();
        int padding = 8 - (length % 8);
        if (padding < 8) {
            binaryString = repeat("0", padding) + binaryString;
        }

        // Convert each 8 bits to a character
        for (int i = 0; i < binaryString.length(); i += 8) {
            String binaryByte = binaryString.substring(i, i + 8);
            int asciiValue = Integer.parseInt(binaryByte, 2);
            result.append((char) asciiValue);
        }

        return result.toString();
    }
    public Hufmann(String code)
    {
        this.xmlCode = code;
        hufmannCharsCodes = new HashMap<>();
        fillCharsFreqMap();
    }
    private String encode()
    {
        Node nodeLeft;
        Node nodeRight;
        Queue<Node> queue = new PriorityQueue<>();
        charFreq.forEach((character,freq)-> queue.add(new Leaf(character,freq)));
        while(queue.size() > 1)
        {
            nodeLeft = queue.poll();
            nodeRight = queue.poll();
            queue.add(new Node(nodeLeft,nodeRight));
        }

        root = queue.poll();
        if(root != null) {
            generateHufmannCodes(root, "");
        }
        return getEncodedCode();
    }

    private  String decode(String code)
    {
        StringBuffer sb = new StringBuffer();
        Node current = root;
        for(char character : code.toCharArray())
        {
            current = character=='0'?current.getLeftNode():current.getRightNode();
            if(current instanceof Leaf)
            {
                sb.append(((Leaf) current).getChar());
                current = root;
            }
        }
        return sb.toString();
    }
    public String get_xml_encode()
    {
        String s = encode();
        return binaryToString(s);
    }
    public String get_xml_decode()
    {
        String s = encode();
        return decode(s);
    }
}


