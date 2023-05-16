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
	
	/*
	 * 각 노드의 key List와 비교하며 e가 존재하는지 확인하고,
	 * 없다면 현재 비교하고 있는 인덱스를 따라서 자식 노드로 내려가서
	 * 재귀적으로 탐색함
	 */
	public boolean contains(Integer e) {
		if(keyList.size() == 0) return false;
		
		int i=0;
		while(i < keyList.size()) {
			int v = keyList.get(i);
			if(v == e) return true;
			else if(v > e && i < children.size()) 
				return children.get(i).contains(e);
			
			i++;
		}
		if(i >= children.size()) return false;
		
		return children.get(i).contains(e);
	}
	
	/*
	 * contains 함수와 동일하게 
	 * 현재 갖고 있는 keyList와 비교하며
	 * 들어가야할 위치를 찾아내려감.
	 * 
	 * 원소의 삽입은 리프 노드에서 발생함.
	 */
	public boolean add(Integer e) {
		if(children.size() == 0)//leaf node
			return addKey(e);
		
		for(int i=0; i<keyList.size(); ++i) {
			int v = keyList.get(i);
			
			if(v == e) return false;
			else if(v > e && i < children.size()) 
				return children.get(i).add(e);
			
		}
		return children.get(children.size()-1).add(e);
	}
	
	/*
	 * 3 way b-tree 에서 Max Key Size 는 3임.
	 * 
	 * 만약에 이 함수를 호출한 노드가 루트라면, 
	 * 현재 키에서 가운데 값이 루트가 되고,
	 * 좌,우 키 값은 각각 루트의 자식 노드가 됨.
	 * 
	 * 리프 노드던지, 내부 보드던지 
	 * 부모 노드에 현재 키 중 가운데 값을 넣어주고
	 * 가장 큰 값은 오른쪽 자식 노드로 넣어준다는 건 동일함.
	 */
	private void balanceMaxKeyProperty() {
		if(keyList.size() < 3) return;
		
		
		if(this.parent == null) { // root node
			MyThreeWayBTreeNode left = new MyThreeWayBTreeNode();
			MyThreeWayBTreeNode right = new MyThreeWayBTreeNode();
			
			left.addKey(keyList.remove(0));
			right.addKey(keyList.remove(1));
			
			if(children.size() == 4) {
				for(var c: this.children.subList(0, 2)) 
					left.addChild(c);
				
				for(var c: this.children.subList(2, 4)) 
					right.addChild(c);
			}
			
			this.children.clear();
			this.addChild(left);
			this.addChild(right);
			
		} else {
			var node = new MyThreeWayBTreeNode();
			node.addKey(keyList.remove(2));
			parent.addChild(node);
			
			if(children.size() == 4) {
				node.addChild(children.remove(2));
				node.addChild(children.remove(2));
			}
			
			int midK = this.keyList.remove(1);
			parent.addKey(midK);
		}
	}
	
	/*
	 * 실제적인 키 삽입이 발생하며,
	 * 키 삽입 과정에서 유일하게 balanceMaxKeyProperty 를 호출하는 함수임.
	 */
	private boolean addKey(int e) {
		int idx = 0, v;
		while(idx < keyList.size()) {
			v = keyList.get(idx);
			if(v > e) break;
			else if(v == e) return false;
			idx++;
		}
		
		keyList.add(idx, e);
		
		balanceMaxKeyProperty();
		
		return true;
	}
	
	/*
	 * 현재 노드의 자식 리스트에 노드를 추가하는 함수임.
	 * 
	 * 삽입할 노드의 가장 작은 키 값과
	 * 자식 리스트의 각 노드의 가장 작은 키 값을 비교해서
	 * 넣을 위치를 정함.
	 */
	private void addChild(MyThreeWayBTreeNode e) {
		
		int idx = 0, minV = e.keyList.get(0);
		while(idx < children.size()) {
			var child = children.get(idx);
			if(child.keyList.get(0) > minV) break;
			
			idx++;
		}
		
		children.add(idx, e);
		e.parent = this;
	}
	
	/*
	 * e를 키 리스트에서 지우는 함수로서,
	 * 내부 노드라면 직전 수, 직후 수 중에 값을 가져와서 지운 값의 위치를 대체하고
	 * 그 노드에서부터 balanceMinKeyProperty 를 호출함.
	 * 
	 * 리프 노드라면 값을 지우고 바로 balanceMinKeyProperty를 호출함.
	 */
	public boolean delete(Integer e) {
		if(!contains(e)) return false;
		
		
		for(int i=0; i<keyList.size(); ++i) {
			int v = keyList.get(i);
			
			if(v == e) { // 노드 발견
				keyList.remove(i);
				
				if(children.size() == 0) { //leaf node
					balanceMinKeyProperty();
					
				} else { //internal node
					var leftSucc = children.get(i);
					while(leftSucc.children.size() != 0) 
						leftSucc = leftSucc.children.get(leftSucc.children.size()-1);
					
					var rightSucc = children.get(i+1);
					while(rightSucc.children.size() != 0) 
						rightSucc = rightSucc.children.get(0);
					
					int leftKeySize = leftSucc.keyList.size();
					int rightKeySize = rightSucc.keyList.size();
					if(leftKeySize > 1 || leftKeySize == rightKeySize) {
						int k = leftSucc.keyList.remove(leftKeySize-1);
						addKey(k);
						leftSucc.balanceMinKeyProperty();
					} else {// if(rightSucc.keyList.size() > 1) {
						int k = rightSucc.keyList.remove(0);
						addKey(k);
						rightSucc.balanceMinKeyProperty();
					}
					
				}
					
				return true;
			} else if(v > e) 
				return children.get(i).delete(e);
			
		}
		
		return children.get(children.size()-1).delete(e);
	}
	
	/*
	 * 3 way b-tree의 min key size인 0이 아니라면 종료함.
	 * 
	 * 타 함수에서 첫 호출은 리프 노드에서만 발생함.
	 * 주의할 부분은 MinKeyProperty가 위배되면서 루트가 변하는 경우임.
	 * 
	 * sibling 노드의 키 리스트 사이즈가 1 이하라면, 
	 * 키 하나를 빌려왔을 때 그 노드도 MinKeyProperty가 위배되므로 Merge를 진행함.
	 * Merge를 했을 때 부모 노드의 키를 하나 가져오므로 부모에서도 MinKeyProperty가 위배될 가능성이 있기에 해당 함수를 호출함.
	 * 
	 * sibling 노드의 키 리스트 사이즈가 2 이상이라면,
	 * 키 하나를 빌려왔을 때 그 노드도 MinKeyProperty가 위배되지 않음.
	 * 따라서 부모 노드에서 해당하는 키를 가져와서 현재 노드에 넣어주고,
	 * sibling 노드의 키를 부모 노드로 넣어줌.
	 * 내부 노드라면 키를 빌려올 때, 자식도 데려오는 걸 주의해야 함.
	 */
	private void balanceMinKeyProperty() {
		if(keyList.size() > 0) return;
		else if(parent == null) { // MinKeyProperty가 위배되면서 root라면 자식이 하나인 루트가 변하는 경우
			children.get(0).parent = null;
			return;
		}
		
		int id = 0;
		var pChilds = parent.children;
		for(int i=0; i < pChilds.size(); ++i) {
			if(pChilds.get(i) == this) {
				id = i;
				break;
			}
		}
		
		int siblingId = Math.abs(id-1), pId = 0;
		if(id == 2) pId = 1;
		var sibling = pChilds.get(siblingId);
		
		if(sibling.keyList.size() <= 1) {
			
			sibling.addKey(parent.keyList.remove(pId));
			for(var c: children)
				sibling.addChild(c);
			parent.children.remove(id);
			parent.balanceMinKeyProperty();
			
			return;
		} 
		
		int pKey = parent.keyList.remove(pId);
		addKey(pKey);
		if(sibling.keyList.get(0) > pKey) {
			parent.addKey(sibling.keyList.remove(0));
			
			if(!children.isEmpty())
				addChild(sibling.children.remove(0));
		}
		else {
			parent.addKey(sibling.keyList.remove(sibling.keyList.size()-1));
			
			if(!children.isEmpty())
				addChild(sibling.children.remove(sibling.keyList.size()-1));
		}
			
		
	}
	
	/*
	 * 재귀적으로 중위순회를 하며 q에 각 키를 추가함.
	 */
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
