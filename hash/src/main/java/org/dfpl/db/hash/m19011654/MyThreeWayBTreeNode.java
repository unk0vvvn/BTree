package org.dfpl.db.hash.m19011654;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class MyThreeWayBTreeNode {
	private MyThreeWayBTreeNode parent;
	private List<Integer> keyList;
	private List<MyThreeWayBTreeNode> children;
	
	MyThreeWayBTreeNode(){
		parent = null;
		keyList = new LinkedList<>();
		children = new LinkedList<>();
	}
	
	public MyThreeWayBTreeNode getParent() {
		return parent;
	}
	
	public List<Integer> getKeys(){
		return keyList;
	}
	
	public List<MyThreeWayBTreeNode> getChilds(){
		return children;
	}
	
	public boolean contains(int e) {
		if(keyList.size() == 0) return false;
		
		int i=0;
		while(i < keyList.size()) {
			int v = (int) keyList.get(i);
			if(v == e) return true;
			else if(v > e) {
				if(i >= children.size()) return false;
				
				return children.get(i).contains(e);
			}
			
			
			i++;
		}
		if(i >= children.size()) return false;
		
		return children.get(i).contains(e);
	}
	
	public boolean add(int e) {
		if(children.size() == 0) {
			int i=0;
			while(i<keyList.size()) {
				int v = (int) keyList.get(i);
				if(v == e) return false;
				else if(v > e) {
					keyList.add(i,e);
					break;
				}
				i++;
			}
			if(i == keyList.size())
				keyList.add(i,e);
			
			reconstruct();
			return true;
		}
		
		for(int i=0; i<keyList.size(); ++i) {
			int v = keyList.get(i);
			if(v == e) return false;
			else if(v > e) {
				return children.get(i).add(e);
			}
		}
		return children.get(children.size()-1).add(e);
	}
	
	private void reconstruct() {
		if(keyList.size() < 3) return;
		
		MyThreeWayBTreeNode left = new MyThreeWayBTreeNode();
		MyThreeWayBTreeNode right = new MyThreeWayBTreeNode();
		
		left.add(keyList.get(0));
		right.add(keyList.get(keyList.size()-1));
		
		if(this.parent == null) {
			if(children.size() != 0) {
				left.children = this.children.subList(0, 2);
				right.children = this.children.subList(2, 4);
			}
			this.keyList = this.keyList.subList(1, 2);
			
			this.children.clear();
			this.children.add(left);
			this.children.add(right);
		} else {
			int midK = this.keyList.get(1);
			
			var parentKeys = parent.keyList;
			var parentChilds = parent.children;
			
			int i=0;
			while(i<parentKeys.size()) {
				if(parentKeys.get(i) > midK) {
					parentKeys.add(i, midK);
					
					parentChilds.add(i, left);
					parentChilds.add(i+1, right);
					
					break;
				}
				
				i++;
			}
			if(i == parentKeys.size()) {
				parentKeys.add(i, midK);
				
				parentChilds.add(i, left);
				parentChilds.add(i+1, right);
			}
			
			parent.reconstruct();
		}
	}
	
	public void traverse(List<Integer> q){
		for(int i=0; i<keyList.size(); ++i) {
			if(i < children.size())
				children.get(i).traverse(q);
			q.add(keyList.get(i));
		}
		if(!children.isEmpty())
			children.get(children.size()-1).traverse(q);
	}
	
}
