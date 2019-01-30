package gui;

import java.util.Collection;
import java.util.HashSet;

public class CheckedHashSet<E> extends HashSet<E> {

	private static final long serialVersionUID = 1L;
	
	public CheckedHashSet() {
		super();
	}
	
	public CheckedHashSet(Collection<? extends E> c) {
		super(c.size());
		c.stream().forEach(object -> add(object));
	}
	
	public CheckedHashSet(int initialCapacity) {
		super(initialCapacity);
	}
	
	public CheckedHashSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	@Override
	public boolean add(E e) {
		if(!super.add(e)) {
			throw new IllegalArgumentException("This set already contains " + e.toString());
		}
		return true;
	}
	
	@Override
	public boolean remove(Object o) {
		if(!super.remove(o)) {
			throw new IllegalArgumentException("This set does not contain " + o.toString());			
		}
		return true;
	}
	
	@Override
	public Object clone() {
		return new CheckedHashSet<>(this);
	}
}
