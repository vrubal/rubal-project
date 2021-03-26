package com.rubal.ds.tree.traversal;


public class CheckIfTreeFoldable {

    public static void main(String[] args) {
        System.out.println(isFoldable(new TreeNode(4,null,null)));
    }

    private static boolean checkIfMirror(TreeNode left, TreeNode right) {
        if(left==null && right==null){
            return true;
        }
        if(left==null || right==null){
            return false;
        }
        return checkIfMirror(left.getLeft(), right.getRight())
                && checkIfMirror(left.getRight(), right.getLeft());

    }

    private static boolean isFoldable(TreeNode node) {
        if(node==null){
            return true;
        }
        return checkIfMirror(node.getLeft(), node.getRight());
    }
}
