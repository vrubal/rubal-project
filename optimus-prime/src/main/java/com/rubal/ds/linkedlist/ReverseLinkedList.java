package com.rubal.ds.linkedlist;

public class ReverseLinkedList {
    public static Node reverseRec(Node node){
        if(node==null){
            return null;
        }
        if(node.getNext()==null){
            return node;
        }
        Node newHead = reverseRec(node.getNext());
        node.getNext().setNext(node);
        node.setNext(null);
        return newHead;
    }
    public static Node reverseNonRec(Node node){
        if(node==null){
            return null;
        }
        Node prev = null;
        while(node!=null) {
            Node next = node.getNext();
            node.setNext(prev);
            prev=node;
            node = next;
        }
        return prev;
    }

    public static Node kReverseNonRec(Node node, int k){
        if(node == null)
            return null;
        Node next = null;
        Node prev = null;
        Node newHead = null;
        Node head=node;
        Node oPrev = head;

        while(node != null) {
            /* Reverse first k nodes of linked list */
            int count = 0;
            prev=null;
            Node start = node;
            while (count < k && node != null) {
                next = node.getNext();
                node.setNext(prev);
                prev = node;
                node = next;
                count++;
            }
            if(null == newHead){
                newHead=prev;
            }else{
                oPrev.setNext(prev);
                oPrev=start;
                oPrev.setNext(null);
            }

        }

        /* next is now a pointer to (k+1)th node
           Recursively call for the list starting from
           current. And make rest of the list as next of
           first node */
        /*if (next != null)
            head.setNext(kReverseNonRec(next, k));*/

        // prev is now head of input list
        return newHead;
    }
    public static void main(String[] args) {
        Node one = new Node(1);
        Node two = new Node(2);
        Node three = new Node(3);
        Node four = new Node(4);
        Node five = new Node(5);

        one.setNext(two);
        two.setNext(three);
        three.setNext(four);
        four.setNext(five);
      //  five.setNext(six);


        //Node reverse = reverseRec(one);
        //Node reverse = reverseNonRec(one);

        /*while (one!=null) {
            System.out.println(one.getData());
            one=one.getNext();
        }*/
        Node reverse = kReverseNonRec(one, 6);

        while(reverse!=null){
            System.out.println(reverse.getData());
            reverse=reverse.getNext();
        }
        /*while(head!=null){
            System.out.println(head.getData());
            head=head.getNext();
        }*/

    }
}
