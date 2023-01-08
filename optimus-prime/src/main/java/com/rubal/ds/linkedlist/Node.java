package com.rubal.ds.linkedlist;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
public class Node {
    @NonNull public int data;
    public Node next;
}
