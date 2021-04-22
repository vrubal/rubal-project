package com.rubal.ds.tree.traversal;

import java.util.LinkedList;
import java.util.Queue;

public class GenerateTreeFromString {
    public static  String serialize(TreeNode root){
        StringBuffer s =  new StringBuffer();
        buildString(root,s);
        return s.toString();

    }
    // Encodes a tree to a single string.
    public static void buildString(TreeNode root, StringBuffer s) {
        if(root == null){
            s.append("null,");
        }else{
            s.append(root.data+",");
            buildString(root.left, s);
            buildString(root.right, s);

        }
    }


    public static TreeNode createTree(String[]v, int pos){

        if(pos>v.length-1 || v[pos].equals("null"))
            return null;

        TreeNode root = new TreeNode(Integer.parseInt(v[pos]));
        root.left = createTree(v, 2*pos+1);
        root.right = createTree(v, 2*pos+2);
        return root;
    }
    // Decodes your encoded data to tree.
    public static TreeNode deserialize(String data) {

        String[] v = data.split(",");
        return createTree(v,0);


    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.setLeft(new TreeNode(2));
        TreeNode l =new TreeNode(3);
        TreeNode r =new TreeNode(4);
        root.setRight(l);
        l.setLeft(r);
        l.setRight(new TreeNode(5));
        r.setLeft(new TreeNode(6));
        r.setRight(new TreeNode(7));
        String serialize = serialize(root);
        System.out.println(serialize);
        TreeNode deserialize = deserialize(serialize);

        System.out.println(deserialize);
    }
}
