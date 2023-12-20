package com.editor.xml_editor;
import java.util.LinkedList;

public class Post_search {
    static LinkedList<String> PostList = new LinkedList<>();

    void FillTheLinkedList(LinkedList<String> Linked)                // function to get the list of posts to be searched

    {
        for (int i = 0 ;i < getLinkedListLength(Linked); i++)
        {
            PostList.add(Linked.get(i));
        }
    }
     LinkedList<String> GetThePost (String s)                          // function to get the searched posts
    {
        LinkedList<String> ArrayOfSucceeded = new LinkedList<>();     // linked list for true posts
        for (int i = 0 ; i< getLinkedListLength(PostList); i++)       // iteration to find the true posts
        {

            String temp2 = PostList.get(i);                           // string to put the current node's post
            int flag=0;                                               // flag to find if input == post
            for (int j = 0 ; j < s.length(); j ++)                    // iteration to compare input with post
            {
                if (s.charAt(j) != temp2.charAt(j) )                  // compare
                {
                    flag=1;                                           // increment of flag
                    break;
                }
            }
            if (flag == 0)                                   // if flag = input length
            {
                ArrayOfSucceeded.add(temp2);                          // add this post with true posts
            }
        }
        if (getLinkedListLength(ArrayOfSucceeded)==0)                // if no true posts
        {
                    ArrayOfSucceeded.add("!Not Found");
            return ArrayOfSucceeded;
        }
        return ArrayOfSucceeded;
    }

    private static <T> int getLinkedListLength(LinkedList<T> list) {      // function to get length of linked list
        int count = 0;
        for (T element : list) {
            count++;
        }
        return count;
    }
}
