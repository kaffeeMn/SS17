package edu.udo.cs.swtsf.util;

import java.util.function.Supplier;

/**
 * <p>The {@link GroupFactory} is a static global Singleton used to create all 
 * instances of {@link Group} within the framework. It is not necessary to have 
 * more than one instance of GroupFactory.</p>
 */
public class GroupFactory {
	
	/**
	 * Holds the singleton instance of the GroupFactory.
	 */
	private static GroupFactory INSTANCE;//Lazily initialized
	
	/**
	 * <p>Returns the global instance of {@link GroupFactory}. The returned 
	 * instance should always be the same instance. Caching is not required.</p>
	 * @return	a non-null instance of {@link GroupFactory}
	 */
	public static GroupFactory get() {
		if (INSTANCE == null) {
			INSTANCE = new GroupFactory();
		}
		return INSTANCE;
	}
	
	/*
	 * Constructor is private. Construction outside of this class is not supported.
	 */
	private GroupFactory() {}
	
	/**
	 * The {@link Supplier} used as the Factory-Object to create {@link Group} instances.
	 * By default the supplier always generates instances of {@link BufferedGroup}.
	 */
	private Supplier<Group<?>> sup = () -> new BufferedGroup<>();
	
	/**
	 * <p>Sets the factory object for the creation of {@link Group} 
	 * instances. The factory must always produce a valid, non-null 
	 * Group instance.</p>
	 * @param supplier		a non-null {@link Supplier} for arbitrary {@link Group Groups}
	 */
	public void setSupplier(Supplier<Group<?>> supplier) {
		if (supplier == null) {
			throw new IllegalArgumentException("supplier == null");
		}
		sup = supplier;
	}
	
	/**
	 * <p>Creates a new instance of {@link Group} and returns it. 
	 * No assumptions should be made about the implementation.</p>
	 * @return		returns a non-null instance of Group
	 */
	@SuppressWarnings("unchecked")
	public <E> Group<E> createNewGroup() {
		Group<E> group = (Group<E>) sup.get();
		if (group == null) {
			throw new IllegalStateException("supplier generated null group");
		}
		return group;
	}
	
}