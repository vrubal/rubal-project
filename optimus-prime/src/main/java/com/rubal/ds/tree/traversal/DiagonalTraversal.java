package com.rubal.ds.tree.traversal;

import java.util.*;
import java.util.stream.Collectors;

public class DiagonalTraversal {
    static Map<Integer, List<Integer>> map = new HashMap<>();
    static Map<Integer, Integer> map2 = new HashMap<>();

    public static void printDiagonal(TreeNode root, int depth){
        if(root == null){
            return;
        }
        List<Integer> orDefault = map.getOrDefault(depth, new ArrayList<>());
        orDefault.add(root.getData());
        map.putIfAbsent(depth, orDefault);

        printDiagonal(root.left, depth+1);
        printDiagonal(root.right, depth);
    }

    public static void main(String[] args) {

        /*
                             8
                          5    10
                         4  6 9  11
         */
        TreeNode root = new TreeNode(8);
        TreeNode left = new TreeNode(5);
        TreeNode right = new TreeNode(10);
        left.left=new TreeNode(4);
        left.right=new TreeNode(6);
        right.left = new TreeNode(9);
        right.right = new TreeNode(11);
        root.right=right;
        root.left=left;
        map.values().forEach(e->new ArrayList().add(e));
        //for (int i = 1; i <= 3; i++) {
            printDiagonal(root, 1 );
            System.out.println();
        //}
    }
}
