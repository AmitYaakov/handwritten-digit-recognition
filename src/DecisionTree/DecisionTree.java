package DecisionTree;
import Constraints.Constraint;
import Constraints.Task;
import miniProject.Example;
import miniProject.tree_Utilities;

import java.util.*;

import  java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class DecisionTree extends BinaryTree<NodeData> implements Serializable {

    protected ArrayList<Example> trainingSet;
    protected ArrayList<Constraint> constrains;
    protected ArrayList <BinaryNode<NodeData>> leafs;
	Set<Integer> impartial_traning_set ; //indexes of examples in the trainning set.
	private ExecutorService jobsPool;
	int numOfThreads=100;
	double max_ig = -1000;
	BinaryNode<NodeData> max_leaf = null;
	Constraint max_con = null;
	AtomicInteger tasksCompleted= new AtomicInteger(0);
	int numOfTotalTasks=0;


	public synchronized AtomicInteger getTasksCompleted() {
		return tasksCompleted;
	}

	public void shutDown(){
		jobsPool.shutdown();
	}
	public DecisionTree(ArrayList<Example> _trainingSet, ArrayList<Constraint> cons, Set<Integer> _impartial_traning_set){
		root=null;
        trainingSet = _trainingSet;
        constrains = new ArrayList<Constraint>();
        for (Constraint con : cons ) {
        	constrains.add(con);
        }
        leafs = new ArrayList<BinaryNode<NodeData>>();
        impartial_traning_set = _impartial_traning_set;
		jobsPool = Executors.newFixedThreadPool(numOfThreads);
    }
	
	public DecisionTree(){
        trainingSet = null;
        constrains = null;
        leafs = null;
        impartial_traning_set = null;
    }

	public void BuildTree(int T) {
		if(root == null) {
			int max_label = calc_max_label(impartial_traning_set);
			NodeData _nodeData = new NodeData(max_label, trainingSet, impartial_traning_set);
			root = new BinaryNode<NodeData>(_nodeData);
			root.getData().CalculateEntropy();
		}
    	while(T > 0) {
			
        	BinaryNode<NodeData> max_leaf = getLeafToReplaceAsync();
        	NodeData max_leaf_data = max_leaf.getData();
        	Constraint best_con = max_leaf_data.getConstraint();
        	tree_Utilities.apply_new_node(best_con, max_leaf);
        	NodeData right_data = max_leaf.right.getData(); 
        	right_data.CalculateEntropy();
        	NodeData left_data = max_leaf.left.getData(); 
        	left_data.CalculateEntropy();
        	int right_num_of_example = calc_max_label(right_data.getImages_indexes());
        	int left_num_of_example = calc_max_label(left_data.getImages_indexes());
        	if (right_data.getImages_indexes().size() == 0) {
        		right_num_of_example = calc_max_label(max_leaf_data.getImages_indexes());
        	}
        	right_data.setLabel(right_num_of_example);
        	right_data.setConstraint(null);
        	if (left_data.getImages_indexes().size()  == 0) {
        		left_num_of_example = calc_max_label(max_leaf_data.getImages_indexes());
        	}
        	left_data.setLabel(left_num_of_example);
        	left_data.setConstraint(null);
			T--;
        }
	}
	
	/**
	 *run every example on the tree and predicts its label. 
	 * @param validation - Set of the examples indexes to check.
	 * @return hashMap of <example,label>
	 */
	public HashMap<Example,Integer> predict(Set<Integer> validation){
		HashMap<Example,Integer>results= new HashMap<Example,Integer>();
		for(Integer index : validation){
			Example example = trainingSet.get(index);
			Integer tag= predictExample(example);
			results.put(example,tag);
		}
		return results;
	}
	
	public void predictAndPrint(ArrayList<Example> test_set){
		for(Example example : test_set){
			Integer tag = predictExample(example);
			System.out.println(tag);
		}
	}
	
	/**
	 * run an example on the tree and predicts its label. 
	 * @param example
	 * @return integer- the example label according to the tree prediction.
	 */
	public Integer predictExample(Example example) {
		BinaryNode<NodeData> _root = root;
		while(!_root.isLeaf()){
			if(_root.data.getConstraint().checkConstraint(example)) {
				_root = _root.right;
			} else {
				_root = _root.left;
			}
		}
		return _root.data.getLabel();	
	}	


	public BinaryNode<NodeData> getLeafToReplaceAsync() {
		max_leaf = null;
		max_con = null;
		tasksCompleted = new AtomicInteger(0);
		max_ig = Integer.MIN_VALUE;
		ArrayList <BinaryNode<NodeData>> leafs = getLastIterationLeafs();	
		numOfTotalTasks = leafs.size() * constrains.size();
		
		for(BinaryNode<NodeData> leaf: leafs ){
			for(Constraint con : constrains){
				Task task = new Task(leaf,con,this);
				jobsPool.execute(task);
			}
		}
		
		while(tasksCompleted.get() != numOfTotalTasks){};
		max_leaf = find_leaf_with_max_ig();
		if(max_leaf != null){
			max_con = max_leaf.getData().getConstraint();
			update_leaf_con(max_leaf , max_con);
		}
		return max_leaf;
	}
	
	public BinaryNode<NodeData> find_leaf_with_max_ig() {
		BinaryNode<NodeData> to_return = find_leaf_with_max_ig2();
		return to_return;
	}
	
	public BinaryNode<NodeData> find_leaf_with_max_ig2() {
		BinaryNode<NodeData> to_return = null;
		ArrayList <BinaryNode<NodeData>> tree_leafs = getTreeLeafs();
		Double max_ig = -Double.MAX_VALUE;
		for (BinaryNode<NodeData> leaf : tree_leafs) {
			if (max_ig < leaf.getData().getIG()) {
				max_ig = leaf.getData().getIG();
				to_return = leaf;
			}  
		}
		return to_return;
	}
	public ArrayList <BinaryNode<NodeData>> getLastIterationLeafs () {
		ArrayList <BinaryNode<NodeData>> to_return = new ArrayList <BinaryNode<NodeData>>();
		getLastIterationLeafs2(to_return,root);
		return to_return;
	}
	
	public void getLastIterationLeafs2(ArrayList <BinaryNode<NodeData>> leafs,BinaryNode<NodeData> node) {
		if (node.isLeaf() && node.getData().getIG() == null){  
		       leafs.add(node);
		    }  
	    if (node.right != null)  
	    	getLastIterationLeafs2(leafs,node.right);  
	    if (node.left != null)  
	        getLastIterationLeafs2(leafs,node.left);;  
	        
	}
	

	public void update_leaf_con(BinaryNode<NodeData> leaf, Constraint con) {
		leaf.getData().setConstraint(con);
		leaf.getData().setLabel(-1);

	}
	
	public int calc_max_label(Set<Integer> indexes) {
		int max = -1;
		int index = 0;
		int [] labelCount = new int[10];
		for (Integer indec : indexes){
			labelCount[trainingSet.get(indec).getLabel()]++;
		}
		for (int i = 0; i < labelCount.length; i++) {
			if (max < labelCount[i]) {
				max = labelCount[i];
				index = i;
			}
		}
		return index;
	}
	
	
	public String toString() {
		return root.toString();  
	}
}
