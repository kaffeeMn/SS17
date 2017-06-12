package edu.udo.cs.swtsf.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>A simple data structure which allows adding and removing an arbitrary 
 * number of objects. A Group differs from {@link Set Set's} and {@link List 
 * List's} in the way, that the elements within the Group can neither be 
 * iterated over with an {@link Iterator} nor with a {@link List#get(int) get} 
 * method of any sort. Instead the only way to iterate over elements within a 
 * Group is to use the {@link #forEach(Consumer)} or {@link #forEachTuple(BiConsumer)} 
 * methods provided by the Group.<br>
 * A benefit of this restriction is the ability to allow adding and removing 
 * of elements to and from the Group during an iteration without throwing any 
 * {@link ConcurrentModificationException ConcurrentModificationException's} 
 * unlike a Set or List would do. This behavior is the most important aspect 
 * of a Group and the only reason to use a Group over a Set or List.</p>
 * 
 * <p>In addition to the {@link #add(Object)}, {@link #remove(Object)} and 
 * {@link #forEach(Consumer)} methods, a Group does also give access to a 
 * {@link #contains(Object)}, {@link #isEmpty()}, {@link #getSize()} and 
 * {@link #count(Object)} method.</p>
 * 
 * @param <E>	the type of elements
 */
public interface Group<E> {
	
	/**
	 * <p>Makes sure that {@code element} is added to this group after all 
	 * iterations on it have returned. If there are any iterations currently 
	 * in progress the Group can decide how to resolve the write conflict. 
	 * The {@code element} may be added immediately or be buffered for later 
	 * insertion. The only guarantee is, that the element will be added to 
	 * the group immediately after all current iterations have returned.<br>
	 * Elements can be added more then once to the group.</p>
	 * 
	 * @param element						the non-null element that is added
	 * @throws IllegalArgumentException		if element is null
	 * @see #remove(Object)
	 * @see #count(Object)
	 * @see #contains(Object)
	 * @see #forEach(Consumer)
	 * @see #forEachTuple(BiConsumer)
	 */
	public boolean add(E element);
	
	/**
	 * <p>Makes sure that {@code element} is removed from this group after all 
	 * iterations on it have returned. If there are any iterations currently 
	 * in progress the Group can decide how to resolve the write conflict. 
	 * The {@code element} may be removed immediately or be buffered for later 
	 * removal. The only guarantee is, that the element will be removed from 
	 * the group immediately after all current iterations have returned.</p>
	 * 
	 * <p>Removing an element will only remove it once from the group. If the 
	 * element was previously added more then once it may still be contained 
	 * unless it has been removed the same number of times as it was added.</p>
	 * 
	 * @param element						the non-null element that is removed
	 * @throws IllegalArgumentException		if element is null
	 * @see #add(Object)
	 * @see #count(Object)
	 * @see #contains(Object)
	 * @see #forEach(Consumer)
	 * @see #forEachTuple(BiConsumer)
	 */
	public boolean remove(E element);
	
	/**
	 * <p>Returns the number of times that {@code element} is contained within 
	 * this group. If {@code element} is not contained within this group, 
	 * zero will be returned.</p>
	 * @param element						the non-null element to count
	 * @throws IllegalArgumentException		if element is null
	 * @return								the number of times element is contained within this group
	 * @see #contains(Object)
	 */
	public default int count(E element) {
		if (isEmpty()) {
			return 0;
		}
		int[] result = new int[1];
		forEach(e -> {
			if (e.equals(element)) {
				result[0]++;
			}
		});
		return result[0];
	}
	
	/**
	 * <p>Returns true if {@code element} is contained at least once within 
	 * this group. Otherwise false is returned.</p>
	 * 
	 * @param element						the non-null element to check
	 * @throws IllegalArgumentException		if element is null
	 * @return								true if element is contained at least once
	 * @see #count(Object)
	 */
	public default boolean contains(E element) {
		return count(element) > 0;
	}
	
	/**
	 * <p>Returns the total number of elements contained within this group.<br>
	 * If an element is contained more then once it will be counted for 
	 * each occurrence within the group. If there are no elements within 
	 * the group zero is returned.</p>
	 * @return the total number of elements within this group
	 */
	public int getSize();
	
	/**
	 * <p>Returns true if there are no elements within this group. 
	 * Otherwise returns false.</p>
	 * @return true if this group is empty, else false
	 */
	public default boolean isEmpty() {
		return getSize() == 0;
	}
	
	/**
	 * <p>Calls the {@link Consumer#accept(Object)} method for each element 
	 * within this group exactly as many times as the element is contained. 
	 * The total number of calls to {@link Consumer#accept(Object)} is equal 
	 * to the size of this group as returned by {@link #getSize()}.</p>
	 * 
	 * <p>Until this method has returned it will be counted as an active 
	 * iteration on this group. Any modifications made to this group 
	 * during an active iteration, either by adding or removing elements, 
	 * will not result in an exception being thrown. Instead, the group 
	 * will guarantee the modification will take place immediately after all 
	 * active iterations have returned. How this is done is an internal 
	 * implementation detail of the group.</p>
	 * 
	 * @param action						the non-null consumer that will be given 
	 * 										all elements
	 * @throws IllegalArgumentException		if consumer is null
	 * @see #getSize()
	 * @see #add(Object)
	 * @see #remove(Object)
	 * @see #forEachTuple(BiConsumer)
	 */
	public void forEach(Consumer<? super E> action);
	
	/**
	 * <p>Pairs each element within this Group with each other element and calls the 
	 * {@link BiConsumer#accept(Object, Object)} of the given {@link BiConsumer} 
	 * exactly once for each such pair. The {@link BiConsumer#accept(Object, Object)} 
	 * is never called with the first and second argument being equal unless an 
	 * element is contained more then once within this Group.<br>
	 * The exact number of times the {@link BiConsumer#accept(Object, Object)} will 
	 * be called can be calculated with the equation: 
	 * <pre>{@code (N*N-N)/2}</pre> 
	 * where N is the number of elements within this Group.</p>
	 * 
	 * <p>If we assume our Group contains the elements A, B, C and D, the exact calls 
	 * to {@link BiConsumer#accept(Object, Object)} will be made with the following 
	 * pairs of elements: <pre>	(A, B)
	 * 	(A, C)
	 * 	(A, D)
	 * 	(B, C)
	 * 	(B, D)
	 * 	(C, D)</pre></p>
	 * 
	 * <p>Until this method has returned it will be counted as an active 
	 * iteration on this group. Any modifications made to this group 
	 * during an active iteration, either by adding or removing elements, 
	 * will not result in an exception being thrown. Instead, the group 
	 * will guarantee the modification will take place immediately after all 
	 * active iterations have returned. How this is done is an internal 
	 * implementation detail of the group.<br>
	 * 
	 * @param action						the non-null consumer that will be given all pairs of elements
	 * @throws IllegalArgumentException		if consumer is null
	 * @see #getSize()
	 * @see #add(Object)
	 * @see #remove(Object)
	 * @see #forEach(Consumer)
	 */
	public void forEachTuple(BiConsumer<? super E, ? super E> action);
	
	/**
	 * <p>Each element within this group will be checked against the given 
	 * condition. The first element to satisfy the condition will be returned.</p>
	 * <p>No assumptions should be made about the order in which the elements 
	 * will be tested. If more then one element from this group satisfies the 
	 * condition this method may return a different element with each call.</p>
	 *  
	 * @param condition						the condition that needs to be satisfied by the 
	 * 										returned value
	 * @throws IllegalArgumentException		if condition is null
	 * @return								an element from this group which satisfies the given 
	 * 										condition or null if no such element exists
	 * @see Predicate#test(Object)
	 * @see #getAllMatches(Predicate)
	 */
	@SuppressWarnings("unchecked")
	public default E getFirstMatch(Predicate<? super E> condition) {
		Object[] result = new Object[1];
		forEach(e -> {
			if (result[0] == null && condition.test(e)) {
				result[0] = e;
			}
		});
		return (E) result[0];
	}
	
	/**
	 * <p>Each element within this group will be checked against the given 
	 * condition. All elements which satisfy the condition will be returned 
	 * within a {@link Collection}.</p>
	 * <p>No assumptions should be made about the order in which the elements 
	 * will be tested and returned.</p>
	 *  
	 * @param condition						the condition that needs to be satisfied by all 
	 * 										returned values
	 * @throws IllegalArgumentException		if condition is null
	 * @return								a Collection containing zero or more elements 
	 * 										from this group
	 * @see Predicate#test(Object)
	 * @see #getFirstMatch(Predicate)
	 */
	public default Collection<E> getAllMatches(Predicate<? super E> condition) {
		class CollectorConsumer implements Consumer<E> {
			Collection<E> collectionResult = null;
			E singleResult = null;
			public void accept(E element) {
				if (condition.test(element)) {
					if (singleResult == null) {
						singleResult = element;
					} else if (collectionResult == null) {
						collectionResult = new ArrayList<>();
						collectionResult.add(singleResult);
						collectionResult.add(element);
					} else {
						collectionResult.add(element);
					}
				}
			}
		};
		CollectorConsumer cc = new CollectorConsumer();
		forEach(cc);
		
		if (cc.collectionResult != null) {
			return cc.collectionResult;
		}
		if (cc.singleResult != null) {
			return Collections.singleton(cc.singleResult);
		}
		return Collections.emptyList();
	}
	
	/**
	 * <p>This is a default {@link #toString()} implementation, which returns a String 
	 * containing all contents of this Group separated by commas.</p>
	 * <p>Please note, that this method is called <b><u>as</u>String</b> and not 
	 * <b><u>to</u>String</b> since to the toString method can not be overwritten by 
	 * an interface. To have a proper toString implementation, subclasses need to 
	 * overwrite toString and call asString as part of the toString implementation.</p>
	 * @return	a String starting with '[' and ending with ']' containing all contents of this group separated by commas.
	 */
	public default String asString() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		forEach(e -> {
			sb.append(e);
			sb.append(", ");
		});
		if (sb.length() > 2) {
			sb.delete(sb.length()-2, sb.length());
		}
		sb.append(']');
		return sb.toString();
	}
	
}