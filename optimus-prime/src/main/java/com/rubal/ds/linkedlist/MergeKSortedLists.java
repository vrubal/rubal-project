package com.rubal.ds.linkedlist;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class MergeKSortedLists {
    public static Node mergeKLists(Node[] l) {
        Node finalNode = null;
        int val = Integer.MAX_VALUE;
        //Stack<Node> s = new Stack<>();
        Node smallestCurrNode = null;
        int smallestCurrNodeList = -1;
        boolean notEnd = true;
        while(true){
            smallestCurrNode = null;
            for(int i=0;i<l.length;i++){
                if(l[i] == null)
                    continue;
                if(smallestCurrNode==null)
                    smallestCurrNode = l[i];
                else if(l[i].data < smallestCurrNode.data){
                    smallestCurrNode = l[i];
                    smallestCurrNodeList = i;
                    System.out.println(smallestCurrNodeList+" "+smallestCurrNode.data);
                }
                smallestCurrNodeList=i;
            }
            if(smallestCurrNode==null)
                break;
            else
                l[smallestCurrNodeList] = l[smallestCurrNodeList].next;
        }
        return finalNode;
    }
    public static Node mergeKLists2(Node[] lists) {
        // create an empty min-heap using a comparison object for ordering the min-heap
        PriorityQueue<Node> pq;
        pq = new PriorityQueue<>(Comparator.comparingInt(a -> ((Node) a).data));

        for (int i = 0; i < lists.length; i++) {
            if(lists[i]!=null)
            pq.add(lists[i]);
        }

        // take two pointers, head and tail, where the head points to the first node
        // of the output list and tail points to its last node
        Node head = null, last = null;

        // run till min-heap is empty
        while (!pq.isEmpty())
        {
            // extract the minimum node from the min-heap
            Node min = pq.poll();

            // add the minimum node to the output list
            if (head == null) {
                head = last = min;
            }
            else {
                last.next = min;
                last = min;
            }

            // take the next node from the "same" list and insert it into the min-heap
            if (min.next != null) {
                pq.add(min.next);
            }
        }
        return head;

    }
    static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node e1, Node e2){
            return Integer.valueOf(e1.data).compareTo(Integer.valueOf(e2.data));
        }
    }

    public static void main(String[] args) {
        Node one = new Node(1);
        Node two = new Node(1);
        Node three = new Node(3);
        one.next = two;
        two.next = three;
        Node four = new Node(1);
        Node []arr = new Node[]{one,four};
        mergeKLists2(arr);

    }
}
