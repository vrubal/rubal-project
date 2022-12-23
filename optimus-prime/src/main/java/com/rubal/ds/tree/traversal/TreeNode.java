package com.rubal.ds.tree.traversal;

import lombok.*;

import java.beans.ConstructorProperties;


@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TreeNode {
    public TreeNode(@NonNull int data, TreeNode left, TreeNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }
    public TreeNode(@NonNull int data) {
        this.data = data;
    }
    @NonNull
    int data;
    TreeNode left;
    TreeNode right;
    int lvl;
}
