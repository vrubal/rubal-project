package com.rubal.ds.tree.traversal;

import lombok.*;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TreeNode {
    @NonNull
    int data;
    TreeNode left;
    TreeNode right;
}
