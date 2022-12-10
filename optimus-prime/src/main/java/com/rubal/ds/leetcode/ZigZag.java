package com.rubal.ds.leetcode;

import com.rubal.ds.tree.traversal.TreeNode;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class ZigZag {
    static TreeNode nw = new TreeNode(0);
    static Stack<TreeNode> stack = new Stack<>();
    public static void traversal(List<List<Integer>> l, TreeNode r, int h){
        if(r==null)
            return;
        if(h>=l.size()){
            l.add(new ArrayList<>());
        }
        l.get(h).add(r.getData());
        traversal(l, r.getRight(), h+1 );
        traversal(l, r.getLeft(), h+1);
    }
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> l = new ArrayList();
        traversal(l, root, 0);

        for(int i=0;i<l.size();i++){
            if(i==i){
                Collections.reverse(l.get(i));
            }
        }
        return l;
    }
    public static void buildPre(TreeNode r, TreeNode nw){
        if(r==null){
            return;
        }
        TreeNode t = new TreeNode(r.getData());
        stack.peek().setRight(t);
        stack.push(t);
        System.out.println(r.getData());
        buildPre(r.getLeft(), nw);
        buildPre(r.getRight(), nw);
    }

    public static void main(String[] args) {
        TreeNode t = new TreeNode(3, new TreeNode(2, new TreeNode(1),null),
                new TreeNode(5, new TreeNode(4), new TreeNode(6)));
        //zigzagLevelOrder(t);
        TreeNode tmp = new TreeNode(0);
        stack.push(nw);
        buildPre(t, nw);
        stack.stream().forEach(e->{
            val e1 = e;
            System.out.println(e1.getData());
        });
    }
}
