package DecisionTree;
import Constraints.Constraint;
import miniProject.Example;

import java.util.*;

public class NodeData {
	
	protected int label; 
	protected  ArrayList<Example> training_set;
	protected double entropy;
	protected Constraint constraint;
    protected int k;
    Set<Integer> images_indexes;
    protected Double IG;



	public NodeData(int label, ArrayList<Example> training_set , Set<Integer> images_indexes) {
		super();
		this.label = label;
		this.training_set = training_set;
		this.entropy = 0;
		this.constraint = null;
		this.k = 9;
		this.images_indexes = images_indexes;
		this.IG = null;
	}
	
	public NodeData(int label , ArrayList<Example> training_set) {
		this.label = label;
		this.entropy = 0;
		this.constraint = null;
		this.k = 9;
		this.training_set = training_set;
		this.images_indexes = new HashSet<>();
		this.IG = null;
	}

	public NodeData() {

	}
	
	public ArrayList<Example> getTraining_set() {
		return training_set;
	}


    public void CalculateEntropy(){
    	int N_L = images_indexes.size();
    	if (N_L == 0 ) {
    		entropy = 0;
    	} 
    	else {
    		double H_L = 0;
    		int [] N_i_arr = CalculateNi();
			for (int i=0; i <=k ; i++) {
				int N_i = N_i_arr[i];
				if (N_i != 0 ) {
					double x = (double) N_i / N_L;
					H_L += x*Math.log(1/x);
				}
			}
			entropy = H_L;
    	}
    }
    
    public Set<Integer> getImages_indexes() {
		return images_indexes;
	}



	public int [] CalculateNi () { 
    	
		int [] to_return = new int [k+1];
		for(Integer index : images_indexes){
			int label = training_set.get(index).getLabel();
			to_return[label]++;
		}
         return to_return;        
    }

	
	public ArrayList<Example> getExamples() {
		return training_set;
	}

	public void setExamples( ArrayList<Example> examples) {
		this.training_set = examples;
	}

	public double getEntropy() {
		return entropy;
	}


	public Constraint getConstraint() {
		return constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}


	public int getLabel() {
		return label;
	}

	public Double getIG() {
		return IG;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	
	public  void updateMaxIG(Double IG , Constraint con) {
		if (this.IG  == null) {
			this.IG = IG;
			this.constraint = con;

		}
		else if (this.IG < IG) {
			this.IG = IG;
			this.constraint = con;
		}
	}
    
	
	public String toString() {
		String string_to_return = "";
		if (label != -1) {
			string_to_return += "L: " + label;
		}
		else if (constraint != null) {
			string_to_return += "C" + constraint.toString() + " ";
		}

		return string_to_return;
	}
}
