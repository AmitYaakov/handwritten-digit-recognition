package DecisionTree;

import java.util.*;

public class BinaryNode<T> {

	protected T data;
	protected BinaryNode<T> left;
	protected BinaryNode<T> right;
	

	
	public BinaryNode(T element) {
		if (element == null) 
			throw new NullPointerException();
		this.data = element;
		left = null;
		right = null;
	}

	public void setLeft(BinaryNode<T> left) {
		this.left = left;
	}

	public void setRight(BinaryNode<T> right) {
		this.right = right;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}



	public int size() {
		int leftS = 0, rightS = 0;
		if(left != null) 
			leftS = left.size();
		if(right != null) 
			rightS = right.size();
		return leftS + rightS + 1;
	}
	
	public boolean isLeaf() {
		return right == null && left == null;
	}
	

	public void getTreeLeafs(ArrayList <BinaryNode<T>> leafs) {
		if (isLeaf()){  
		       leafs.add(this);
		    }  
	    if (right != null)  
	    	right.getTreeLeafs(leafs);  
	    if (left != null)  
	        left.getTreeLeafs(leafs);;  
	}


	
	public boolean equals(Object other) {
		boolean isEqual = false;
		if (other instanceof BinaryNode<?>) {
			BinaryNode<?> otherNode = (BinaryNode<?>) other;
			isEqual = data.equals(otherNode.data)
					&&
					((left == null & otherNode.left == null) |
							(left != null && left.equals(otherNode.left)))
					&&
					((right == null & otherNode.right == null) |
							(right != null && right.equals(otherNode.right)));
		}
		return isEqual;

	}

 
	public String toString() {
	    ArrayList<String> list = new ArrayList<>();
	    LinkedList<BinaryNode<T>> nodes_queue = new LinkedList<>();
	    nodes_queue.offer(this);
	
	    while (!nodes_queue.isEmpty()) {
	    	BinaryNode<T> current = nodes_queue.poll();
	        if (current == null) {
	            list.add("#");
	        } else {
		    	list.add(current.getData().toString());
		    	nodes_queue.offer(current.left);
		    	nodes_queue.offer(current.right);
		     }
	    }
	    return String.join(",", list);
	}  	 
    
}