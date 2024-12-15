package com.rubal.ds.linkedlist;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class DeleteDuplicateNode extends BaseLinkedList{
    public void deleteDuplicates(Node head) {
        if(head == null || head.next == null)
            return;

        Node node = head;
        Node dummy = new Node(-1);
        dummy.next = head;
        Node prev = dummy;
        while(node != null ){
            while(node.next != null && node.data==node.next.data){
                node = node.next;
            }
            if(prev.next == node)
                prev = prev.next;
            else
                prev.next = node.next;
            node = node.next;
        }
        this.print(dummy.next);
    }

    public static void main(String[] args) {
        Node one = new Node(1);
        Node two = new Node(1);
        Node three = new Node(2);
        Node four = new Node(4);
        Node five = new Node(5);
        Node six = new Node(3);
        Node seven = new Node(3);
        Node eight = new Node(9);
        Node nine = new Node(9);

        one.next=two;
        two.next=three;
        three.next=four;
        four.next=five;
        five.next=six;
        six.next=seven;
        seven.next=eight;
        eight.next=nine;
        new DeleteDuplicateNode().deleteDuplicates(one);
    }
}
