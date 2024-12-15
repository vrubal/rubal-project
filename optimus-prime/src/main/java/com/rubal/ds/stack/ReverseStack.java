package com.rubal.ds.stack;

import org.springframework.jdbc.core.metadata.HsqlTableMetaDataProvider;

import java.util.Stack;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 * 5
 * 4
 * 3
 * 2
 * 1
 */

public class ReverseStack {

    public static void addToBottom(Stack<Integer> s , int e){
        if(s.isEmpty())
            s.push(e);
        else{
            int t = s.pop();
            addToBottom(s, e);
            s.push(t);
        }
    }
    public static void reverse(Stack<Integer> s){
        if(s.empty())
            return;
        int t = s.pop();
        reverse(s);
        addToBottom(s,t);

    }
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        reverse(stack);
        stack.stream().forEach(System.out::println);
    }

}
