package com.rubal.ds.tree.traversal;

import java.util.*;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class TreeView {
    public static void topViewOfTree(TreeNode root, Map<Integer, Integer> left, Map<Integer, Integer> right, int level){
        if(root==null) return;

        left.putIfAbsent(level, root.data);
        right.put(level, root.data);
        topViewOfTree(root.left, left, right, level+1);
        topViewOfTree(root.right, left, right, level+1);

    }
    public static void bottomViewOfTree(TreeNode root, Map map, int level){
        if(root==null) return;
        Queue<TreeNode> q =     new ArrayDeque<>();
        q.add(root);
        root.lvl=0;
        int idx = 0;
        while(!q.isEmpty()){
            TreeNode t = q.remove();
            int lvl = t.lvl;
            map.putIfAbsent(lvl, t.data);
            if(t.left!=null) {
                t.left.lvl=lvl-1;
                q.add(t.left);
            }
            if(t.right!=null) {
                t.right.lvl=lvl+1;
                q.add(t.right);
            }

        }
    }

    //Function to return a list containing the bottom view of the given tree.
    public static void main(String[] args) {
        /*
                             8
                          5    10
                         4  6 9  11
         */
     TreeNode root = new TreeNode(8);
        TreeNode left = new TreeNode(5);
        TreeNode right = new TreeNode(10);
        //root.left=left;
        root.right=right;
        /*left.left=new TreeNode(4);
        left.right=new TreeNode(6);
        right.left = new TreeNode(9);
        right.right = new TreeNode(11);*/
        // Code here
        Map<Integer, Integer> leftMap = new TreeMap<>();
        Map<Integer, Integer> rightMap = new TreeMap<>();
        bottomViewOfTree(root, leftMap, 0);
        //topViewOfTree(root, leftMap,rightMap, 0);
        ArrayList<Integer> ans=new ArrayList<>();

        for(Map.Entry<Integer,Integer> iterator:leftMap.entrySet())
        {
            ans.add(iterator.getValue());
        }
        /*rightMap.remove(0);
        for(Map.Entry<Integer,Integer> iterator:rightMap.entrySet())
        {
            ans.add(iterator.getValue());
        }*/
        System.out.println(ans);
    }

}
