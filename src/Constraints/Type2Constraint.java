package Constraints;

import java.util.ArrayList;

import DecisionTree.BinaryNode;
import miniProject.Example;
import DecisionTree.NodeData;

public class Type2Constraint extends Constraint {

	private int x;
	private int y;
	private int value;
	
	public Type2Constraint(int x, int y, int value) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
	}

	public String toString() {
		return "_2:"+ this.x +"\t" + this.y + "\t>\t" + this.value;
	}
	
	@Override
	public boolean checkConstraint(Example example){
		if (example.getImage()[x][y] > value) {
			return true;
		}
		return false;
	}
	

}
