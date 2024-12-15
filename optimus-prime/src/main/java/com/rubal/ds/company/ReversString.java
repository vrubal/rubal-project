package com.rubal.ds.company;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ReversString {
    //India -- aidnI

    /*
        Class A -- add(int a, int b){
            try{

            }catch(Exception e){
                throw BusinessExprion("")
            }

        }
        Class B -- add(int a, int b){

        }

        try{
        A a = new A();
        a.add(0,5)


     */
    public String reverseGivenString(String input){
        if(null==input || input.length()<=1)
            return input;
        char [] chars = input.toCharArray();
        int start = 0, end = chars.length-1;
        while(start<end){
            char t = chars[start];
            chars[start] = chars[end];
            chars[end] = t;
            start++;
            end--;
        }
        return new String(chars);
    }
}
