package edu.udo.cs.swtsf.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An implementation of {@link Group} which buffers all write operations 
 * that happen concurrently during an iteration. Once all iterations have 
 * returned the buffered write operations will be performed in their 
 * original order.
 * 
 * @param <E> the element type
 */
public class BufferedGroup<E> implements Group<E> {
	
	/**
	 * Contains all elements of the group
	 */
	private final List<E> list = new ArrayList<>(2);
	/**
	 * Buffers all modifications (add / remove operations) as {@link Consumer} instances.
	 * @see #add(Object)
	 * @see #remove(Object)
	 */
	private final List<Consumer<List<E>>> buffer = new ArrayList<>(2);
	/**
	 * Counts ongoing iterations. This variable should never be negative.
	 * @see #forEach(Consumer)
	 * @see #forEachTuple(BiConsumer)
	 */
	private int iterationCount = 0;
	
	public boolean add(E element) {
		if (iterationCount > 0) {
			// Use a lambda for the write operation and add it to the buffer
			buffer.add(list -> list.add(element));
		} else {
			list.add(element);
		}
		return true;
	}
	
	public boolean remove(E element) {
		if (iterationCount > 0) {
			// Use a lambda for the write operation and add it to the buffer
			buffer.add(list -> list.remove(element));
		} else {
			list.remove(element);
		}
		return true;
	}
	
	/**
	 * Returns the current size without paying attention to the buffer.<br>
	 */
	public int getSize() {
		return list.size();
	}
	
	public void forEach(Consumer<? super E> consumer) {
		if (consumer == null) {
			throw new IllegalArgumentException("consumer == null");
		}
		
		iterationCount++;
		for (E element : list) {
			consumer.accept(element);
		}
		iterationCount--;
		
		// Perform buffered write operations iff this was the last concurrent iteration
		performBufferedWritesIfPossible();
	}
	
	public void forEachTuple(BiConsumer<? super E, ? super E> consumer) {
		if (consumer == null) {
			throw new IllegalArgumentException("consumer == null");
		}
		
		iterationCount++;
		for (int i = 0; i < list.size() - 1; i++) {
			E a = list.get(i);
			
			for (int j = i + 1; j < list.size(); j++) {
				E b = list.get(j);
				
				consumer.accept(a, b);
			}
		}
		iterationCount--;
		
		// Perform buffered write operations iff this was the last concurrent iteration
		performBufferedWritesIfPossible();
	}
	
	/**
	 * Checks whether there are no more iterations going on. If so, 
	 * performs all buffered write operations and clears the buffer.<br>
	 */
	private void performBufferedWritesIfPossible() {
		if (iterationCount < 0) {
			throw new IllegalStateException("iterationCount is negative. iterationCount="+iterationCount);
		}
		if (iterationCount == 0) {
			buffer.forEach(runnable -> runnable.accept(list));
			buffer.clear();
		}
	}
	
	public E getFirstMatch(Predicate<? super E> condition) {
		if (condition == null) {
			throw new IllegalArgumentException("condition == null");
		}
		
		iterationCount++;
		E result = null;
		for (E element : list) {
			if (condition.test(element)) {
				result = element;
				break;
			}
		}
		iterationCount--;
		
		// Perform buffered write operations iff this was the last concurrent iteration
		performBufferedWritesIfPossible();
		return result;
	}
	
	public Collection<E> getAllMatches(Predicate<? super E> condition) {
		if (condition == null) {
			throw new IllegalArgumentException("condition == null");
		}
		
		iterationCount++;
		Collection<E> result = null;
		E firstMatch = null;
		for (E element : list) {
			if (condition.test(element)) {
				if (firstMatch == null) {
					firstMatch = element;
				} else if (result == null) {
					result = new ArrayList<>();
					result.add(firstMatch);
				} else {
					result.add(element);
				}
			}
		}
		iterationCount--;
		
		// Perform buffered write operations iff this was the last concurrent iteration
		performBufferedWritesIfPossible();
		if (result == null) {
			if (firstMatch == null) {
				return Collections.emptyList();
			} else {
				return Collections.singleton(firstMatch);
			}
		}
		return result;
	}
	
	public String toString() {
		return asString();
	}
	
}