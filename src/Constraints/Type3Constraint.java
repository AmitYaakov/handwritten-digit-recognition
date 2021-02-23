package Constraints;
import DecisionTree.BinaryNode;
import miniProject.Example;
import DecisionTree.NodeData;

import java.util.*;

public class Type3Constraint extends Constraint {

	private int diagonal;
	private int value;

	public Type3Constraint(int diagonal, int value) {
		this.diagonal = diagonal;
		this.value = value;
	}

	public String toString() {
		return "_3:" + this.diagonal +"\t>\t"+ this.value;
	}

	/**
	 * given an example check if it satisfieis the constraint.
	 * @param example
	 * @return
	 */
	public boolean checkConstraint(Example example) {

		int number=0;
		int diagonal_sum = 0;
		if (diagonal >= 0) {
			for (int i = 0; i < 28 - diagonal; i++) {
				diagonal_sum = diagonal_sum + example.getImage()[i][i + diagonal];
				number++;
			}

			return diagonal_sum>=number*0.5*value;
		} else {
			for (int i = 0; i < 28 + diagonal; i++) {
				diagonal_sum = diagonal_sum + example.getImage()[i - diagonal][i];
				number++;
			}
			return diagonal_sum>=number*0.5*value;
		}
	}
}
