package com.rubal.ds.tree.traversal;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
public class TrieNode {
    @NonNull
    int data;
    int cost;
    @NonNull
    List<TrieNode> children;
}