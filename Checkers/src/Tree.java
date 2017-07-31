import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree{
	
	private Board node;
	private Move move;
	private int score;
	private ArrayList<Tree> children;
	
	public Tree(Board node, Move move, int score, Tree ... children){
		this.node = node;
		this.move = move;
		this.score = score;
		this.children = new ArrayList<>(Arrays.asList(children));
	}
	
	public Board getBoard(){ return node; }	
	public Move getMove(){	return move; }
	public int getScore(){ return score;}
	public List<Tree> getChildren(){ return children;}	
	public int getNumberOfChildren(){ return children.size();}
	
	public void updateScore(int newScore){
		score = newScore;
	}
	
	public Tree getChild(int index){
		if(index < children.size())		
			return children.get(index);		
		return null;
	}
	
	public void addChild(Tree child){
		children.add(child);
	}
		
	
}