package DecisionTree;


import java.util.*;
public class BinaryTree<T> {
	
	protected BinaryNode<T> root;

	public BinaryTree(){
		root = null;
	}

	public boolean isEmpty(){
		return root == null;
	}

	public BinaryNode<T> getRoot() {
		return root;
	}

	public void setRoot(BinaryNode<T> root) {
		this.root = root;
	}

	public ArrayList <BinaryNode<T>> getTreeLeafs(){
		ArrayList <BinaryNode<T>> toReturn = new ArrayList <BinaryNode<T>>();
		root.getTreeLeafs(toReturn);
		return toReturn;
	}
	public boolean equals(Object other){
		boolean isEqual = false;
		if(other instanceof BinaryTree<?>) {
			BinaryTree<?> otherTree = (BinaryTree<?>) other;
			if (isEmpty())
				isEqual = otherTree.isEmpty();
			else
				isEqual = root.equals(otherTree.root);
		}
		return isEqual;

	}


}
