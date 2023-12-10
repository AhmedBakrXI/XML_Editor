// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import java.lang.String;
import java.util.Stack;

public class Undo_Redo {

   static Stack<String> stack_test = new Stack<>();          // main stack
   static Stack<String> stack_test2 = new Stack<>();         // temp stack
   static void puch_Stack(String s)                          // function to save all changes
    {
        stack_test.push(s);
    }
    static String Undo_is_clicked ()                        // function to undo
    {
        stack_test2.push(stack_test.peek());
        stack_test.pop();
        return stack_test.peek();
    }
    static String Redo_is_clicked ()                       // funcion to redo
    {
        stack_test.push(stack_test2.peek());
        stack_test2.pop();
        return stack_test.peek();
    }




}