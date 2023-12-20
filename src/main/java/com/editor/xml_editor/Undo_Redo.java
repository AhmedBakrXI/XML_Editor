// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
package com.editor.xml_editor;

import java.lang.String;
import java.util.Stack;

public class Undo_Redo {

    static Stack<String> stack_test = new Stack<>();          // main stack
    static Stack<String> stack_test2 = new Stack<>();         // temp stack

    static void puch_Stack(String s)                          // function to save all changes
    {
        stack_test.push(s);
    }

    static String Undo_is_clicked()                        // function to undo
    {
        if (!stack_test.isEmpty()) {
            stack_test2.push(stack_test.pop()); // Corrected order of push and pop
            if (!stack_test2.isEmpty()) {
                return stack_test2.peek();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    static String Redo_is_clicked()                       // funcion to redo
    {
        if (!stack_test2.isEmpty()) {
            stack_test.push(stack_test2.pop()); // Corrected order of push and pop
            if (!stack_test.isEmpty()) {
                return stack_test.peek();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}