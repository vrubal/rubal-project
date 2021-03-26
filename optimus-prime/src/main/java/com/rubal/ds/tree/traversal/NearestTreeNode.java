package com.rubal.ds.tree.traversal;

/*
                10
             5     15
           2   6  13   22
         1          14

Input : 12
 */


import java.util.Stack;

public class NearestTreeNode {


    public static int minDiff = Integer.MAX_VALUE;
    public static int findNearestNode(TreeNode root, int val){

            if(root!=null){

                if(Math.abs(root.getData()-val)  < minDiff){
                    minDiff = root.getData();
                }
                if(root.getData()>val){
                    findNearestNode(root.getLeft(), val);
                }else{
                    findNearestNode(root.getRight(), val);
                }

            }

            return minDiff;
    }

    public static void findNearestNodeRec(TreeNode root, int val){
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()){
            TreeNode tmp = stack.pop();
            if(Math.abs(tmp.data - val) < minDiff){
                minDiff = tmp.data;
            }
            if(tmp.left != null)  stack.push(tmp.left);
            if(tmp.right != null) stack.push(tmp.right);
        }
        System.out.println(minDiff);
    }
    public static void main(String[] args) {
        TreeNode root = new TreeNode(10); root.left = new TreeNode(5); root.left.left = new TreeNode(2); root.left.left.left = new TreeNode(1);
        root.left.right = new TreeNode(5); root.right = new TreeNode(15); root.right.left = new TreeNode(13);
        root.right.left.right = new TreeNode(14); root.right.right = new TreeNode(22);

        //TreeNode root = new TreeNode(10); root.left = new TreeNode(5); root.left.left = new TreeNode(2); root.left.left.left = new TreeNode(1); root.left.right = new TreeNode(5); root.right = new TreeNode(12); root.right.left = new TreeNode(11); root.right.right = new TreeNode(13);

        //TreeNode root = new TreeNode(10); root.left = new TreeNode(5); root.left.left = new TreeNode(2); root.left.left.left = new TreeNode(-2); root.left.right = new TreeNode(5); root.right = new TreeNode(12); root.right.left = new TreeNode(11); root.right.right = new TreeNode(13);
        findNearestNodeRec(root, -3);
    }



}
