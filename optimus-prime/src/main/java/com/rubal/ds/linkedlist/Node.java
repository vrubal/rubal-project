package com.rubal.ds.linkedlist;

import lombok.*;

import java.util.Objects;

@Setter
@Getter
@RequiredArgsConstructor
public class Node implements Comparable<Node>{
    @NonNull public int data;
    public Node next;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return data == node.data && Objects.equals(next, node.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, next);
    }

    @Override
    public int compareTo(Node o) {
       return Integer.valueOf(this.data).compareTo(Integer.valueOf(o.data));
    }
}
