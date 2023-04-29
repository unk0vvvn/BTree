package org.dfpl.db.hash.m19011654;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.xml.transform.stream.StreamSource;

public class MyHashSet<T> implements Set<Integer> {

	// 멤버 변수는 hashTable 이외의 것을 사용하지 않습니다. (예: size)
	private MyThreeWayBTree[] hashTable;

	public MyHashSet() {
		hashTable = new MyThreeWayBTree[3];
	}
	
	private int getHash(Integer e) {
		return e % 3;
	}

	@Override
	public int size() {
		int size = 0;
		for (MyThreeWayBTree t : hashTable) {
			size += t.size();
		}
		return size;
	}

	@Override
	public boolean isEmpty() {
		if(size() == 0) return true;
		return false;
	}

	@Override
	public boolean contains(Object o) {
		int hash = getHash((Integer)o);
		if(hashTable[hash].contains(o)) return true;
		return false;
	}

	@Override
	public Iterator<Integer> iterator() {
		MyIterator<Integer> ret = new MyIterator<>();
		for(MyThreeWayBTree e: hashTable) {
			ret.concat(e.iterator());
		}
		
		return ret;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Integer e) {
		if(contains(e)) return false;
		
		hashTable[getHash(e)].add(e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

}
