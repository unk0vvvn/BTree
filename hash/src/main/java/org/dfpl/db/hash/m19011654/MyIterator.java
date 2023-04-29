package org.dfpl.db.hash.m19011654;

import java.util.Iterator;
import java.util.LinkedList;

public class MyIterator<T> implements Iterator<T> {
	
	private LinkedList<Iterator<T>> its;
	
	public MyIterator(){
		its = new LinkedList<>();
	}
	
	@Override
	public boolean hasNext() {
		for(Iterator<T> it : its) {
			if(it.hasNext()) 
				return true;
		}
		return false;
	}

	@Override
	public T next() {
		for(Iterator<T> it : its) {
			if(it.hasNext()) 
				return it.next();
		}
		return null;
	}
	
	public MyIterator<T> concat(Iterator<T> it) {
		if(it != null) its.add(it);
		return this;
	}
}
