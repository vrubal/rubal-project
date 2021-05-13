package com.rubal.ds.tree.traversal;

import java.util.Stack;

public class PrintAllPath {

    static Stack<TreeNode> stack = new Stack<>();
    public static void printAllPath(TreeNode node){

        if(node==null){
            return;
        }
        stack.push(node);
        if(node.right==null && node.left==null){
            printPath(stack);
        }
        printAllPath(node.left);
        printAllPath(node.right);
        stack.pop();
    }

    private static void printPath(Stack<TreeNode> stack) {
        System.out.println("Printing path");
        stack.stream().mapToInt(TreeNode::getData).forEach(System.out::println);
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(8);
        TreeNode left = new TreeNode(5);
        TreeNode right = new TreeNode(10);
        left.left=new TreeNode(4);
        left.right=new TreeNode(6);
        right.left = new TreeNode(9);
        right.right = new TreeNode(11);
        root.right=right;
        root.left=left;

        printAllPath(root);
    }
}
