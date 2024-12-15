package com.rubal.ds.company;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */

/*
* Reverse the paragraph line reversing the word in place
*
* */
public class ReverseString {

    public String reverse(String word){
        //Assert.is
        int length = word.length();
        char[] arr = word.toCharArray();
        for(int i = 0; i< length/2; i++){
            char c = arr[i];
            arr[i] = arr[length-i-1];
            arr[length-i-1] = c;
        }
        return new String(arr);
    }
    String reverseWords(String input)
    {

         Arrays.stream(input.split("."))
                .map(e->new StringBuilder(e).reverse().toString())
                .forEach(System.out::println);
                return null;
                //.collect(java.util.stream.Collectors.joining("."));
    }
    public String reverseWord(String input){
        StringBuilder sb = new StringBuilder();
        String[] words = input.split(" ");
        //Map.entry()
        List<String> list = Arrays.asList(words);
        List<String> reverseWords = list.stream().map(e -> new StringBuilder(e).reverse().toString()).collect(Collectors.toList());
        for(String s : reverseWords){
            sb.append(s).append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        ReverseString t = new ReverseString();
        String input = "Hello World  t e";
        String result = Arrays.stream(input.split(" "))
                .map(t::reverse)
                .collect(Collectors.joining(" "));
        System.out.println(input);
        System.out.println(result);
        t.reverseWords("i.like.india");
    }

}
