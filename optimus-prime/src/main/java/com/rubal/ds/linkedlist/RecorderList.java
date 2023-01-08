package com.rubal.ds.linkedlist;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class RecorderList {

    public static void reorderList(Node head) {
        Node node = head;
        Node prev = null;
        int len = 0;
        Node slow=head;
        Node fast=head;
        while(fast.next!=null && fast.next.next!=null){
            slow=slow.next;
            fast=fast.next.next;
        }
        node=slow.next;
        while(node!=null){
            Node next = node.next;
            node.next = prev;
            prev = node;
            node = next;
        }
        slow.next=prev;
        node = head;
        Node p2=slow.next;
        while(node!=slow){
            slow.next=p2.next;
            p2.next=node.next;
            node.next=p2;
            node=p2.next;
            p2=slow.next;
        }

        //return head;
    }

    public static void main(String[] args) {
        Node one = new Node(1);
        Node two = new Node(2);
        Node three = new Node(3);
        Node four = new Node(4);
        Node five = new Node(5);

        one.next=two;
        two.next=three;
        three.next=four;
        four.next=five;
        reorderList(one);

    }
}
