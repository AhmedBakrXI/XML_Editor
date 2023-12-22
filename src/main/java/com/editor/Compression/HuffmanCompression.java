
/**
 * The HuffmanCompression class provides methods for Huffman compression and decompression.
 * It uses a Huffman tree to encode and decode data.
 */
package com.editor.Compression;

import java.io.*;
import java.util.PriorityQueue;

public class HuffmanCompression {
    private static final int DATA_RANGE = 256;

    // Private constructor to prevent instantiation
    private HuffmanCompression() {}

    /**
     * Helper method for Huffman compression.
     * @param data The input string to be compressed.
     */
    private static void encodeCompress(String data)
    {
        char[] inChars = data.toCharArray();

        int[] freq = new int[DATA_RANGE];
        for (char inChar : inChars)
            freq[inChar]++;

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] symbolTable = new String[DATA_RANGE];
        buildCodeTable(symbolTable, root, "");

        // write trie in outputPath file for decoding
        writeTrie(root);

        // write the length of the inputPath stream for decoding
        CustomStdOut.write(inChars.length);

        // use huffman compression to encode the outputPath
        for (char ch : inChars) {
            String code = symbolTable[ch];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '1')
                    CustomStdOut.write(true);
                else if (code.charAt(j) == '0')
                    CustomStdOut.write(false);
                else
                    throw new IllegalStateException("either 0 or 1, illegal state");
            }
        }
        CustomStdOut.close();
    }

    /**
     * Compresses data from standard input and writes to standard output.
     */
    public static void compress(){
        // build the char[] inputPath
        String s = CustomStdIn.readString();
        CustomStdIn.close();

        encodeCompress(s);
    }

    /**
     * Compresses a given string and writes the result to a file.
     * @param code The input string to be compressed.
     * @param output The file to write the compressed data.
     */
    public static void compress(String code, File output){

        PrintStream OutStream = System.out;

        try {
            System.setOut(new PrintStream(output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        encodeCompress(code);
        System.setOut(OutStream);
    }

    /**
     * Compresses data from a file and writes the result to another file.
     * @param file_input The file containing data to be compressed.
     * @param file_output The file to write the compressed data.
     */
    public static void compress(File file_input, File file_output)
    {
        InputStream InStream = System.in;
        PrintStream OutStream = System.out;

        try {
            System.setIn(new FileInputStream(file_input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            System.setOut(new PrintStream(file_output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        compress();

        System.setIn(InStream);
        System.setOut(OutStream);
    }

    /**
     * Builds the Huffman trie using a priority queue.
     * @param frequancy The frequency array for each character.
     * @return The root of the Huffman trie.
     */
    private static Node buildTrie(int[] frequancy) {
        // using priority queue
        PriorityQueue<Node> pqNodes = new PriorityQueue<>();

        for (char c = 0; c < DATA_RANGE; c++)
            if (frequancy[c] > 0)
                pqNodes.add(new Node(c, frequancy[c], null, null));

        while (pqNodes.size() > 1) {
            Node left = pqNodes.poll();
            Node right = pqNodes.poll();
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            pqNodes.add(parent);
        }
        return pqNodes.poll();
    }

    /**
     * Writes the Huffman trie to standard output for decoding.
     * @param node The current node in the Huffman trie.
     */
    private static void writeTrie(Node node) {
        if (node.isLeaf()) {
            CustomStdOut.write(true);
            CustomStdOut.write(node.encode_char);
            return;
        }
        CustomStdOut.write(false);
        writeTrie(node.left);
        writeTrie(node.right);
    }

    /**
     * Builds the code table for encoding characters in the Huffman trie.
     * @param treeTable The array to store the binary codes for each character.
     * @param node The current node in the Huffman trie.
     * @param code The binary code for the current node.
     */
    private static void buildCodeTable(String[] treeTable, Node node, String code) {
        if (!node.isLeaf()) {
            buildCodeTable(treeTable, node.left, code + '0');
            buildCodeTable(treeTable, node.right, code + '1');
        }
        else
            treeTable[node.encode_char] = code;

    }

    /**
     * Decompresses data from standard input and writes to standard output.
     */
    public static void decompress() {

        // read the Huffman trie first from the inputPath
        Node root = readTrie();

        // read the length of the stream
        int length = CustomStdIn.readInt();

        for (int i = 0; i < length; i++) {
            Node temp = root;
            while (!temp.isLeaf()) {
                // read bit
                boolean b = CustomStdIn.readBoolean();
                if (b)
                    temp = temp.right;
                else
                    temp = temp.left;
            }
            CustomStdOut.write(temp.encode_char);
        }
        CustomStdIn.close();
        CustomStdOut.close();
    }

    /**
     * Decompresses data from a file and writes the result to another file.
     * @param input The file containing data to be decompressed.
     * @param output The file to write the decompressed data.
     */
    public static void decompress(File input, File output) {
        InputStream InStream = System.in;
        PrintStream OutStream = System.out;

        try {
            System.setIn(new FileInputStream(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            System.setOut(new PrintStream(output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        decompress();

        System.setIn(InStream);
        System.setOut(OutStream);
    }

    /**
     * Reads the Huffman trie from standard input for decoding.
     * @return The root of the Huffman trie.
     */
    private static Node readTrie()
    {
        boolean isLeafNode = CustomStdIn.readBoolean();
        if (isLeafNode)
            return new Node(CustomStdIn.readChar(), -1, null, null);
        else
            return new Node('\0', -1, readTrie(), readTrie());
    }

    /**
     * The Node class represents a node in the Huffman trie.
     */
    private static class Node implements Comparable<Node> {

        private final char encode_char;
        private final int frequency;
        private final Node left;
        private final Node right;

        Node(char ch, int freq, Node left, Node right) {
            this.encode_char = ch;
            this.frequency = freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node that) {
            return Integer.compare(this.frequency, that.frequency);
        }

        private boolean isLeaf() {
            return ((this.left == null) && (this.right == null));
        }
    }
}
