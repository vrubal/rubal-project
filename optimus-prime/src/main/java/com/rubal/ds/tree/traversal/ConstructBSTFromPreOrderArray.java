package com.rubal.ds.tree.traversal;


/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ConstructBSTFromPreOrderArray {
    static int cnt = 0;
    public static TreeNode build(int arr[], int lim){
        if(cnt==arr.length || lim < arr[cnt])
            return null;

        TreeNode node = new TreeNode(arr[cnt++]);
        node.left = build(arr, node.data);
        node.right = build(arr, lim);
        return node;
    }

    public static void main(String[] args) {
        //int arr[] = new int[]{4,6,5};
        int arr[] = new int[]{5, 4, 6};
        TreeNode node = build(arr, Integer.MAX_VALUE);
        System.out.println(node);
    }
}
