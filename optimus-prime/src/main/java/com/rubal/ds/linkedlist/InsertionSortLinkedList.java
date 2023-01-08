package com.rubal.ds.linkedlist;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class InsertionSortLinkedList {
    public static Node insertionSortList(Node head) {
        if( head == null ){
            return head;
        }

        Node helper = new Node(0); //new starter of the sorted list
        Node cur = head; //the node will be inserted
        Node pre = helper; //insert node between pre and pre.next
        Node next = null; //the next node will be inserted
        //not the end of input list
        while( cur != null ){
            next = cur.next;
            //find the right place to insert
            while( pre.next != null && pre.next.data< cur.data ){
                pre = pre.next;
            }
            //insert between pre and pre.next
            cur.next = pre.next;
            pre.next = cur;
            pre = helper;
            cur = next;
        }

        return helper.next;
    }

    public static void main(String[] args) {
        Node one = new Node(5);
        Node two = new Node(1);
        Node three = new Node(3);
        Node four = new Node(2);
        Node five = new Node(6);

        one.next=two;
        two.next=three;
        three.next=four;
        four.next=five;
        insertionSortList(one);
    }
}
