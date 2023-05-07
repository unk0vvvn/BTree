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

	
	public List<Integer> getKeys(){
		return keyList;
	}
	
	public List<MyThreeWayBTreeNode> getChilds(){
		return children;
	}
	
	public boolean contains(Integer e) {
		if(keyList.size() == 0) return false;
		
		int i=0;
		while(i < keyList.size()) {
			int v = (int) keyList.get(i);
			if(v == e) return true;
			else if(v > e && i < children.size()) 
				return children.get(i).contains(e);
			
			i++;
		}
		if(i >= children.size()) return false;
		
		return children.get(i).contains(e);
	}
	
	public boolean add(Integer e) {
		if(children.size() == 0) { //leaf node
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
				keyList.add(e);
			
			balanceMaxKeyProperty();
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
	
	private void balanceMaxKeyProperty() {
		if(keyList.size() < 3) return;
		
		
		if(this.parent == null) { // root node
			MyThreeWayBTreeNode left = new MyThreeWayBTreeNode();
			MyThreeWayBTreeNode right = new MyThreeWayBTreeNode();
			
			left.addKey(keyList.remove(0));
			right.addKey(keyList.remove(1));
			
			left.parent = this;
			right.parent = this;
			
			if(children.size() == 4) {
				for(var c: this.children.subList(0, 2)) 
					left.addChild(c);
				
				for(var c: this.children.subList(2, 4)) 
					right.addChild(c);
			}
			
			this.children.clear();
			this.children.add(left);
			this.children.add(right);
			
		} else {
			var node = new MyThreeWayBTreeNode();
			node.addKey(keyList.remove(2));
			node.parent = parent;
			parent.children.add(node);
			
			if(children.size() == 4) {
				node.addChild(children.remove(2));
				node.addChild(children.remove(2));
			}
			
			int midK = this.keyList.remove(1);
			parent.addKey(midK);
		}
	}
	
	private void addKey(int e) {
		int idx = 0;
		while(idx < keyList.size() && keyList.get(idx) < e)
			idx++;
		
		keyList.add(idx, e);
		
		balanceMaxKeyProperty();
	}
	
	private void addChild(MyThreeWayBTreeNode e) {
		int idx = 0, minV = e.keyList.get(0);
		
		while(idx < children.size()) {
			var child = children.get(idx);
			if(child.getKeys().get(0) > minV) break;
			
			idx++;
		}
		
		children.add(idx, e);
		e.parent = this;
	}
	
	public boolean delete(Integer e) {
		if(!contains(e)) return false;
		
		boolean isRemoved = false;
		for(int i=0; i<keyList.size(); ++i) {
			int k = keyList.get(i);
			if(k == e) {
				keyList.remove(i);
				isRemoved = true;
				break;
			} else if(k > e)
				return children.get(i).delete(e);
		}
		if(!isRemoved)
			return children.get(children.size()-1).delete(e);
		
		balanceMinKeyProperty();
		
		return true;
	}
	
	private void balanceMinKeyProperty() {
		if(keyList.size() > 0 || parent == null) return;
		
		if(children.size() == 2) { //internal node
			var leftSuccessor = getLeftSuccessor();
			var rightSuccessor = getRightSuccessor();
			
			if(leftSuccessor.keyList.size() > 1) {
				Integer borrowK = leftSuccessor.keyList.remove(keyList.size()-1);
				this.addKey(borrowK);
				
			} else if(rightSuccessor.keyList.size() > 1) {
				Integer borrowK = rightSuccessor.keyList.remove(keyList.size()-1);
				this.addKey(borrowK);
				
			} else {
				Integer borrowK = leftSuccessor.keyList.remove(keyList.size()-1);
				this.addKey(borrowK);
				
				leftSuccessor.balanceMinKeyProperty();
			}
			
		} else { 
			int mergeIdx = 0, borrowIdx = 0, i;
			
			for(i=0; i<parent.children.size(); ++i) {
				var child = parent.children.get(i);
				if(child.keyList.size() == 0) { //deleted node
					if(i == 0) 
						mergeIdx = 1;
					else 
						mergeIdx = i-1;
					
					borrowIdx = i / 2;
					
					break;
				}
			}
			

			var mergeNode = parent.children.get(mergeIdx);
			mergeNode.addKey(parent.keyList.remove(borrowIdx));
			
			if(children.size() == 1)  // after leaf node balancing
				mergeNode.addChild(children.get(0));
			
			parent.children.remove(i);
		}
		
		parent.balanceMinKeyProperty();
	}
	
	private MyThreeWayBTreeNode getLeftSuccessor() {
		if(children.size() == 0) return null;
		
		var ret = children.get(0);
		while(ret.children.size() != 0) {
			var childs = ret.children;
			ret = childs.get(childs.size()-1);
		}
		
		return ret;
	}
	
	private MyThreeWayBTreeNode getRightSuccessor() {
		if(children.size() == 0) return null;
		
		var ret = children.get(children.size()-1);
		while(ret.children.size() != 0) {
			var childs = ret.children;
			ret = childs.get(0);
		}
		
		return ret;
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
