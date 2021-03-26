package com.rubal.ds.tree.traversal;


import java.util.ArrayDeque;
import java.util.Queue;

public class LevelOrderTraversal {


    public static void levelOrderTraversal(TreeNode root){
        Queue<TreeNode> queue1 = new ArrayDeque<>();
        Queue<TreeNode> queue2 =     new ArrayDeque<>();
        if(null != root){
            queue1.add(root);
        }

        while(!queue1.isEmpty() || !queue2.isEmpty()){

           while(!queue1.isEmpty()){
               TreeNode node = queue1.poll();
               System.out.print(node.getData()+" ");
               pushChild(node, queue2);
           }
            System.out.println("");
           while(!queue2.isEmpty()){
               TreeNode node = queue2.poll();
               System.out.print(node.getData()+" ");
               pushChild(node, queue1);
           }

        }
    }
    public static void levelOrderTraversal_R(TreeNode root){
        int treeHeight = height(root);
        boolean flag = false;
        for(int i=0;i<treeHeight; i++){
            printLevelOrder_R(root, i+1, flag);
            System.out.println("");
            flag=!flag;
        }
    }

    private static void printLevelOrder_R(TreeNode root, int level, boolean flag) {
        if(level == 1){
            System.out.print(root.getData());
        }else if(level> 1 ){
            if(flag) {
                printLevelOrder_R(root.getLeft(), level - 1, flag);
                printLevelOrder_R(root.getRight(), level - 1, flag);
            }else {
                printLevelOrder_R(root.getRight(), level - 1, flag);
                printLevelOrder_R(root.getLeft(), level - 1, flag);
            }
        }
    }

    private static void pushChild(TreeNode node, Queue<TreeNode> queue) {
        if(null != node.getLeft()){
            queue.add(node.getLeft());
        }

        if(null != node.getRight()){
            queue.add(node.getRight());
        }
    }
    private static int height(TreeNode root){
        if(root == null)
            return 0;

        return Math.max(height(root.getLeft())+1, height(root.getRight())+1);
    }
    public static void main(String[] args) {

        //System.out.println(height(new TreeNode(2, new TreeNode(1,null,null) , new TreeNode(3,null,null))));
        //levelOrderTraversal(new TreeNode(2, new TreeNode(1,null,null) , new TreeNode(3,null,null)));
        levelOrderTraversal_R(new TreeNode(2, new TreeNode(1,null,null) , new TreeNode(3,null,null)));
    }
}
