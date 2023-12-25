package com.rubal.lld.cache;

import java.util.*;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ConsistentHashingRing {

    int capacity;
    TreeMap<Integer, String> nodes;
    Map<Integer, List<String>> keysMap;

    public ConsistentHashingRing(int capacity) {
        this.capacity = capacity;
        nodes = new TreeMap<>();
        keysMap = new TreeMap<>();
    }

    public void addNode(String node) {
        int hash = getHash(node);
        nodes.put(hash, node);
        keysMap.put(hash, new ArrayList<>());
    }

    public void removeNode(String node) {
        int hash = getHash(node);
        migrateKeys(node, hash);
        nodes.remove(hash);
        keysMap.remove(hash);
    }

    private void migrateKeys(String node, int hash) {
        SortedMap<Integer, String> tailMap = nodes.tailMap(hash, false);
        int newNode;
        if (tailMap.isEmpty()) {
            newNode=nodes.firstEntry().getKey();
        }else{
            newNode=tailMap.firstKey();
        }
        //Migrating the keys of node to next node on right
        keysMap.get(newNode).addAll(keysMap.get(getHash(node)));
    }

    public Integer getNode(String key) {
        if (nodes.isEmpty()) {
            return null;
        }

        int hash = getHash(key);
        SortedMap<Integer, String> tailMap = nodes.tailMap(hash);
        // If no node with a hash greater than or equal to the key's hash is found,
        // wrap around to the first node in the ring
        if (tailMap.isEmpty()) {
            return nodes.firstEntry().getKey();
        }

        return tailMap.firstKey();
    }

    private int getHash(String input) {
        // You can use a real hashing function like MD5 or SHA-1 here
        return input.hashCode();
    }

    public static void main(String[] args) {
        ConsistentHashingRing hashingRing = new ConsistentHashingRing(10);

        // Add nodes to the ring
        hashingRing.addNode("Node-2");
        hashingRing.addNode("Node-1");
        hashingRing.addNode("Node-3");

        // Test the ring with some keys
        hashingRing.addKey("Key-1");
        hashingRing.addKey("Key-2");
        hashingRing.addKey("Key-3");

        System.out.println("Before removing Node-1 keyMap");
        System.out.println(hashingRing.keysMap);
        // Remove a node from the ring and test again
        hashingRing.removeNode("Node-1");

        System.out.println("After removing Node-1 keyMap");
        System.out.println(hashingRing.keysMap);

    }

    private void addKey(String key) {

        Integer node = getNode(key);
        keysMap.get(node).add(key);
        System.out.println("Key: " + key + " is assigned to Node: " + nodes.get(node));
    }
}
