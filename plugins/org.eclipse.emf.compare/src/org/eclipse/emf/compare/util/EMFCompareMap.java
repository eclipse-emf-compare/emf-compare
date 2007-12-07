/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.compare.EMFCompareMessages;

/**
 * Implementation of a map that stores its content using an hash table.
 * <p>
 * <it>Initial capacity</it> and <it>load factor</it> greatly affect the map's performances. <it>Capacity</it>
 * is the number of elements the map can contain and the <it>load factor</it> represents how much the hash
 * table can be filled before it is automatically rehashed.
 * </p>
 * <p>
 * This implementation ensures the capacity of this Map is a prime number at all time.
 * </p>
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 * @param <K>
 *            Specifies the keys' class for this Map.
 * @param <V>
 *            Specifies the values' class for this Map.
 */
@SuppressWarnings("unchecked")
public class EMFCompareMap<K, V> implements Map<K, V>, Serializable, Cloneable {
	/** Default value used as the initial capacity for a Map. */
	protected static final int DEFAULT_INITIAL_CAPACITY = 31;

	/** The load factor used when none specified in constructor. * */
	protected static final float DEFAULT_LOAD_FACTOR = 0.6f;

	/**
	 * Primes that will be used one after the other when using an initial capacity of 31. This lists extends
	 * up to {@link Integer#MAX_VALUE} as it is a prime.
	 */
	protected static final int[] DEFAULT_PRIME_LIST = new int[] {31, 67, 137, 277, 557, 1117, 2237, 4481,
			8963, 17929, 35863, 71741, 143483, 286973, 573953, 1147921, 2295859, 4591721, 9183457, 18366923,
			36733847, 73467739, 146935499, 293871013, 587742049, 1175484103, Integer.MAX_VALUE, };

	/** Minimal allowed load factor for the map. */
	protected static final float MINIMUM_LOAD_FACTOR = 0.05f;

	/** Object used as key for the <code>null</code> key. */
	protected static final Object NULL_KEY = new Object();

	/** Object used as key for the "removed" entries of this Map. */
	protected static final Object REMOVED_ENTRY = new Object();

	/** First hundred primes. Used to compute the next prime. */
	private static final int[] FIRST_PRIMES = new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
			47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, };

	/** Serial number of this map. Used for checks upon deserialization. */
	private static final long serialVersionUID = -890704446117047482L;

	/** Current number of free buckets this Map holds. */
	protected int freeSlots;

	/** Keys of this Map's entries. */
	protected transient K[] keys;

	/** Load factor of this Map. */
	protected float loadFactor = DEFAULT_LOAD_FACTOR;

	/** Index of the next prime in the primes list. */
	protected int nextPrimeIndex;

	/** Threshold for resizing. */
	protected int threshold;

	/** Current number of non-empty slots. */
	protected transient int usedSlots;

	/** Values contained within this Map. */
	protected transient V[] values;

	/**
	 * Constructs an empty <code>EMFCompareMap</code> with the default initial capacity (31) and the default
	 * load factor (0.75).
	 */
	public EMFCompareMap() {
		// We know the initial capacity will be a prime equal to DEFAULT_INITIAL_CAPACITY
		nextPrimeIndex++;
		threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR - 1);
		freeSlots = threshold;
		keys = (K[])new Object[DEFAULT_INITIAL_CAPACITY];
		values = (V[])new Object[DEFAULT_INITIAL_CAPACITY];
	}

	/**
	 * Constructs an empty <code>EMFCompareMap</code> with the specified initial capacity and the default
	 * load factor (0.75). This class will automatically use <code>{@value #MINIMAL_INITIAL_CAPACITY}</code>
	 * as the Map's initial capacity if <code>initialCapacity</code> is lower.
	 * 
	 * @param initialCapacity
	 *            Initial capacity of the Map.
	 */
	public EMFCompareMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Constructs an empty <code>EMFCompareMap</code> with the specified initial capacity and load
	 * factor.This class will automatically use <code>{@value #MINIMAL_INITIAL_CAPACITY}</code> as the Map's
	 * initial capacity if <code>initialCapacity</code> is lower.
	 * 
	 * @param initialCapacity
	 *            Initial capacity of the Map.
	 * @param theLoadFactor
	 *            Load factor to apply to this Map.
	 */
	public EMFCompareMap(int initialCapacity, float theLoadFactor) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException(EMFCompareMessages.getString("EMFCompareMap.NegativeCapacity")); //$NON-NLS-1$
		if (theLoadFactor <= 0 || Float.isNaN(theLoadFactor))
			throw new IllegalArgumentException(EMFCompareMessages.getString(
					"EMFCompareMap.IllegalLoadFactor", theLoadFactor)); //$NON-NLS-1$

		loadFactor = theLoadFactor;
		final int newCapacity = getNearestPrime((int)(initialCapacity / Math.max(MINIMUM_LOAD_FACTOR,
				loadFactor)));
		changeCapacity(newCapacity);
		keys = (K[])new Object[newCapacity];
		values = (V[])new Object[newCapacity];
	}

	/**
	 * Construcs a <code>EMFCompareMap</code> populated by the entries of <code>map</code>.
	 * 
	 * @param map
	 *            The map whose mappings are to be placed in this map.
	 */
	public EMFCompareMap(Map<K, V> map) {
		this(map.size());
		putAll(map);
	}

	/**
	 * Removes all mappings from this Map.
	 */
	public void clear() {
		if (size() == 0)
			return;

		usedSlots = 0;
		freeSlots = capacity();
		for (int i = 0; i < keys.length; i++) {
			keys[i] = null;
			values[i] = null;
		}
	}

	/**
	 * Returns a copy of this Map containing all its elements.
	 * 
	 * @return A clone of this instance.
	 */
	@Override
	public Object clone() {
		try {
			final EMFCompareMap<K, V> map = (EMFCompareMap<K, V>)super.clone();
			map.putAll(this);
			return map;
		} catch (CloneNotSupportedException e) {
			// should be supported
			return null;
		}
	}

	/**
	 * Returns <code>True</code> if the Map contains a mapping for the specified key.
	 * 
	 * @param key
	 *            The key whose presence in this Map is to be tested.
	 * @return <code>True</code> if the Map contains a mapping for <code>key</code>, <code>False</code>
	 *         otherwise.
	 */
	public boolean containsKey(Object key) {
		return indexOf(key) >= 0;
	}

	/**
	 * Checks for a mapping to the specified value in the Map.
	 * 
	 * @param value
	 *            Value whose presence is to be tested.
	 * @return <code>True</code> if the Map contains a mapping to <code>value</code>, <code>False</code>
	 *         otherwise.
	 */
	public boolean containsValue(Object value) {
		boolean result = false;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null && keys[i] != REMOVED_ENTRY) {
				if (value == null && values[i] == null) {
					result = true;
					break;
				} else if (value != null
						&& (values[i] == value || (values[i] != null && values[i].equals(value)))) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#entrySet()
	 */
	public Set<Map.Entry<K, V>> entrySet() {
		return new EntrySet();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object another) {
		boolean result = false;
		if (another == this) {
			result = true;
		} else if (another instanceof Map && ((Map)another).size() == size()) {
			// Two empty maps are considered equal regardless of the implementation
			if (size() == 0)
				result = true;
			for (Map.Entry<K, V> entry : entrySet()) {
				if (((Map)another).containsKey(entry.getKey())) {
					final Object otherValue = ((Map)another).get(entry.getKey());
					result = equal(otherValue, entry.getValue());
				}
			}
		}
		return result;
	}

	/**
	 * Retrieves the value to which the specified key is mapped.
	 * 
	 * @param key
	 *            The key whose associated value is to be returned.
	 * @return Value mapped to <code>key</code>, <code>null</code> if none.
	 */
	public V get(Object key) {
		final int index = indexOf(key);
		if (index < 0)
			return null;
		return values[index];
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		for (Map.Entry<K, V> entry : entrySet())
			hash += entry.hashCode();
		return hash;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set<K> keySet() {
		return new KeySet();
	}

	/**
	 * Associates the specified key with the specified value in the Map. If a value was already associated to
	 * this key, it is replaced and returned.
	 * 
	 * @param key
	 *            Key with which the value will be associated.
	 * @param value
	 *            Value to associate with the key.
	 * @return The previous value associated with <code>key</code>. <code>Null</code> if there were none
	 *         or <code>null</code> was associated.
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public V put(K key, V value) {
		K effectiveKey = key;
		if (key == null)
			effectiveKey = (K)NULL_KEY;

		V oldValue = null;
		boolean usedFreeSlot;

		int index = insertionIndexFor(effectiveKey);
		boolean isNewMapping = true;
		if (index < 0) {
			index = -index - 1;
			oldValue = values[index];
			isNewMapping = false;
		}
		usedFreeSlot = keys[index] == null;
		keys[index] = effectiveKey;
		values[index] = value;
		if (isNewMapping)
			postInsert(usedFreeSlot);

		return oldValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends K, ? extends V> map) {
		checkCapacity(map.size());
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Removes the mapping for this key from this Map if present.
	 * 
	 * @param key
	 *            Key whose mapping is to be removed.
	 * @return The old value mapped to <code>key</code>.
	 */
	public V remove(Object key) {
		Object effectiveKey = key;
		if (key == null)
			effectiveKey = NULL_KEY;

		V oldValue = null;
		final int index = indexOf(effectiveKey);
		if (index >= 0) {
			oldValue = values[index];
			removeEntryForIndex(index);
		}
		return oldValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#size()
	 */
	public int size() {
		return usedSlots;
	}

	/**
	 * Returns a String representation of this Map.
	 * 
	 * @return A String representation of this Map.
	 */
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append("{"); //$NON-NLS-1$

		final Iterator<Map.Entry<K, V>> i = entrySet().iterator();
		boolean hasNext = i.hasNext();
		while (hasNext) {
			final Map.Entry<K, V> e = i.next();
			buf.append(e.toString());
			hasNext = i.hasNext();
			if (hasNext)
				buf.append(", "); //$NON-NLS-1$
		}

		buf.append("}"); //$NON-NLS-1$
		return buf.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection<V> values() {
		return new ValueSet();
	}

	/**
	 * Returns the current capacity of the Map.
	 * 
	 * @return The current capacity of the Map.
	 */
	protected int capacity() {
		return keys.length;
	}

	/**
	 * Modifies the capacity of this Map. This computes the new maximum size and number of free slots.
	 * 
	 * @param newCapacity
	 *            Capacity from which to compute the maximum capacity and the new number of free slots.
	 */
	protected void changeCapacity(int newCapacity) {
		threshold = newCapacity - 1;
		final int candidate = (int)Math.floor(newCapacity * loadFactor);
		if (candidate < threshold)
			threshold = candidate;
		freeSlots = newCapacity - usedSlots;
	}

	/**
	 * Ensures the map has sufficient empty slots to hold <code>desiredSlots</code> additional elements and
	 * resizes it if needed.
	 * 
	 * @param desiredSlots
	 *            Desired number of additional elements.
	 */
	protected void checkCapacity(int desiredSlots) {
		if (desiredSlots > (threshold - size())) {
			resize(getNearestPrime((int)Math.ceil(desiredSlots + size() / loadFactor) + 1));
		}
	}

	/**
	 * Returns <code>True</code> if the two object are equal.
	 * 
	 * @param obj1
	 *            First of the objects to compare.
	 * @param obj2
	 *            Second of the objects to compare.
	 * @return <code>True</code> if the two object are equal, <code>False</code> otherwise.
	 */
	protected boolean equal(Object obj1, Object obj2) {
		if (obj1 == null)
			return obj2 == null;
		return obj1.equals(obj2);
	}

	/**
	 * Finds the nearest pseudo-prime greater than <code>number</code>. Based on Rabin-Miller primality
	 * test.
	 * 
	 * @param number
	 *            Number we will use as a starting point to find a greater prime.
	 * @return First pseudo-prime greater than <code>number</code>
	 */
	protected int getNearestPrime(int number) {
		if (nextPrimeIndex > 0)
			return DEFAULT_PRIME_LIST[nextPrimeIndex++];

		// Any number below 2 will have its nearest prime equal to 2.
		final int isPrime;
		if (number <= 2)
			isPrime = 2;
		else
			isPrime = Math.abs(number);
		int nearestPrime = -1;

		if (isPrime < 100 && Arrays.binarySearch(FIRST_PRIMES, isPrime) >= 0) {
			nearestPrime = isPrime;
		} else if ((isPrime & 1) == 0) {
			nearestPrime = getNearestPrime(isPrime + 1);
		} else if (isPrime % 6 != 1 && isPrime % 6 != 5) {
			nearestPrime = getNearestPrime(isPrime + 5 - (isPrime % 6));
		} else {
			for (int prime : FIRST_PRIMES) {
				if (isPrime % prime == 0) {
					nearestPrime = getNearestPrime(isPrime + 2);
				}
			}

			if (nearestPrime == -1) {
				int d = isPrime - 1;
				int r = 1;

				// Calculates d and r
				while ((d & 1) == 0) {
					d >>= 1;
					r += 1;
				}

				// Tests the number x times (increasing this loop's range increases probability of the number
				// being a prime)
				final int loops = 10;
				for (int i = 0; i < loops; i++) {
					// Calculates (a^d mod n) where a is a random number
					int y = (int)Math.pow(Math.random() * isPrime + 1, d) % isPrime;

					// if y equals 1, it passed the test
					if (y == 1)
						continue;

					boolean pass = false;
					for (int j = 1; j < r; j++) {
						if (y != (isPrime - 1)) {
							pass = true;
							break;
						}
						y = (int)Math.pow(y, 2) % isPrime;
					}
					// number didn't pass the test. We'll test again with the next possible
					if (!pass)
						nearestPrime = getNearestPrime(isPrime + 2);
				}
			}
			// If we are here, number passed the test 10 times. It is most likely a prime
			if (nearestPrime == -1)
				nearestPrime = isPrime;
		}
		return nearestPrime;
	}

	/**
	 * Determines the index of the specified key in the <code>keys</code> array.
	 * 
	 * @param key
	 *            Key whose index is to be fetched.
	 * @return The index of the specified key, <code>-1</code> if inexistant.
	 */
	protected int indexOf(Object key) {
		Object effectiveKey = key;
		if (key == null)
			effectiveKey = NULL_KEY;

		final int mask = 0x7fffffff;
		final int hash = effectiveKey.hashCode() & mask;
		final int length = keys.length;
		int index = hash % length;
		K current = keys[index];

		if (current != null && (current == REMOVED_ENTRY || !equal(current, effectiveKey))) {
			final int hashProbe = 1 + (hash % (length - 2));

			while (current != null && (current == REMOVED_ENTRY || !equal(current, effectiveKey))) {
				index -= hashProbe;
				if (index < 0)
					index += length;
				current = keys[index];
			}
		}

		if (current == null)
			return -1;
		return index;
	}

	/**
	 * Returns the index at which <code>key</code> can be inserted. If such a key already exists, returns
	 * its negative value minus 1.
	 * 
	 * @param key
	 *            Key to be inserted.
	 * @return The index at which <code>key</code> can be inserted. If such a key already exists, returns
	 *         its negative value minus 1.
	 */
	protected int insertionIndexFor(K key) {
		// Key cannot be null here, it is either an object or NULL_KEY

		final int mask = 0x7fffffff;
		final int hash = key.hashCode() & mask;
		final int length = keys.length;
		int index = hash % length;
		K current = keys[index];

		if (current == null) {
			return index;
		} else if (current != REMOVED_ENTRY && equal(current, key)) {
			// Key is already stored
			return -index - 1;
		} else {
			// This slot is already associated.
			final int hashProbe = 1 + (hash % (length - 2));

			if (current != REMOVED_ENTRY) {
				while (current != null && current != REMOVED_ENTRY && !equal(current, key)) {
					index -= hashProbe;
					if (index < 0)
						index += length;
					current = keys[index];
				}
			}

			if (current == REMOVED_ENTRY) {
				final int removedIndex = index;
				while (current != null && (current == REMOVED_ENTRY || !equal(current, key))) {
					index -= hashProbe;
					if (index < 0)
						index += length;
					current = keys[index];
				}
				// if we didn't find a free slot, we'll use a "removed" one
				if (current == null)
					index = removedIndex;
			}
		}
		if (current != null)
			return -index - 1;
		return index;
	}

	/**
	 * Called after an insert to update the used and free slots information and rehash the map if needed.
	 * 
	 * @param usedFreeSlot
	 *            Indicates whether the insert used up a free slot.
	 */
	protected void postInsert(boolean usedFreeSlot) {
		if (usedFreeSlot)
			freeSlots--;
		usedSlots++;

		// If there isn't any space left in the table, expand table.
		if (usedSlots > threshold || freeSlots == 0) {
			final int newCapacity = getNearestPrime(capacity() << 1);
			resize(newCapacity);
		}
	}

	/**
	 * Removes the specified entry from this Map. This is only called from EntrySet.
	 * 
	 * @param e
	 *            Entry to be removed from the Map.
	 * @return The old entry.
	 */
	protected Map.Entry<K, V> removeEntry(Object e) {
		Map.Entry<K, V> result;
		if (!(e instanceof Map.Entry)) {
			result = null;
		} else {
			final Map.Entry<K, V> entry = (Map.Entry<K, V>)e;
			final V oldValue = remove(entry.getKey());
			if (oldValue == null && entry.getValue() != null)
				result = null;
			else
				result = entry;
		}
		return result;
	}

	/**
	 * Removes the mapping at <code>index</code> from this Map.
	 * 
	 * @param index
	 *            Index of the mapping to be removed.
	 */
	protected void removeEntryForIndex(int index) {
		usedSlots--;
		keys[index] = (K)REMOVED_ENTRY;
		values[index] = null;
	}

	/**
	 * Rehashes the content of this Map into a new Array with a larger capacity. Also used without modifying
	 * capacity to delete removed entries in order to free space.
	 * 
	 * @param newCapacity
	 *            New capacity of the Map.
	 */
	protected void resize(int newCapacity) {
		final K[] oldKeys = keys;
		final V[] oldValues = values;

		keys = (K[])new Object[newCapacity];
		values = (V[])new Object[newCapacity];

		for (int i = 0; i < oldKeys.length; i++) {
			if (oldKeys[i] != null && oldKeys[i] != REMOVED_ENTRY) {
				final int index = insertionIndexFor(oldKeys[i]);
				keys[index] = oldKeys[i];
				values[index] = oldValues[i];
			}
		}
		changeCapacity(newCapacity);
	}

	/**
	 * Represents a mapping key => value for this Map.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 */
	/* package */class Entry implements Map.Entry<K, V> {
		/** Index of this entry. */
		private final int index;

		/** Key of this Map entry. */
		private final K key;

		/** Value of this mapping. */
		private V value;

		/**
		 * Creates a mapping.
		 * 
		 * @param entryKey
		 *            The key for this mapping.
		 * @param entryValue
		 *            The value for this Map entry.
		 * @param entryIndex
		 *            The index of this entry.
		 */
		public Entry(K entryKey, V entryValue, int entryIndex) {
			key = entryKey;
			value = entryValue;
			index = entryIndex;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public final boolean equals(Object another) {
			boolean result = false;
			if (another == this) {
				result = true;
			} else if (another instanceof Map.Entry) {
				final Map.Entry other = (Map.Entry)another;
				if (equal(key, other.getKey()) && equal(value, other.getValue())) {
					result = true;
				}
			}
			return result;
		}

		/**
		 * Returns the key corresponding to this entry.
		 * 
		 * @return The key corresponding to this entry.
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Returns the value corresponding to this entry.
		 * 
		 * @return The value corresponding to this entry.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public final int hashCode() {
			int keyHash = 0;
			int valueHash = 0;
			if (key != null)
				keyHash = key.hashCode();
			if (value != null)
				valueHash = value.hashCode();
			return keyHash ^ valueHash;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Map$Entry#setValue(java.lang.Object)
		 */
		public V setValue(V newValue) {
			if (EMFCompareMap.this.values[index] != value)
				throw new ConcurrentModificationException();

			final V oldValue = value;
			EMFCompareMap.this.values[index] = newValue;
			value = newValue;

			return oldValue;
		}

		/**
		 * Returns a String representation of this entry.
		 * 
		 * @return A String representation of this entry.
		 */
		@Override
		public String toString() {
			// Note that we cannot have entries with key == FREE_ENTRY or key == REMOVED_ENTRY
			// (see AbstractHashIterator#nextEntry)
			final StringBuilder result = new StringBuilder();
			if (key == NULL_KEY)
				result.append("null"); //$NON-NLS-1$
			else
				result.append(key.toString());
			result.append('=');
			if (value == null)
				result.append("null"); //$NON-NLS-1$
			else
				result.append(value.toString());
			return result.toString();
		}
	}

	/**
	 * This iterator allows traversing keys and values of this Map.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 * @param <E>
	 *            Defines the class of this iterator's contents.
	 */
	private abstract class AbstractHashIterator<E> implements Iterator<E> {
		/** Current entry. */
		protected Entry currentEntry;

		/** Current key. */
		protected K currentKey;

		/** Current value. */
		protected V currentValue;

		/** Expected number of entries for this iterator to traverse. */
		private int expectedSize;

		/** Index of the next entry. */
		private int nextIndex;

		/**
		 * Constructs an HashIterator over the entries of this Map.
		 */
		public AbstractHashIterator() {
			expectedSize = usedSlots;
			while (nextIndex < keys.length && (keys[nextIndex] == null || keys[nextIndex] == REMOVED_ENTRY))
				nextIndex++;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return nextIndex < keys.length;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			if (currentKey == null)
				throw new IllegalStateException();
			if (expectedSize != usedSlots)
				throw new ConcurrentModificationException();

			EMFCompareMap.this.remove(currentKey);
			currentEntry = null;
			currentKey = null;
			currentValue = null;
			expectedSize = usedSlots;
		}

		/**
		 * Advances this iterator to the next non removed, non <code>null</code> entry of the Map.
		 */
		protected void nextEntry() {
			if (expectedSize != usedSlots)
				throw new ConcurrentModificationException();

			currentKey = keys[nextIndex];
			currentValue = values[nextIndex];
			currentEntry = new Entry(currentKey, currentValue, nextIndex);

			nextIndex++;
			while (nextIndex < keys.length && (keys[nextIndex] == null || keys[nextIndex] == REMOVED_ENTRY))
				nextIndex++;
		}
	}

	/**
	 * Allows iteration over the entries of this Map.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 */
	private class EntryIterator extends AbstractHashIterator<Map.Entry<K, V>> {
		/**
		 * Increases visibility of the default constructor.
		 */
		public EntryIterator() {
			super();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#next()
		 */
		public Map.Entry<K, V> next() {
			nextEntry();
			return currentEntry;
		}
	}

	/**
	 * Provides a view over the entries of the Map.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 */
	private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
		/**
		 * Increases visibility of the default constructor.
		 */
		public EntrySet() {
			super();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Set#clear()
		 */
		@Override
		public void clear() {
			EMFCompareMap.this.clear();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#contains(java.lang.Object)
		 */
		@Override
		public boolean contains(Object e) {
			boolean result = false;
			if (!(e instanceof Map.Entry))
				return false;

			final Map.Entry<K, V> entry = (Map.Entry<K, V>)e;
			if (EMFCompareMap.this.containsKey(entry.getKey())) {
				final V candidate = EMFCompareMap.this.get(entry.getKey());
				if (candidate == null)
					result = entry.getValue() == null;
				else
					result = entry.getValue().equals(candidate);
			}

			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#remove(java.lang.Object)
		 */
		@Override
		public boolean remove(Object e) {
			return EMFCompareMap.this.removeEntry(e) != null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractSet#removeAll(java.util.Collection)
		 */
		@Override
		public boolean removeAll(Collection<?> c) {
			boolean modified = false;

			if (size() > c.size()) {
				final Iterator<?> i = c.iterator();
				while (i.hasNext()) {
					modified |= remove(i.next());
				}
			} else {
				final Iterator<?> i = c.iterator();
				while (i.hasNext()) {
					final Object entry = i.next();
					if (contains(entry)) {
						remove(entry);
						modified = true;
					}
				}
			}
			return modified;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#retainAll(java.util.Collection)
		 */
		@Override
		public boolean retainAll(Collection<?> c) {
			boolean modified = false;
			final Object[] retainedKeys = new Object[c.size()];
			int indx = 0;
			final Iterator<?> it = c.iterator();
			while (it.hasNext()) {
				final Object entry = it.next();
				if (entry instanceof Map.Entry)
					retainedKeys[indx++] = ((Map.Entry)entry).getKey();
			}

			final Iterator<?> e = iterator();
			while (e.hasNext()) {
				final Object entry = e.next();
				boolean removeIt = true;
				for (Object o : retainedKeys) {
					if (((Map.Entry)entry).getKey() == o) {
						removeIt = false;
						break;
					}
				}
				if (removeIt) {
					e.remove();
					modified = true;
				}
			}
			return modified;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return EMFCompareMap.this.size();
		}
	}

	/**
	 * Allows iteration over the keys of this Map.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 */
	private class KeyIterator extends AbstractHashIterator<K> {
		/**
		 * Increases visibility of the default constructor.
		 */
		public KeyIterator() {
			super();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#next()
		 */
		public K next() {
			nextEntry();
			return currentKey;
		}
	}

	/**
	 * Provides a view over the keys of this Map.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 */
	private class KeySet extends AbstractSet<K> {
		/**
		 * Increases visibility of the default constructor.
		 */
		public KeySet() {
			super();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Set#clear()
		 */
		@Override
		public void clear() {
			EMFCompareMap.this.clear();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#contains(java.lang.Object)
		 */
		@Override
		public boolean contains(Object o) {
			return EMFCompareMap.this.containsKey(o);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Set#iterator()
		 */
		@Override
		public Iterator<K> iterator() {
			return new KeyIterator();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Set#remove(java.lang.Object)
		 */
		@Override
		public boolean remove(Object o) {
			return EMFCompareMap.this.remove(o) != null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Set#size()
		 */
		@Override
		public int size() {
			return EMFCompareMap.this.size();
		}
	}

	/**
	 * Allows iteration over the values of this Map.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 */
	private class ValueIterator extends AbstractHashIterator<V> {
		/**
		 * Increases visibility of the default constructor.
		 */
		public ValueIterator() {
			super();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Iterator#next()
		 */
		public V next() {
			nextEntry();
			return currentValue;
		}
	}

	/**
	 * Provides a view over the values of this Map.
	 * 
	 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
	 */
	private class ValueSet extends AbstractSet<V> {
		/**
		 * Increases visibility of the default constructor.
		 */
		public ValueSet() {
			super();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#clear()
		 */
		@Override
		public void clear() {
			EMFCompareMap.this.clear();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#contains(java.lang.Object)
		 */
		@Override
		public boolean contains(Object o) {
			return EMFCompareMap.this.containsValue(o);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override
		public Iterator<V> iterator() {
			return new ValueIterator();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return EMFCompareMap.this.size();
		}
	}
}
