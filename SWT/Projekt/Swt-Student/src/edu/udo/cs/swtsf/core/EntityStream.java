package edu.udo.cs.swtsf.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import edu.udo.cs.swtsf.core.player.Player;

/**
 * <p>An EntityStream offers access to a set of {@link Entity Entities} in a {@link Game}. 
 * An EntityStream can be used to perform certain {@link Consumer actions} for all Entities 
 * which fulfill a set of {@link Predicate conditions}, the EntityStream can count Entities 
 * or copy them into other data structures as required.</p>
 * 
 * <p>EntityStream offers two kinds of operations: decorating operations which wrap the 
 * EntityStream in another EntityStream to modify its functionality or filter certain 
 * Entities, and terminal operations which consume the EntityStream and perform an action 
 * on all Entities within the stream.</p>
 * 
 * <p>The EntityStream API is similar to the {@link Stream} API in the JDK but far less 
 * powerful. EntityStream is a simplification and offers operations specifically designed 
 * for game development.</p>
 * 
 * <p>Example of use:
 * <pre>{@code 	game.getAllEntities()
	.without(player)
	.ofType(Target.class)
	.filter(e -> e.isAlive())
	.withinRadiusOfEntity(player, 50.0)
	.forEach(e -> e.addHitpoints(-1));}</pre>
	
 * This will decrease the {@link Target#getHitpoints() hitpoints} of all 
 * {@link Target#isAlive living} {@link Target Targets} within a radius of 50.0 around the 
 * {@link Player} by 1 point. The players own hitpoints will not be changed.</p>
 * 
 * @param <T>	a sub-type of {@link Entity}; the type of Entities returned by this stream
 * @see FilteredEntityStream
 * @see #filter(Predicate)
 * @see #forEach(Consumer)
 * @see #count()
 */
public interface EntityStream<T extends Entity> {
	
	/**
	 * <p>Iterates over all {@link Entity Entities} within this stream and performs the 
	 * given {@link Consumer#accept(Object) action} on each entity in arbitrary sequence.</p>
	 * 
	 * <p>No assumptions should be made about the order of iteration.</p>
	 * 
	 * <p>This is a terminal operation on the EntityStream.</p>
	 * 
	 * @param action		a non-null Consumer. The {@link Consumer#accept(Object)} method 
	 * 						is called exactly once for each {@link Entity} in this stream.
	 */
	public void forEach(Consumer<T> action);
	
	/**
	 * <p>Calls the {@link Consumer#accept(Object)} method of the given action only for the 
	 * first {@link Entity} within this stream. If this stream is empty, the accept method is 
	 * not called at all.</p>
	 * 
	 * <p>If this stream contains more than one Entity, no assumption should be made about 
	 * which Entity will be chosen.</p>
	 * 
	 * <p>This is a terminal operation on the EntityStream.</p>
	 * 
	 * @param action		a non-null Consumer. The {@link Consumer#accept(Object)} method 
	 * 						is called either once or never with an {@link Entity} from this stream.
	 */
	public default void forFirst(Consumer<T> action) {
		boolean[] init = {true};
		forEach(e -> {
			if (init[0]) {
				action.accept(e);
				init[0] = false;
			}
		});
	}
	
	/**
	 * <p>Returns an EntityStream which contains all {@link Entity Entities} from this stream 
	 * except for those which do not pass the {@link Predicate#test(Object) filter condition}.</p>
	 * 
	 * <p>This is a decorating operation.</p>
	 * 
	 * @param filter		a non-null {@link Predicate filter} for {@link Entity Entities}.
	 * @return				a non-null stream containing all Entities from this stream 
	 * 						which pass the {@link Predicate#test(Object) filter test}.
	 */
	public default EntityStream<T> filter(Predicate<T> filter) {
		return new FilteredEntityStream<>(this, filter);
	}
	
	/**
	 * <p>Counts all {@link Entity Entities} within this stream and returns their number.</p>
	 * 
	 * <p>This is a terminal operation on the EntityStream.</p>
	 * 
	 * @return				the number of Entities within this stream
	 * @see #accumulate(Function, BiFunction, Object)
	 */
	public default int count() {
		return accumulate(e -> 1, (i, j) -> i + j, 0);
	}
	
	/**
	 * <p>Returns true if the given {@link Predicate#test(Object) condition} is true for all 
	 * {@link Entity Entities} within this stream. Otherwise returns false. If this stream 
	 * contains no Entities, true is returned.</p>
	 * 
	 * <p>This is a terminal operation on the EntityStream.</p>
	 * 
	 * @return				true if condition is fulfilled by all Entities within this stream; otherwise false.
	 * @see #isTrueForAny(Predicate)
	 * @see #accumulate(Function, BiFunction, Object)
	 */
	public default boolean isTrueForAll(Predicate<T> condition) {
		return accumulate(e -> condition.test(e), (a, b) -> a && b, true);
	}
	
	/**
	 * <p>Returns true if the given {@link Predicate#test(Object) condition} is true for at 
	 * least one {@link Entity} within this stream. Otherwise returns false. If this stream 
	 * contains no Entities, false is returned.</p>
	 * 
	 * <p>This is a terminal operation on the EntityStream.</p>
	 * 
	 * @return				true if condition is fulfilled by at least one Entity within this stream; otherwise false.
	 * @see #isTrueForAll(Predicate)
	 * @see #accumulate(Function, BiFunction, Object)
	 */
	public default boolean isTrueForAny(Predicate<T> condition) {
		return accumulate(e -> condition.test(e), (a, b) -> a || b, false);
	}
	
	/**
	 * <p>Iterates over all {@link Entity Entities} within this stream feeds them into the 
	 * transformer. The value returned by the transformer is then fed into the accumulator, 
	 * together with the previous accumulation result. For the very first step of the 
	 * iteration the initialValue is used as accumulation result. If this stream contains 
	 * no Entities the initialValue is returned.</p>
	 * 
	 * <p>This is a terminal operation on the EntityStream.</p>
	 * 
	 * <p>The operations {@link #count()}, {@link #isTrueForAll(Predicate)} and 
	 * {@link #isTrueForAny(Predicate)} (or similar) can be implemented using this method.</p>
	 * 
	 * @param transformer			A {@link Function} which transforms each {@link Entity} into a value
	 * @param accumulator			A {@link BiFunction} which takes the value of the current iteration step 
	 * 								and the accumulated value of all previous iteration steps, and sums them up
	 * @param initialValue			The initial value of the accumulation before the iteration has started. 
	 * 								Fed to the accumulator in the first iteration step or returned if the stream 
	 * 								is empty.
	 * @return						An value accumulated over all Entities from this stream
	 */
	@SuppressWarnings("unchecked")
	public default <A> A accumulate(Function<T, A> transformer, 
			BiFunction<A, A, A> accumulator, A initialValue) 
	{
		Object[] result = {initialValue};
		forEach(entity -> {
			A value = transformer.apply(entity);
			result[0] = accumulator.apply(value, (A) result[0]);
		});
		return (A) result[0];
	}
	
	/**
	 * <p>Returns a {@link List} which contains all {@link Entity Entities} of this stream.</p>
	 * <p>No assumptions should be made about the ordering of Entities within the List or the 
	 * implementation of the List.</p>
	 * 
	 * <p>This is a terminal operation on the EntityStream.</p>
	 * 
	 * @return		a newly created non-null List containing all Entities of this stream
	 */
	public default List<T> createList() {
		List<T> result = new ArrayList<>();
		forEach(e -> result.add(e));
		return result;
	}
	
	/**
	 * <p>Returns a {@link Set} which contains all {@link Entity Entities} of this stream.</p>
	 * <p>No assumptions should be made about the implementation of the Set.</p>
	 * 
	 * <p>This is a terminal operation on the EntityStream.</p>
	 * 
	 * @return		a newly created non-null Set containing all Entities of this stream
	 */
	public default Set<T> createSet() {
		Set<T> result = new HashSet<>();
		forEach(e -> result.add(e));
		return result;
	}
	
	/**
	 * <p>Returns an EntityStream which contains only those {@link Entity Entities} from this stream 
	 * which are of the given {@link Class type} or sub-types of that type.</p>
	 * 
	 * <p>This is a decorating operation.</p>
	 * 
	 * @param entityType			a non-null {@link Class} which is a sub-type of {@link Entity}
	 * @return						a non-null EntityStream containing only entities of the given type (or of its sub-types)
	 * @see #filter(Predicate)
	 * @see Class#isInstance(Object)
	 */
	@SuppressWarnings("unchecked")
	public default <K extends Entity> EntityStream<K> ofType(Class<K> entityType) {
		return (EntityStream<K>) filter(e -> entityType.isInstance(e));
	}
	
	/**
	 * <p>Returns an EntityStream which contains only those {@link Entity Entities} from this stream 
	 * which are no further than {@code radius} many units of length away from the given point.</p>
	 * 
	 * <p>This is a decorating operation.</p>
	 * 
	 * @param x						the x-coordinate of the point around which entities are collected
	 * @param y						the y-coordinate of the point around which entities are collected
	 * @param radius				a radius within which entities are collected around the given point. Must be a non-negative number.
	 * @return						a non-null EntityStream containing only entities within the given radius around the given point
	 * @throws IllegalArgumentException		if radius is negative
	 * @see #filter(Predicate)
	 * @see #withinRadiusOfEntity(Entity, double)
	 * @see Entity#getDistanceTo(double, double)
	 */
	public default EntityStream<T> withinRadiusOfPoint(double x, double y, double radius) {
		if (radius < 0) {
			throw new IllegalArgumentException("radius="+radius);
		}
		return filter(e -> e.getDistanceTo(x, y) <= radius);
	}
	
	/**
	 * <p>Returns an EntityStream which contains only those {@link Entity Entities} from this stream 
	 * which are no further than {@code radius} many units of length away from the given entity.</p>
	 * 
	 * <p>This is a decorating operation.</p>
	 * 
	 * @param other					a non-null Entity
	 * @param radius				a radius within which entities are collected around the given entity. Must be a non-negative number.
	 * @return						a non-null EntityStream containing only entities within the given radius around the given entity
	 * @throws IllegalArgumentException		if radius is negative
	 * @see #filter(Predicate)
	 * @see #withinRadiusOfPoint(double, double, double)
	 * @see Entity#getDistanceTo(Entity)
	 */
	public default EntityStream<T> withinRadiusOfEntity(Entity other, double radius) {
		if (radius < 0) {
			throw new IllegalArgumentException("radius="+radius);
		}
		return filter(e -> e.getDistanceTo(other) <= radius);
	}
	
	/**
	 * <p>Returns an EntityStream which contains all {@link Entity Entities} from this stream 
	 * except the entity given as argument.</p>
	 * 
	 * <p>This is a decorating operation.</p>
	 * 
	 * @param entity				a non-null Entity which will be filtered from this stream
	 * @return						a non-null EntityStream containing all entities from this stream except {@code entity}
	 * @see #filter(Predicate)
	 */
	public default EntityStream<T> without(Entity entity) {
		return filter(e -> e != entity);
	}
	
	/**
	 * <p>Implementation of {@link EntityStream} which filters elements from a base stream which 
	 * do not pass a {@link Predicate#test(Object) condition}.</p>
	 * 
	 * <p>This is an example of the Decorator pattern</p>
	 *
	 * @param <T> the type of {@link Entity Entities} within this stream
	 */
	public static class FilteredEntityStream<T extends Entity> implements EntityStream<T> {
		
		protected final EntityStream<T> baseStream;
		protected Predicate<T> filter;
		
		public FilteredEntityStream(EntityStream<T> baseStream, Predicate<T> filter) {
			this.baseStream = baseStream;
			this.filter = filter;
		}
		
		public void forEach(Consumer<T> action) {
			baseStream.forEach(e -> {
				if (filter.test(e)) {
					action.accept(e);
				}
			});
		}
	}
	
}