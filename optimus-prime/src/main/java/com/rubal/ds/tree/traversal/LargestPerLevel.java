package com.rubal.ds.tree.traversal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LargestPerLevel {
    public static void findLargetPerRow(TreeNode root, Map<Integer, Integer> map, int lvl){
        if(root==null)
            return;
        if(!map.containsKey(lvl))
            map.computeIfPresent(lvl, (o, n) ->{
                System.out.println(o+" "+ n);
                return null;
            });
        else {
            if(map.containsKey(lvl) && map.get(lvl) < root.data)
                map.put(lvl, root.data);
        }
        findLargetPerRow(root.left, map, lvl++);
        findLargetPerRow(root.right,map, lvl++);
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
        Map<Integer, Integer> map = new HashMap<>();
        findLargetPerRow(root, map, 0);
    }
}
