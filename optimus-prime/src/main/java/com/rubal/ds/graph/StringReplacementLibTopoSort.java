package com.rubal.ds.graph;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class StringReplacementLibTopoSort {
    static Set<String> placeHolders(String text) {
        Pattern pattern = Pattern.compile("%(.+?)%");
        Matcher matcher = pattern.matcher(text);
        Set<String> set = new HashSet<>();
        while (matcher.find())
            set.add(matcher.group(1));
        return set;
    }

    static Map<String, String> normalize(Map<String, String> dict) {
        Queue<String> queue = new LinkedList<>();

        //~ build graph
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Set<String>> consumes = new HashMap<>();
        for(String key : dict.keySet()) {
            Set<String> phs = placeHolders(dict.get(key));
            consumes.putIfAbsent(key, new HashSet<>());
            consumes.get(key).addAll(phs);
            for(String ph : phs) {
                graph.putIfAbsent(ph, new HashSet<>());
                graph.get(ph).add(key);
            }
            if(consumes.get(key).isEmpty()) queue.add(key);
        }

        //top sort
        Set<String> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            String u = queue.poll();
            //if(visited.contains(u)) throw new IllegalArgumentException("can't resolve all placeholders");
            visited.add(u);
            Set<String> v = graph.getOrDefault(u, new HashSet<>());
            for(String next : v) {
                String value = dict.get(next);
                value = value.replaceAll("%"+u+"%", dict.get(u));
                dict.put(next, value);
                consumes.get(next).remove(u);
                if(consumes.get(next).isEmpty()) queue.add(next);
            }
        }

        if(visited.size() != dict.size()) throw  new IllegalArgumentException("unresolved place holders");

        return dict;
    }
    public static void main(String[] args) {
        Map<String, String> dict = new HashMap<>();
        dict.put("USER","vrubal");
        dict.put("HOME","/root/%USER%");
        normalize(dict);
        String text = "My %HOME%";
            Set<String> ph = placeHolders(text);
            for (String key : ph)
                text = text.replaceAll("%"+key+"%", dict.get(key));
        System.out.println(text);
    }
}
