package com.rubal.ds.graph.google;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
import java.util.*;

public class FindMinimumIndex {

    public static void main(String[] args) {
        List<String> input = new ArrayList<>();
        input.add("a > b");
        input.add("b > c");
        //input.add("a > c");

        int result = findMinimumIndex(input);
        System.out.println("Output: " + result);
    }

    public static int findMinimumIndex(List<String> input) {
        Map<Character, List<Character>> graph = new HashMap<>();

        for (String pair : input) {
            char from = pair.charAt(0);
            char to = pair.charAt(4);
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        Stack<Character> stack = new Stack<>();
        Set<Character> visited = new HashSet<>();

        for (char node : graph.keySet()) {
            if (!visited.contains(node)) {
                topologicalSort(node, graph, visited, stack);
            }
        }

        if (stack.size() != input.size()) {
            return stack.size();
        } else {
            return -1; // No single sorted order exists
        }
    }

    private static void topologicalSort(char node, Map<Character, List<Character>> graph, Set<Character> visited, Stack<Character> stack) {
        visited.add(node);
        List<Character> neighbors = graph.get(node);
        if (neighbors != null) {
            for (char neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    topologicalSort(neighbor, graph, visited, stack);
                }
            }
        }
        stack.push(node);
    }
}

