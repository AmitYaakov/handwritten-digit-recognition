package miniProject;
import Constraints.*;
import DecisionTree.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class Model {
	
	 private int version;
	 private int P;
	 private int L;
	 private int rows = 28;
	 private int cols = 28;
	 private int max_pixel = 255;
	 private double maxRate = -1;
	 private int bestT = -1;
	 private int t = 1;
	 private int numOfTotalTasks = 0;
	 private AtomicInteger numOfFinishedTasks;
	 private String training_set_file_name;
	 private String output_tree_file_name;
	 ArrayList<Example> training_set; // validation_set + Impartialtraining_set.
	 Set<Integer> validation_set;
	 Set<Integer> impartial_training_set;
	 ArrayList<Constraint> constraints;


	public Model(int _version, int _p, int _l, String training_set, String output) {
		 version =_version;
		 P = _p;
		 L = _l;
		 training_set_file_name = training_set;
		 output_tree_file_name = output;
		 numOfFinishedTasks = new AtomicInteger(0);
	 }
	 

	 public void Init() {
		 training_set = tree_Utilities.generateSetFromCsv(this.training_set_file_name);
		 validation_set = generateValidationSet();
		 constraints = generateConstraints(this.version);
		 impartial_training_set = generateImpartialTrainingSet();
	 }

	 /**
	  * creates the optimal tree.
	  * @return optimal tree.
	  */
	 public DecisionTree runLearningAlgorithm() {
		 findOptimalTValue();
		 while (numOfTotalTasks != numOfFinishedTasks.get());
		     impartial_training_set.clear();
			 for(int i=0;i<training_set.size();i++){
				 impartial_training_set.add(i);
			 }
		 DecisionTree tree = new DecisionTree(training_set,constraints,impartial_training_set);
		 tree.BuildTree(bestT);
		 validation_set.clear();
		 for(int i=0;i<training_set.size();i++){
			 validation_set.add(i);
		 }
		 HashMap<Example,Integer> results = tree.predict(validation_set);
		 double success_rate = calculateSuccessRate(results);
		 printLeranTreeData(success_rate);
		 return tree; 
	 }
	 
	 public void printLeranTreeData (double success) {
		 int num_of_errors = 100 - (int) Math.ceil(success);
		 System.out.println("num: " + training_set.size());
		 System.out.println("error: " + num_of_errors);
		 System.out.println("size: " + this.bestT);
	 }
	 public void treeToFile(DecisionTree tree,int version) {
		 try (PrintWriter out = new PrintWriter (output_tree_file_name)) {
		 		out.print(version+",");
	            out.print(tree.toString());
	        } catch (Exception e) {
	            System.out.println("Error treeToFile");
	            System.exit(0);
	        }
	 }
	 
	/**
	 * add indexes of examples to validation set.
	 * @return
	 */
	public Set<Integer> generateValidationSet(){
		Set<Integer> validation_test = new HashSet<>();
		double size = (int) Math.ceil(((double) P * training_set.size()) / 100);
		Random rand = new Random();
		while (validation_test.size() != size){
			int index = rand.nextInt(training_set.size());
			validation_test.add(index);
		}
		 return validation_test; 
	 }

	/**
	 * Impartial set = trainning set/validation set (subtraction between groups)
	 * @return
	 */	 
	 public Set<Integer> generateImpartialTrainingSet () {
		 int [] impartial = new int [training_set.size()];
		 for (Integer index : validation_set){
		 	impartial[index] = 1;
		 }
		 Set<Integer> to_ret = new HashSet<>();
		 for(int i = 0; i < impartial.length; i++){
		 	if (impartial[i] == 0) {
		 		to_ret.add(i);
		 	}
		 }
		 return to_ret;
	 }



	public void findOptimalTValue() {
		int bound = (int) Math.pow(2, L);
		DecisionTree tree = new DecisionTree(training_set, constraints, impartial_training_set);
		int last_t = 0;
		while(t <= bound) {
					int current_t = t - last_t;
					tree.BuildTree(current_t);
					HashMap<Example,Integer> results = tree.predict(validation_set);
					double success_rate = calculateSuccessRate(results);
					checkIfNeedToUpdateT(success_rate, t);
					last_t = t;
			if (t == 1) {
				t = 2;
			}
			else {
				t = 2 * t;
			}
		}
		tree.shutDown();
	}
	

	 
	 public synchronized void checkIfNeedToUpdateT(double success_rate, int t){
		 if (success_rate > maxRate) {
			maxRate = success_rate;
			bestT = t; 
		} 
	 }
	 public double calculateSuccessRate(HashMap<Example, Integer> results) { //DONE
		 double size=  results.size();
		 int right_answers = 0;
		 for(Map.Entry<Example, Integer> entry: results.entrySet()) {
			 int tree_prediction = entry.getValue();
			 int real_label = entry.getKey().getLabel();
			 if (real_label == tree_prediction) {
				 right_answers++;
			 }
		 }
		 double success_rate = (right_answers/size)*100;
		 return success_rate;
	 }

	 public ArrayList<Constraint> generateConstraintsVersion1 () {
		 ArrayList<Constraint> constraints = new ArrayList<Constraint>();
		 for (int i = 0; i < rows ; i++) {
				for (int j = 0; j < cols ; j++) {
					Constraint con = new Type1Constraint(i, j, 128);
					constraints.add(con);

				}
		}
		return constraints;
	 }
	 
	 public ArrayList<Constraint> generateConstraintsVersion2 () {
	 	ArrayList<Constraint> constraints = new ArrayList<Constraint>();
		 for (int i = 0; i < rows ; i++) {
			 for (int j = 0; j < cols ; j++) {
				 Constraint con = new Type1Constraint(i, j, 128);
				 constraints.add(con);
			 }
		 }
		for (int diag = -20; diag <= 20 ; diag++) {
				Constraint con = new Type3Constraint(diag, 128);
				constraints.add(con);
		}

		 int i=2;
			 for (int x1=0; x1<=27-i; x1=x1+2) {
				 for (int y2=0; y2 <= 27-i;y2=y2+2) {
					 for (int val=200; val <= 500; val = val+100) {
						 int x2 = x1+i;
						 int y1 = y2+i;
						 Constraint con = new Type5Constraint(x1,y1,x2,y2,val);
						 constraints.add(con);
					 }
				 }
		 }
		return constraints; 
	 }

	/**
	 *
	 * @param version
	 * @return array list of constraints according to the version.
	 */
	public ArrayList<Constraint> generateConstraints(int version) {
		ArrayList<Constraint> constraints= new ArrayList<Constraint>();
		if (version == 1) {
			constraints =  generateConstraintsVersion1();	
		}
		else if (version == 2) {

			constraints = generateConstraintsVersion2();	
		}
		return constraints;
	 }
	public int getBestT() {
		return bestT;
	}
}
