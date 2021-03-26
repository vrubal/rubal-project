package com.rubal.ds.tree.traversal;


public class CheckIfBalancedTree {


    public static boolean checkBalancedTree(TreeNode root){

        if(null != root) {
            if (((null != root.getLeft()
                    && null != root.getRight()) || (root.getLeft() == null && root.getRight() == null))) {
                return true;
            }

            if (checkBalancedTree(root.getLeft()) && checkBalancedTree(root.getRight())) {
                return true;
            }
        }
        return false;
    }


    public static boolean checkBalancedTreee(TreeNode root){
       return false;
    }

    public static void main(String[] args) {
        System.out.println(checkBalancedTree(new TreeNode(3,new TreeNode(2,null,null),new TreeNode(2,null,null))));
    }
}
