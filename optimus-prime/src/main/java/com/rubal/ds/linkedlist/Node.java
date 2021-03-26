package com.rubal.ds.linkedlist;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
public class Node {
    @NonNull private int data;
    private Node next;
}
