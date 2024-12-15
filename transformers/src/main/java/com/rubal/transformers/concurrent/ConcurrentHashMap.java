package com.rubal.transformers.concurrent;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private List<List<Entry<K, V>>> buckets;
    private List<Lock> locks;
    private int capacity;
    private float loadFactor;
    private int size;

    public ConcurrentHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public ConcurrentHashMap(int initialCapacity, float loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.size = 0;
        this.buckets = new ArrayList<>(capacity);
        this.locks = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) {
            buckets.add(new ArrayList<>());
            locks.add(new ReentrantLock());
        }
    }

    public void put(K key, V value) {
        resizeIfNeeded();

        int bucketIndex = getBucketIndex(key);
        Lock lock = locks.get(bucketIndex);
        lock.lock();
        try {
            List<Entry<K, V>> bucket = buckets.get(bucketIndex);
            Entry<K, V> entry = getEntry(bucket, key);
            if (entry != null) {
                entry.setValue(value);
            } else {
                bucket.add(new Entry<>(key, value));
                size++;
            }
        } finally {
            lock.unlock();
        }
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        Lock lock = locks.get(bucketIndex);
        lock.lock();
        try {
            List<Entry<K, V>> bucket = buckets.get(bucketIndex);
            Entry<K, V> entry = getEntry(bucket, key);
            return entry != null ? entry.getValue() : null;
        } finally {
            lock.unlock();
        }
    }
    public void remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Lock lock = locks.get(bucketIndex);
        lock.lock();
        try {
            List<Entry<K, V>> bucket = buckets.get(bucketIndex);
            Entry<K, V> entry = getEntry(bucket, key);
            if (entry != null) {
                bucket.remove(entry);
                size--;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean containsKey(K key) {
        int bucketIndex = getBucketIndex(key);
        Lock lock = locks.get(bucketIndex);
        lock.lock();
        try {
            List<Entry<K, V>> bucket = buckets.get(bucketIndex);
            return getEntry(bucket, key) != null;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return size;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private Entry<K, V> getEntry(List<Entry<K, V>> bucket, K key) {
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry;
            }
        }
        return null;
    }

    private void resizeIfNeeded() {
        if (size >= capacity * loadFactor) {
            capacity *= 2;
            List<List<Entry<K, V>>> newBuckets = new ArrayList<>(capacity);
            List<Lock> newLocks = new ArrayList<>(capacity);

            for (int i = 0; i < capacity; i++) {
                newBuckets.add(new ArrayList<>());
                newLocks.add(new ReentrantLock());
            }

            // Rehash the existing entries
            for (List<Entry<K, V>> bucket : buckets) {
                for (Entry<K, V> entry : bucket) {
                    int newBucketIndex = getBucketIndex(entry.getKey());
                    newBuckets.get(newBucketIndex).add(entry);
                }
            }

            // Update the buckets and locks references
            buckets = newBuckets;
            locks = newLocks;
        }
    }

    private static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("one1", 2);
        map.put("one2", 3);
        map.put("one3", 4);
        map.put("two1", 3);
        map.put("two2", 4);
        map.put("two3", 5);
        map.put("two4", 6);
        map.entrySet().stream().forEach(System.out::println);

    }
}
