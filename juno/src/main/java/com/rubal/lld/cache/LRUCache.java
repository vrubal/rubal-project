package com.rubal.lld.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */

class Node<K,V>{
    K key;
    V value;
    Node next;
    Node prev;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
public class LRUCache<K ,V> {
    int capacity;
    private Map<K, Node<K, V>> map = new HashMap<>();
    Node<K, V> head;
    Node<K, V> tail;
    LRUCache(){
        this.capacity=3;
        this.map = new HashMap<>();
    }

    public V get(K k) {
        Node<K, V> node=map.get(k);
        if(node==null){
            System.out.println("-1");
            return null;
        }else{
            delete(node);
            add(node);
        }
        System.out.println(node.value);
        return node.value;
    }

    public void put(K k, V v) {
        Node e = new Node(k,v);
        if (map.get(k) != null) {
            delete(map.get(k));
        }else if(map.size()==capacity){
            delete(tail);
        }
        add(e);
    }

    private void add(Node<K, V> node) {
        map.put(node.key, node);
        if(head==null) {
            head = node;
            tail = node;
        }else{
            node.next=head;
            head.prev=node;
            head = node;
            node.prev=null;
        }
    }

    private void delete(Node<K, V> node){
        map.remove(node.key);
        Node<K,V> prev = node.prev;
        Node<K,V> next = node.next;
        if(node==head) {
            head = head.next;
            if(head!=null)
                head.prev=null;
        }else if(node==tail){
            tail=tail.prev;
            if(tail!=null)
                tail.next=null;
        }else {
            if (prev != null && next != null) {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    public static void main(String[] args) {
        /*
        GET 73 SET 74 38 GET 80 SET 10 62 SET 11 58 SET 67 47 GET 79 GET 42 GET 26
        SET 4 70 SET 26 25 SET 85 8 SET 70 32 SET 90 5 GET 66 SET 8 52 GET 63 GET 7
        GET 61 SET 55 69 SET 12 46 SET 70 1 SET 27 27 GET 63 SET 32 36 GET 41 GET 50
        SET 1 24 GET 33 GET 96 GET 43 SET 63 76 SET 61 80 SET 61 1 GET 98 GET 25 GET 1
        SET 32 90 GET 82 GET 80 SET 80 28 SET 95 12 GET 3 GET 15 SET 80 4 GET 64 GET 61 SET 11 55
        GET 79 GET 15 GET 54 GET 81 SET 60 55 GET 34 GET 38 SET 45 85 SET 59 74 GET 29 SET 92 90*/
        LRUCache<Integer, Integer> lruCache = new LRUCache();
        lruCache.get(73);lruCache.put(74,38);
        lruCache.get(80);lruCache.put(10,38);lruCache.put(11,38);lruCache.put(67,38);
        lruCache.get(79);
        lruCache.get(42);
        lruCache.get(26);
        lruCache.get(11);
        lruCache.get(11);
        lruCache.get(11);
        System.out.println("Raun");

    }
}
