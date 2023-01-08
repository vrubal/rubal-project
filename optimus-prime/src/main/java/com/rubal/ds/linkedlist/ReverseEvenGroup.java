package com.rubal.ds.linkedlist;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ReverseEvenGroup {
    public static Node reverseEvenLengthGroups(Node head) {
        if(head==null || head.next==null)
            return head;
        int count =1;
        Node node = head;
        Node prev = node;
        while(node != null){
            if(count%2==0){
                int len = count;
                Node pHead = node;
                Node node2 = node;
                Node prev2 = null;
                while(len>=1 && node2!=null){
                    Node next = node2.next;
                    node2.next=prev2;
                    prev2=node2;
                    node2=next;
                    len--;
                }
                pHead.next=node2;
                prev.next=prev2;
                node=prev2.next;
            }else{
                int len = count;
                while(len>=1 && node!=null){
                    prev=node;
                    node=node.next;
                    len--;
                }
            }
            if(node!=null)
                node=node.next;
            count++;
        }
        return head;

    }

    public static void main(String[] args) {
        Node one = new Node(1);
        Node two = new Node(2);
        Node three = new Node(3);
        Node four = new Node(4);
        Node five = new Node(5);
        Node six = new Node(6);
        Node seven = new Node(7);
        Node eight = new Node(8);
        Node nine = new Node(9);

        one.next=two;
        two.next=three;
        three.next=four;
        four.next=five;
        five.next=six;
        six.next=seven;
        seven.next=eight;
        eight.next=nine;
        print(reverseEvenLengthGroups(one));
    }

    private static void print(Node node) {
        while(node!=null) {
            System.out.println(node.data);
            node=node.next;
        }
    }
}
