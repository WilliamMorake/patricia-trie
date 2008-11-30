/*
 * Copyright 2005-2008 Roger Kapsi, Sam Berlin
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * A collection of {@link Trie} utilities
 */
public class TrieUtils {

    private TrieUtils() {}
    
    /**
     * Returns true if both values are either null or equal
     */
    static boolean compare(Object a, Object b) {
        return (a == null ? b == null : a.equals(b));
    }
    
    /**
     * Returns a synchronized instance of a {@link Trie}
     * 
     * See {@link Collections#synchronizedSortedMap(SortedMap)}
     */
    public static <K, V> Trie<K, V> synchronizedTrie(Trie<K, V> trie) {
        if (trie instanceof SynchronizedTrie) {
            return trie;
        }
        
        return new SynchronizedTrie<K, V>(trie);
    }
    
    /**
     * A synchronized {@link Trie}
     */
    private static class SynchronizedTrie<K, V> implements Trie<K, V> {
        
        private final Trie<K, V> delegate;
        
        private SynchronizedTrie(Trie<K, V> delegate) {
            if (delegate == null) {
                throw new NullPointerException("delegate");
            }
            
            this.delegate = delegate;
        }

        @Override
        public synchronized SortedMap<K, V> getPrefixedBy(K key, int offset, int length) {
            return new SynchronizedSortedMap<K, V>(this, delegate.getPrefixedBy(key, offset, length));
        }

        @Override
        public synchronized SortedMap<K, V> getPrefixedBy(K key, int length) {
            return new SynchronizedSortedMap<K, V>(this, delegate.getPrefixedBy(key, length));
        }

        @Override
        public synchronized SortedMap<K, V> getPrefixedBy(K key) {
            return new SynchronizedSortedMap<K, V>(this, delegate.getPrefixedBy(key));
        }

        @Override
        public synchronized SortedMap<K, V> getPrefixedByBits(K key, int lengthInBits) {
            return new SynchronizedSortedMap<K, V>(this, delegate.getPrefixedByBits(key, lengthInBits));
        }

        @Override
        public synchronized Entry<K, V> select(K key, Cursor<? super K, ? super V> cursor) {
            return delegate.select(key, cursor);
        }

        @Override
        public synchronized Entry<K, V> select(K key) {
            return delegate.select(key);
        }

        @Override
        public synchronized K selectKey(K key) {
            return delegate.selectKey(key);
        }

        @Override
        public synchronized V selectValue(K key) {
            return delegate.selectValue(key);
        }

        @Override
        public synchronized Entry<K, V> traverse(Cursor<? super K, ? super V> cursor) {
            return delegate.traverse(cursor);
        }

        @Override
        public synchronized Comparator<? super K> comparator() {
            return delegate.comparator();
        }

        @Override
        public synchronized Set<Entry<K, V>> entrySet() {
            return new SynchronizedSet<Entry<K, V>>(this, delegate.entrySet());
        }

        @Override
        public synchronized K firstKey() {
            return delegate.firstKey();
        }

        @Override
        public synchronized SortedMap<K, V> headMap(K toKey) {
            return new SynchronizedSortedMap<K, V>(this, delegate.headMap(toKey));
        }

        @Override
        public synchronized Set<K> keySet() {
            return new SynchronizedSet<K>(this, delegate.keySet());
        }

        @Override
        public synchronized K lastKey() {
            return delegate.lastKey();
        }

        @Override
        public synchronized SortedMap<K, V> subMap(K fromKey, K toKey) {
            return new SynchronizedSortedMap<K, V>(this, delegate.subMap(fromKey, toKey));
        }

        @Override
        public synchronized SortedMap<K, V> tailMap(K fromKey) {
            return new SynchronizedSortedMap<K, V>(this, delegate.tailMap(fromKey));
        }

        @Override
        public synchronized Collection<V> values() {
            return new SynchronizedCollection<V>(this, delegate.values());
        }

        @Override
        public synchronized void clear() {
            delegate.clear();
        }

        @Override
        public synchronized boolean containsKey(Object key) {
            return delegate.containsKey(key);
        }

        @Override
        public synchronized boolean containsValue(Object value) {
            return delegate.containsValue(value);
        }

        @Override
        public synchronized V get(Object key) {
            return delegate.get(key);
        }

        @Override
        public synchronized boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public synchronized V put(K key, V value) {
            return delegate.put(key, value);
        }

        @Override
        public synchronized void putAll(Map<? extends K, ? extends V> m) {
            delegate.putAll(m);
        }

        @Override
        public synchronized V remove(Object key) {
            return delegate.remove(key);
        }

        @Override
        public synchronized int size() {
            return delegate.size();
        }
        
        @Override
        public synchronized int hashCode() {
            return delegate.hashCode();
        }
        
        @Override
        public synchronized boolean equals(Object obj) {
            return delegate.equals(obj);
        }
        
        @Override
        public synchronized String toString() {
            return delegate.toString();
        }
    }
    
    /**
     * A synchronized {@link Collection}
     */
    private static class SynchronizedCollection<E> implements Collection<E> {
        
        private final Object lock;
        
        private final Collection<E> delegate;
        
        public SynchronizedCollection(final Object lock, final Collection<E> delegate) {
            if (lock == null) {
                throw new NullPointerException("lock");
            }
            
            if (delegate == null) {
                throw new NullPointerException("delegate");
            }
            
            this.lock = lock;
            this.delegate = delegate;
        }

        @Override
        public boolean add(final E e) {
            synchronized (lock) {
                return delegate.add(e);
            }
        }

        @Override
        public boolean addAll(final Collection<? extends E> c) {
            synchronized (lock) {
                return delegate.addAll(c);
            }
        }

        @Override
        public void clear() {
            synchronized (lock) {
                delegate.clear();
            }
        }

        @Override
        public boolean contains(final Object o) {
            synchronized (lock) {
                return delegate.contains(o);
            }
        }

        @Override
        public boolean containsAll(final Collection<?> c) {
            synchronized (lock) {
                return delegate.containsAll(c);
            }
        }

        @Override
        public boolean isEmpty() {
            synchronized (lock) {
                return delegate.isEmpty();
            }
        }

        @Override
        public Iterator<E> iterator() {
            synchronized (lock) {
                return delegate.iterator();
            }
        }

        @Override
        public boolean remove(final Object o) {
            synchronized (lock) {
                return delegate.remove(o);
            }
        }

        @Override
        public boolean removeAll(final Collection<?> c) {
            synchronized (lock) {
                return delegate.removeAll(c);
            }
        }

        @Override
        public boolean retainAll(final Collection<?> c) {
            synchronized (lock) {
                return delegate.retainAll(c);
            }
        }

        @Override
        public int size() {
            synchronized (lock) {
                return delegate.size();
            }
        }

        @Override
        public Object[] toArray() {
            synchronized (lock) {
                return delegate.toArray();
            }
        }

        @Override
        public <T> T[] toArray(final T[] a) {
            synchronized (lock) {
                return delegate.toArray(a);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (delegate) {
                return delegate.hashCode();
            }
        }
        
        @Override
        public boolean equals(Object obj) {
            synchronized (delegate) {
                return delegate.equals(obj);
            }
        }
        
        @Override
        public String toString() {
            synchronized (lock) {
                return delegate.toString();
            }
        }
    }
    
    /**
     * A synchronized {@link Set}
     */
    private static class SynchronizedSet<E> extends SynchronizedCollection<E> 
            implements Set<E> {

        public SynchronizedSet(Object lock, Collection<E> deleate) {
            super(lock, deleate);
        }
    }
    
    /**
     * A synchronized {@link SortedMap}
     */
    private static class SynchronizedSortedMap<K, V> implements SortedMap<K, V> {
        
        private final Object lock;
        
        private final SortedMap<K, V> delegate;
        
        public SynchronizedSortedMap(Object lock, SortedMap<K, V> delegate) {
            if (lock == null) {
                throw new NullPointerException("lock");
            }
            
            if (delegate == null) {
                throw new NullPointerException("delegate");
            }
            
            this.lock = lock;
            this.delegate = delegate;
        }

        @Override
        public Comparator<? super K> comparator() {
            synchronized (lock) {
                return delegate.comparator();
            }
        }

        @Override
        public Set<Entry<K, V>> entrySet() {
            synchronized (lock) {
                return new SynchronizedSet<Entry<K,V>>(lock, delegate.entrySet());
            }
        }

        @Override
        public K firstKey() {
            synchronized (lock) {
                return delegate.firstKey();
            }
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            synchronized (lock) {
                return new SynchronizedSortedMap<K, V>(lock, delegate.headMap(toKey));
            }
        }

        @Override
        public Set<K> keySet() {
            synchronized (lock) {
                return new SynchronizedSet<K>(lock, delegate.keySet());
            }
        }

        @Override
        public K lastKey() {
            synchronized (lock) {
                return delegate.lastKey();
            }
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            synchronized (lock) {
                return new SynchronizedSortedMap<K, V>(lock, delegate.subMap(fromKey, toKey));
            }
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            synchronized (lock) {
                return new SynchronizedSortedMap<K, V>(lock, delegate.tailMap(fromKey));
            }
        }

        @Override
        public Collection<V> values() {
            synchronized (lock) {
                return new SynchronizedCollection<V>(lock, delegate.values());
            }
        }

        @Override
        public void clear() {
            synchronized (lock) {
                delegate.clear();
            }
        }

        @Override
        public boolean containsKey(Object key) {
            synchronized (lock) {
                return delegate.containsKey(key);
            }
        }

        @Override
        public boolean containsValue(Object value) {
            synchronized (lock) {
                return delegate.containsValue(value);
            }
        }

        @Override
        public V get(Object key) {
            synchronized (lock) {
                return delegate.get(key);
            }
        }

        @Override
        public boolean isEmpty() {
            synchronized (lock) {
                return delegate.isEmpty();
            }
        }

        @Override
        public V put(K key, V value) {
            synchronized (lock) {
                return delegate.put(key, value);
            }
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            synchronized (lock) {
                delegate.putAll(m);
            }
        }

        @Override
        public V remove(Object key) {
            synchronized (lock) {
                return delegate.remove(key);
            }
        }

        @Override
        public int size() {
            synchronized (lock) {
                return delegate.size();
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (delegate) {
                return delegate.hashCode();
            }
        }
        
        @Override
        public boolean equals(Object obj) {
            synchronized (delegate) {
                return delegate.equals(obj);
            }
        }
        
        @Override
        public String toString() {
            synchronized (lock) {
                return delegate.toString();
            }
        }
    }
}