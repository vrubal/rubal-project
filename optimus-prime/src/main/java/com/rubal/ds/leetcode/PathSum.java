package com.rubal.ds.leetcode;

import com.rubal.ds.tree.traversal.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class PathSum {
    public static void findPath(TreeNode r, List<List<Integer>> l, List<Integer> tl, int t){
        if(r==null)return;
        tl.add(r.getData());
        t=t-r.getData();
        if(r.getLeft()==null && r.getRight()==null){
            l.add(new ArrayList<>(tl));
            tl.remove(tl.size()-1);
            return;
        }

        findPath(r.getLeft(), l, tl, t);
        if(tl.size()>0)tl.remove(tl.size()-1);
        findPath(r.getRight(), l, tl, t);
        if(tl.size()>0)tl.remove(tl.size()-1);


    }
    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> l = new ArrayList();
        List<Integer> t = new ArrayList();
        findPath(root, l, t, targetSum);
        return l;
    }

    public static void main(String[] args) {
        TreeNode t = new TreeNode(3, new TreeNode(2, new TreeNode(1),null),
                new TreeNode(5, new TreeNode(4), new TreeNode(6)));

        System.out.println(pathSum(t, 6));
    }
}
