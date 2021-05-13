package com.rubal.ds.tree.traversal;

public class FindKPathSum {




    public static boolean findKPathSum(TreeNode node, int sum){

        if(node==null){
            return sum==0;
        }
        return findKPathSum(node.left, sum-node.data)
                ||  findKPathSum(node.right, sum-node.data);
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

        System.out.println(findKPathSum(root,17));
    }
}
