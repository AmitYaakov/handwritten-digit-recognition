package Constraints;
import DecisionTree.BinaryNode;
import miniProject.Example;
import DecisionTree.NodeData;

import java.util.*;

public class Type1Constraint extends Constraint {
    
	private int x;
	private int y;
	private int value;

	public Type1Constraint(int x, int y, int value) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
	}

	public String toString() {
		return "_1:"+ this.x +"\t" + this.y + "\t>\t" + this.value;
	}

	/**
	 * given an example check if it satisfieis the constraint.
	 * @param example
	 * @return
	 */
	public boolean checkConstraint(Example example) {
		if (example.getImage()[x][y] > value) {
			return true;
		}
		return false;
	}
			
}
