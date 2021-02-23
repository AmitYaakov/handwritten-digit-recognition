package Constraints;

import DecisionTree.BinaryNode;
import DecisionTree.DecisionTree;
import DecisionTree.NodeData;
import miniProject.tree_Utilities;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;

public class Task implements Runnable {
	
    BinaryNode<NodeData> leaf;
    Constraint con;
    DecisionTree tree;
    
    public Task(BinaryNode<NodeData> _leaf, Constraint _con, DecisionTree _tree){
        leaf = _leaf;
        con = _con;
        tree = _tree;
    }
    
    @Override
    public void run() {
        float N_L = leaf.getData().getImages_indexes().size();
        double IG = tree_Utilities.check(con, leaf);
        double x = N_L * IG;
        leaf.getData().updateMaxIG(x,con);
        AtomicInteger i = tree.getTasksCompleted();
        i.getAndIncrement();
    }
}
