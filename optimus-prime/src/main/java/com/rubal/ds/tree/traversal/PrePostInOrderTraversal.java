package com.rubal.ds.tree.traversal;

import java.util.Stack;

public class PrePostInOrderTraversal {

    public static void inorderTraversal_R(TreeNode root){

        if(root!=null){
            inorderTraversal_R(root.getLeft());
            System.out.print(root.getData()+" ");
            inorderTraversal_R(root.getRight());
        }
    }

    public static void inorderTraversal_NR(TreeNode root){
        Stack<TreeNode> s = new Stack<>();
        while(root!=null || !s.isEmpty()){
            while(root != null){
                s.push(root);
                root=root.left;
            }
            root=s.pop();
            System.out.println(root.data);
            s.push(root.right);
        }
    }
    public static void inorderTraversalMorris(TreeNode root){
        TreeNode pre=null;
        TreeNode curr = root;

        while(curr != null){
            if(curr.left == null){
                System.out.println(curr.data);
                curr = curr.right;
            }else{
                pre = curr.left;
                while(pre.right != null && pre.right != curr){
                    pre=pre.right;
                }
                if(pre.right == null){
                    pre.right = curr;
                    curr = curr.left;
                }else{
                    pre.right = null;
                    System.out.println(curr.data);
                    curr = curr.right;
                }
            }
        }
    }

    public static void postOrderTraversalMorris(TreeNode root){
        TreeNode pre=null;
        TreeNode curr = root;

        while(curr != null){
            if(curr.left == null){
                System.out.println(curr.data);
                curr = curr.right;
            }else{
                pre = curr.left;
                while(pre.right != null && pre.right != curr){
                    pre=pre.right;
                }
                if(pre.right == null){
                    pre.right = curr;
                    curr = curr.left;
                }else{
                    pre.right = null;
                    System.out.println(curr.data);
                    curr = curr.right;
                }
            }
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

        /*inorderTraversal_R(root);
        System.out.println();
        preTraversal_R(root);
        System.out.println();
        postTraversal_R(root);*/
    }
}
