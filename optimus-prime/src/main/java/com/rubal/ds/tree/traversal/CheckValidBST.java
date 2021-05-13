package com.rubal.ds.tree.traversal;

import java.util.Stack;

public class CheckValidBST {

    public static boolean isValidBST(TreeNode root) {

        if(root==null)
            return true;
        TreeNode curr = root;
        TreeNode prev = null;
        Stack<TreeNode> s = new Stack<>();
        while(!s.isEmpty() || curr != null){

            while(curr != null){
                s.push(curr);
                curr=curr.left;
            }

            TreeNode node = s.pop();
            System.out.println("Prev"+prev);
            System.out.println("Node"+node.data);
            if(prev !=null && node.data <= prev.data){
                return false;
            }
            prev = node;
            curr = node.right;
        }
        return true;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        TreeNode left = new TreeNode(7);
        TreeNode right = new TreeNode(6);

        root.left=left;
        root.right = right;
        System.out.println(isValidBST(root));
    }
}
