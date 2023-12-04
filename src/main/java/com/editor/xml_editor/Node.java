package com.editor.xml_editor;/*
 * @author : Ahmed Khaled Abdelmaksod Ebrahim
 * @date   : 3 DEC 2023
 * @brief  : contains the class Node which will be the base unit for the compression Tree 
*/

public class Node implements Comparable<Node>{
    private final int freq;
    private Node leftNode;
    private Node rightNode;


    public Node(Node leftNode,Node rightNode)
    {
        freq = leftNode.getFreq() + rightNode.getFreq();
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }
    public Node(int freq)
    {
        this.freq = freq;
    }

    @Override
    public int compareTo(Node node) {
        if(node == null)
        {
            throw new NullPointerException("Comparing with a null node");
        }
        return Integer.compare(freq,node.getFreq());
    }

    public int getFreq() {
        return freq;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }
}
