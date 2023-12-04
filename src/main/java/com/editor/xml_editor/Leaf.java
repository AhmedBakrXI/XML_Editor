package com.editor.xml_editor;/*
 * @author : Ahmed Khaled Abdelmaksod Ebrahim
 * @date   : 3 DEC 2023
 * @brief  : contains the class Leaf
*/

import com.editor.xml_editor.Node;

public class Leaf extends Node {
    private final char character;

    public Leaf(char character,int freq)
    {
        super(freq);
        this.character = character;
    }

    public char getChar() {
        return character;
    }
}
