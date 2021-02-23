package miniProject;

import DecisionTree.DecisionTree;

public class Main {

	public static void main(String[] args) {

		validateInput(args);
		Model model = new Model(Integer.valueOf(args[0]),
								Integer.valueOf(args[1]), 
								Integer.valueOf(args[2]), 
								args[3],args[4]);
		model.Init();
		DecisionTree tree = model.runLearningAlgorithm();// tree is the output!
		model.treeToFile(tree,Integer.valueOf(args[0]));
		System.exit(0);
		
	}

	public static void validateInput(String[] args) { // TO BE COMPLETED. exit if input is invalid.
		if(args.length!=5){
			System.err.println("invalid number of arguments");
			System.exit(1);
		}
		String version = args[0]; // version of the learning algorithm
		String percentValidation = args[1]; // percent of the training set that should be used for validation
		String L = args[2]; // the maximal power of 2 for tree sizes T
		String trainingset_filename = args[3]; // path of the training set file
		String outputtree_filename = args[4]; // path of the output decision tree
		if (!version.equals("1") && !version.equals("2")) {
			System.err.println("invalid version of learning algorithm");
			System.exit(1);
		}

	}

}
