package com.rubal.ds.tree.traversal;

public class PrePostInOrderTraversal {

    public static void inorderTraversal_R(TreeNode root){

        if(root!=null){
            inorderTraversal_R(root.getLeft());
            System.out.print(root.getData()+" ");
            inorderTraversal_R(root.getRight());
        }
    }

    public static void preTraversal_R(TreeNode root){

        if(root!=null){
            System.out.print(root.getData()+" ");
            preTraversal_R(root.getLeft());
            preTraversal_R(root.getRight());
        }
    }

    public static void postTraversal_R(TreeNode root){

        if(root!=null){
            postTraversal_R(root.getLeft());
            postTraversal_R(root.getRight());
            System.out.print(root.getData()+" ");
        }
    }


    public static void main(String[] args) {
        TreeNode left1 = new TreeNode(7,null,null);
        TreeNode right1 = new TreeNode(8,null,null);
        TreeNode left = new TreeNode(2, left1, null);
        TreeNode right = new TreeNode(4, null, right1);
        TreeNode root = new TreeNode(3, left,right);

        inorderTraversal_R(root);
        System.out.println();
        preTraversal_R(root);
        System.out.println();
        postTraversal_R(root);
    }
}
