package miniProject;

import java.util.ArrayList;

import DecisionTree.DecisionTree;

public class PredictMain {

	public static void main(String[] args) {

		if(args.length!=2){
			System.err.println("invalid number of arguments");
			System.exit(1);
		}
		String tree_filename = args[0];
		String testset_filename = args[1];
		ArrayList<Example> test_set = tree_Utilities.generateSetFromCsv(testset_filename);
		DecisionTree tree_from_file = readTree(tree_filename);
		tree_from_file.predictAndPrint(test_set);
	}

	public static DecisionTree readTree (String tree_filename) {
		DecisionTree tree_from_file = new DecisionTree();
		tree_from_file.setRoot(tree_Utilities.readeTreeFromFile(tree_filename));
		return tree_from_file;
	}
}
