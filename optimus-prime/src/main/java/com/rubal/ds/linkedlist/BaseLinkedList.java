package com.rubal.ds.linkedlist;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class BaseLinkedList {
    public void print(Node node){
        while(node!=null){
            System.out.println(node.data);
            node=node.next;
        }
    }
}
