package org.dfpl.db.hash.m19011654;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class BTreeIterator implements Iterator{
	private List<Integer> q;
	private int cur;
	
	BTreeIterator(MyThreeWayBTree t){
		q = new ArrayList<>();
		
		t.getRoot().traverse(q);
		cur = 0;
	}
	
	@Override
	public boolean hasNext() {
		if(cur < q.size())return true;
		
		return false;
	}

	@Override
	public Integer next() {
		int ret = q.get(cur);
		cur++;
		
		return ret;
	}

}
