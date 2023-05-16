package com.rubal.ds.array;

import java.util.Stack;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LongestValidParenthesis {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        String S = "(()()()(())";
        stack.push(-1);
        int max = 0;
        for(int i=0;i<S.length();i++){
            if(S.charAt(i)=='('){
                stack.push(i);
            }else{
                if(!stack.empty()){
                    stack.pop();
                }
                if(!stack.empty()){
                    max = Math.max(max, i - stack.peek());
                }else{
                    stack.push(i);
                }
            }
        }
        System.out.println(max);
    }
}
