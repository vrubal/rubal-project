package com.rubal.ds.tree.traversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagonalTraversal {
    static Map<Integer, List<Integer>> map = new HashMap<>();
    public static void printDiagonal(TreeNode root, int d, int i){

        if(root == null){
            return;
        }
        if(d==i){
            System.out.print(root.data+" ");
        }
        printDiagonal(root.left, d, i);
        printDiagonal(root.right, d+1, i);

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
        for (int i = 1; i <= 3; i++) {

            printDiagonal(root, 1, i );
            System.out.println();
        }
        //map.entrySet().stream().forEach(System.out::println);
    }
}
