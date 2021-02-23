package miniProject;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import Constraints.*;
import DecisionTree.BinaryNode;
import DecisionTree.NodeData;

import static java.lang.System.exit;

public class tree_Utilities {

 
	public static ArrayList<BinaryNode<NodeData>> generate_children(Constraint con ,BinaryNode<NodeData> leaf){
		NodeData leaf_data = leaf.getData();
		BinaryNode<NodeData> l_a = new BinaryNode<NodeData>(new NodeData(-1 ,leaf_data.getTraining_set())); //La
		BinaryNode<NodeData> l_b = new BinaryNode<NodeData>(new NodeData(-1,leaf_data.getTraining_set())); //Lb
		NodeData l_a_data = l_a.getData();
		NodeData l_b_data = l_b.getData();
		for(Integer index : leaf_data.getImages_indexes()){
			Example example =leaf_data.getExamples().get(index);
			if(con.checkConstraint(example))  l_a_data.getImages_indexes().add(index);
			else  l_b_data.getImages_indexes().add(index);

			

		}
		ArrayList<BinaryNode<NodeData>> children = new ArrayList<BinaryNode<NodeData>>();
		children.add(l_a);
		children.add(l_b);
		return children;
	}
	public static void apply_new_node(Constraint con, BinaryNode<NodeData> leaf){
		ArrayList<BinaryNode<NodeData>> children = generate_children(con, leaf);
		leaf.setRight(children.get(0));
		leaf.setLeft(children.get(1));

	}
	
	/**
	 * 
	 * @param leaf- parent
	 * @param l_a - "success node"
	 * @param l_b - "fail node"
	 * @return H_X
	 */
	public static double calculateH_X(BinaryNode<NodeData> leaf,BinaryNode<NodeData> l_a,BinaryNode<NodeData> l_b){
			ArrayList<Example> examples = leaf.getData().getExamples();
			NodeData node_data = leaf.getData();
			NodeData l_a_data = l_a.getData();
			NodeData l_b_data = l_b.getData();
	
			float N_l = node_data.getImages_indexes().size();
			if (N_l == 0) {
				return 0;
			} else {
				double N_la = l_a_data.getImages_indexes().size();
				double N_lb =  l_b_data.getImages_indexes().size();
				l_a_data.CalculateEntropy();
				double H_la = l_a_data.getEntropy();
				l_b_data.CalculateEntropy();
				double H_lb = l_b_data.getEntropy();
				double H_X = ((N_la/N_l) * H_la) + ((N_lb/N_l) * H_lb);
				return H_X;
			}
	}
	
	/**
	 * given a parent, construct 2 children (success and fail), and NOT connecting them to him.
	 */
	public static  double  check(Constraint con, BinaryNode<NodeData> leaf){
		ArrayList<BinaryNode<NodeData>> children = generate_children(con, leaf);
		BinaryNode<NodeData> l_a =  children.get(0);
		BinaryNode<NodeData> l_b = children.get(1);
		double H_X = calculateH_X(leaf,l_a,l_b);
		double H_l = leaf.getData().getEntropy();
		return H_l - H_X;
	}
	
	 public static BinaryNode<NodeData> readeTreeFromFile (String file_name) {
		 
		String tree_string = readFileToString(file_name);
		String[] arr = tree_string.split(",");
		if (arr[1].equals("#")) {
		        return null;
		 }
		 
		BinaryNode<NodeData> root = new BinaryNode(createNodeData(arr[1]));
	    LinkedList<BinaryNode<NodeData>> tree_content = new LinkedList<>();
	    tree_content.offer(root);
	 
	    int i = 2;
       while (!tree_content.isEmpty()) {
       	BinaryNode<NodeData> curr = tree_content.poll();
           if (curr != null) {
           	BinaryNode<NodeData> left = null;
               if (!arr[i].contains("#")) {
                   left = new BinaryNode(createNodeData(arr[i]));
               }
               curr.setLeft(left);
               tree_content.offer(left);
               i++;

               BinaryNode<NodeData> right = null;
               if (!arr[i].contains("#")) {
                   right = new BinaryNode(createNodeData(arr[i]));
               }
               curr.setRight(right);
               tree_content.offer(right);
               i++;
           }
       }
       return root;			
	 }
	 
	 public static NodeData createNodeData (String string_node_data) {
		 NodeData to_return = new NodeData();
		 String [] content = string_node_data.split(":");
		 if (content[0].equals("L")) {
			 to_return.setLabel(Integer.parseInt(content[1].trim()));
			 } else {
			 String [] constraint = content[0].split("_");
			 if (Integer.parseInt(constraint[1]) == 1 || Integer.parseInt(constraint[1]) == 2 ) {
				 String [] c_1 = content[1].split("\t");
				 String x = c_1[0];
				 String y= c_1[1];
				 String value = c_1[3];

				 if (Integer.parseInt(constraint[1]) == 1) {
					 to_return.setConstraint(new Type1Constraint(
							 Integer.valueOf(x.trim()),
							 Integer.valueOf(y.trim()),
							 Integer.valueOf(value.trim())));
				 }
				 else {
					 to_return.setConstraint(new Type2Constraint(
							 Integer.valueOf(x.trim()),
							 Integer.valueOf(y.trim()),
							 Integer.valueOf(value.trim())));
				 }
			 }
			 else if(Integer.parseInt(constraint[1]) == 3){
				 String [] c_1 = content[1].split("\t");
				 String diag = c_1[0];
				 String value= c_1[2];
				 to_return.setConstraint(new Type3Constraint(Integer.valueOf(diag.trim()),
						 Integer.valueOf(value.trim())));

			 }
			 else if(Integer.parseInt(constraint[1]) == 4) {
				 String [] c_1 = content[1].split("\t");
				 String row = c_1[0];
				 String col= c_1[1];
				 String value = c_1[3];
				 to_return.setConstraint(
				 		new Type4Constraint(
								Integer.valueOf(row.trim()),
								Integer.valueOf(col.trim()),
								Integer.valueOf(value.trim())));

			 }
			 else if(Integer.parseInt(constraint[1]) == 5){
				 String [] c_1 = content[1].split("\t");
				 String x1 = c_1[0];
				 String y1= c_1[1];
				 String x2 = c_1[2];
				 String y2 = c_1[3];
				 String value = c_1[5];
				 to_return.setConstraint(new Type5Constraint(
						 Integer.valueOf(x1.trim()),
						 Integer.valueOf(y1.trim()),
						 Integer.valueOf(x2.trim()),
						 Integer.valueOf(y2.trim()),
						 Integer.valueOf(value.trim())));
			 }
			 }
		 return to_return;
	 }
	 
	 public static String readFileToString (String file_name) {
		 try {
			 File tree_file = new File(file_name);
			 FileReader file_reader = new FileReader(tree_file); 
	    	 BufferedReader buffer_reader = new BufferedReader(file_reader);
	    	 StringBuffer string_buffer = new StringBuffer();
	    	 String line;
	    	 while ((line = buffer_reader.readLine()) != null) {
	    		 string_buffer.append(line);
	    		 string_buffer.append("\n");
	    	 }
	    	 file_reader.close();
	 		return  string_buffer.toString();
			 
		 } catch (IOException e) {
			 System.err.println("Cant open file.");
			 exit(1);
		 }
		return null;
	 }
	 
	 public static ArrayList<Example> generateSetFromCsv (String file_name){
		 ArrayList<Example> examples = new ArrayList<Example>();		 
		    try {
		    	FileReader file_reader = new FileReader(file_name);
		    	try {
		    		BufferedReader buffer_reader = new BufferedReader(file_reader);
		    		String line;
				    while ((line = buffer_reader.readLine()) != null) {			 	
				        String[] values = line.split(",");
						int label = Integer.valueOf(values[0]);
						int [][] pixels = generateMatrix(values);
						Example ex = new Example(label, pixels);
						examples.add(ex);
				    }
				    return examples;	     
		    	} catch (IOException e) {
		    		System.err.println("Cant open file.");
					exit(1);
			    }
		    } catch (IOException e) {
				System.err.println("Cant open file.");
				exit(1);
		    }			
		 return null;
	 }

	 public static int[][] generateMatrix (String[] pixels){
		 int [][] matrix = new int[28][28];
		 int k = 1;
		 for(int i = 1; i < 28; i++){
			 for (int j = 1; j < 28; j++) {
				 matrix[i][j] = Integer.valueOf(pixels[k]);
				 k++;
			 }	
		 }
		 return matrix;	
	 }
	 
	
}
