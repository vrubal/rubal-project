package com.rubal.ds.tree.traversal;

import lombok.*;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TreeNode {
    @NonNull
    int data;
    TreeNode left;
    TreeNode right;
}
