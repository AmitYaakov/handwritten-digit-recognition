package Constraints;
import DecisionTree.BinaryNode;
import miniProject.Example;
import DecisionTree.NodeData;

import java.util.*;

public class Type4Constraint extends Constraint {
    
	private int value;
	private int row;
	private int col;
	
	
	public Type4Constraint(int row, int col, int value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}

	public String toString() {

		return "_4:"+ this.row +"\t" + this.col + "\t>\t" + this.value;
	}

	/**
	 * given an example check if it satisfieis the constraint.
	 * @param example
	 * @return
	 */
	public boolean checkConstraint(Example example) {
			int sum = 0;
			for (int i = 0; i < 28; i++) {
				sum = sum + example.getImage()[row][i];
			}
			for (int i = 0; i < 28; i++) {
				sum = sum + example.getImage()[i][col];
			}
			return (sum >= value);
	}
}