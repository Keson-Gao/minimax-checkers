
public class Move{	
	public int currRow, currCol, movRow, movCol;
	
	public Move(int currRow, int currCol, int movRow, int movCol){
		this.currRow = currRow;
		this.currCol = currCol;
		this.movRow = movRow;
		this.movCol = movCol;
	}
	
	public Pair<Integer, Integer> getSquareSkipped(){		
		return new Pair<Integer, Integer>((currRow + movRow)/2, (currCol + movCol)/2);
	}
	
	public String toString(){
		return "current = (" + currRow + ", " + currCol + ") move = (" + movRow + ", " + movCol + ")";
	}
}