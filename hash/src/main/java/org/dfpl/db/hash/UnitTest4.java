package org.dfpl.db.hash;

import java.util.Iterator;
import java.util.Set;

import org.dfpl.db.hash.m19011654.MyHashSet;

public class UnitTest4 {
	public static void main(String[] args) {
		Set<Integer> set = new MyHashSet<Integer>();
		System.out.println("[1] " + set.isEmpty());
		for (int i = 0; i < 30; i++) {
			set.add(i);
		}
		System.out.println("[2] " + set.contains(3));
		System.out.println("[3] " + set.contains(30));
		System.out.println("[4] " + set.isEmpty());
		System.out.println("[5] " + set.size());
		Iterator<Integer> iter = set.iterator();
		String join = "";
		while (iter.hasNext()) {
			join += iter.next() + ",";
		}
		join = join.substring(0, join.length() - 1);
		// B-Tree 점수 획득을 위해서는 각 Hash Table Bucket 내의 요소는 '오름차순'으로 출력되어야 한다.
		// 부분점수를 위한 구현이라면 순서 상관없이 모든 항목이 출력되어야 한다. 
		// 예 1: 0,3,6,9,12,15,18,21,24,27,1,4,7,10,13,16,19,22,25,28,2,5,8,11,14,17,20,23,26,29
		// 예 2: 1,4,7,10,13,16,19,22,25,28,0,3,6,9,12,15,18,21,24,27,2,5,8,11,14,17,20,23,26,29
		System.out.println("[6] " + join);
		
	}
}
