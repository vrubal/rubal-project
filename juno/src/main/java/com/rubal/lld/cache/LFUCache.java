package com.rubal.lld.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
class DataNode<K,V>{
    DataNode(K key, V value){
        this.key=key;
        this.value=value;
    }
    K key;
    V value;
    DataNode next;
    DataNode prev;
    FrequencyNode<K,V> freq;
}
class LinkedList<K,V>{
    DataNode<K,V> head;
    DataNode<K,V> tail;
    int size;

    void delete(DataNode<K,V> node){
        size--;
        DataNode<K,V>  next = node.next;
        DataNode<K,V>  prev = node.prev;
        if(node==head){
            head=head.next;
            if(head!=null)
                head.prev=null;
        } else if (node == tail) {
            tail=tail.prev;
            tail.next=null;
        } else{
            next.prev=prev;
            prev.next=next;
        }

    }
    void add(DataNode<K,V> node){
        size++;
        if(head==null){
            head=node;
            tail=node;
        }else{
            tail.next=node;
            node.prev=tail;
            tail=tail.next;
        }
    }

    public DataNode<K,V> evict() {
        size--;
        DataNode deleted=head;
        head=head.next;
        if(head!=null)
            head.prev=null;
        return deleted;
    }
}
class FrequencyNode<K,V>{
    FrequencyNode(int freq){
        this.list=new LinkedList<>();
        this.frequency = freq;
    }
    LinkedList<K,V> list;
    FrequencyNode<K,V> next;
    FrequencyNode<K,V> prev;
    int frequency;

}
public class LFUCache<K, V> {
    int capacity;
    Map<K,DataNode<K,V>> valueMap;
    Map<Integer,FrequencyNode<K,V>> frequencyMap;
    FrequencyNode<K, V> head;
    public LFUCache(int capacity){
        this.capacity=capacity;
        valueMap =new HashMap<>();
        frequencyMap=new HashMap<>();
    }

    void put(K key, V value){
        if(capacity==0)
            return;
        DataNode dataNode = valueMap.get(key);
        if(dataNode!=null){
            dataNode.value=value;
            promote(dataNode);
        }else{
            if(valueMap.size()==capacity)
                evict();
            dataNode = new DataNode(key, value);
            valueMap.put(key, dataNode);
            if(head==null ){
                add(new FrequencyNode(1));
            } else if (head.frequency != 1) {
                FrequencyNode newHead = new FrequencyNode(1);
                head.prev = newHead;
                newHead.next = head;
                head = newHead;
            }
            dataNode.freq = head;
            head.list.add(dataNode);
        }
    }



    V get(K key){
        DataNode<K,V> dataNode = valueMap.get(key);
        if(dataNode==null) {
            System.out.println("Res:-1");
            return null;
        }
        V res = promote(dataNode);
        System.out.println("Res:"+res);
        return res;

    }

    private V promote(DataNode<K, V> dataNode) {
        FrequencyNode<K,V> curFrequencyNode = dataNode.freq;
        curFrequencyNode.list.delete(dataNode);
        FrequencyNode<K,V> nextFrequencyNode = curFrequencyNode.next;
        if(nextFrequencyNode==null){
            nextFrequencyNode = new FrequencyNode<>(curFrequencyNode.frequency+1);
            frequencyMap.put(nextFrequencyNode.frequency, nextFrequencyNode);
            nextFrequencyNode.prev = curFrequencyNode;
            curFrequencyNode.next = nextFrequencyNode;
        } else if (nextFrequencyNode.frequency != curFrequencyNode.frequency + 1) {
            nextFrequencyNode = new FrequencyNode<>(curFrequencyNode.frequency+1);
            nextFrequencyNode.next = curFrequencyNode.next;
            nextFrequencyNode.prev = curFrequencyNode;
            curFrequencyNode.next.prev = nextFrequencyNode;
            curFrequencyNode.next = nextFrequencyNode;
        }
        dataNode.freq = nextFrequencyNode;
        nextFrequencyNode.list.add(dataNode);
        if(curFrequencyNode.list.size==0)
            delete(curFrequencyNode);
        return dataNode.value;
    }

    private void evict() {
        if(head==null){
            return;
        }
        DataNode<K, V> evict = head.list.evict();
        valueMap.remove(evict.key);
        if(head.list.size==0)
            delete(head);
    }
    private void delete(FrequencyNode<K, V> node){
        frequencyMap.remove(node.frequency);
        FrequencyNode<K,V> next = node.next;
        FrequencyNode<K,V> prev = node.prev;
        if(node==head){
            head=head.next;
            if(head!=null)
                head.prev = null;
        } else {
            prev.next=next;
            next.prev=prev;
        }
        node.prev=null;
        node.next=null;

    }
    private void add(FrequencyNode<K, V> node){
        frequencyMap.put(node.frequency, node);
        if(head==null){
            head=node;
        }
    }

    public static void main(String[] args) {
        LFUCache<Integer, Integer> lfuCache = new LFUCache(5);
        lfuCache.get(68);
        lfuCache.put(46,24);
        lfuCache.put(46,79);
        lfuCache.get(6);
        lfuCache.put(11,82);
        lfuCache.get(5);
        lfuCache.get(80);
        lfuCache.put(25,46);
        lfuCache.put(72,91);
        lfuCache.get(12);
        lfuCache.get(4);
        lfuCache.get(74);
        lfuCache.get(92);
        lfuCache.put(10,29);
        lfuCache.put(68,83);
        lfuCache.put(45,89);
        lfuCache.get(45);
        lfuCache.put(99,0);
        lfuCache.get(45);
        lfuCache.put(44,13);
        lfuCache.get(68);
        lfuCache.put(99,75);
        lfuCache.get(95);
        lfuCache.get(31);
        lfuCache.get(46);
        lfuCache.get(66);
        lfuCache.put(27,78);
        lfuCache.put(15,5);
        lfuCache.put(24,1);

    }
}
