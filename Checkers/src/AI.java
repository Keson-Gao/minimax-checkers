import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AI{
 
	final int MAX_DEPTH = 4;
	final int MAX = -13;
	final int MIN = 13;
	private static PieceColor color;
	private PieceColor oppColor;
	private Tree gameTree;
	private Move lastMove;
	
	public AI(PieceColor color){
		AI.color = color;		
		
		if(color == PieceColor.WHITE)
			oppColor = PieceColor.BLACK;
		else
			oppColor = PieceColor.WHITE;
	}
	
	public Move getAIMove(Board board){						
		Tree root = new Tree(board, null, score(board));
		ArrayList<Move> moves;
				
		if(board.isJumped()){			
			moves = board.getJumps(lastMove.movRow, lastMove.movCol);			
		}else{			
			moves = board.getAllPossibleMovesForColor(color);
		}
		
		gameTree = generateTree(board, root, moves, color, 5);				
		lastMove = gameTree.getChild(pickMove( gameTree, 0, new Tree(gameTree.getBoard(), null, score(gameTree.getBoard())), 
				new Tree(gameTree.getBoard(), null, Integer.MAX_VALUE), color, true).getSecond()).getMove();
		return lastMove;
	}
	
	public static PieceColor getColor(){
		return color;
	}
		
	
	private Tree generateTree(Board board, Tree tree, ArrayList<Move> moves, PieceColor player, int depth){
							
		if(depth == 0){
			return null;		
		}
								
		for(Move move: moves){						
			Board temp =  copyBoard(board);
			temp.movePiece(move);
			temp.handleJump(move);
											
			Tree newLayer = new Tree(temp, move, score(temp));
			ArrayList<Move> newMoves = temp.getAllPossibleMovesForColor((player == color)? oppColor: color);
			Tree childTree = generateTree(temp, newLayer, newMoves, (player == color)? oppColor: color, depth-1);			
			newLayer.addChild(childTree);
			tree.addChild(newLayer);	
		}
		
		return tree;
	}	
	
	private Board copyBoard(Board board) {		
		return new Board(deepCopyHashMap(board.blackPieces), deepCopyHashMap(board.whitePieces), board.getWhiteCount(), board.getBlackCount(),
				board.getWhiteKingsCount(), board.getBlackKingsCount());
	}

    private HashMap<Point, Piece> deepCopyHashMap(HashMap<Point, Piece> source)
    {
        HashMap<Point, Piece> target = new HashMap<>();
        for (Map.Entry<Point, Piece> pieceEntry : source.entrySet()) {        	
            target.put(pieceEntry.getKey(), pieceEntry.getValue());
        }
        
        return target;
    }
    
    private Pair<Integer, Integer> pickMove(Tree tree, int depth, Tree alpha, Tree beta, PieceColor currColor, boolean isMax){    	
    	if (depth == 5) {    		
            return null;
        }

        if (isMax) {            
            int max = Integer.MIN_VALUE;
            int index = -1;
            for (int i = 0; i < tree.getNumberOfChildren(); i++) {
            	Tree child = tree.getChild(i);            	
            	
            	Pair<Integer, Integer> score = pickMove(child, depth + 1, alpha, beta, getOppositeColor(currColor), false);
            	if(!(score == null)){                	
            		child.updateScore(score.getFirst());
            	}                    
                if(child.getScore() >= max){							
					max = child.getScore();
					index = i;
				}                
            }

            return new Pair<Integer, Integer>(max, index);
        } else {
        	
           int min = Integer.MAX_VALUE;
           int index = -1;
           for (int i = 0; i < tree.getNumberOfChildren(); i++) {
           		Tree child = tree.getChild(i);
           		
           		Pair<Integer, Integer> score = pickMove(child, depth + 1, alpha, beta, getOppositeColor(currColor), true);           		
           		if(!(score == null)){                	
           			child.updateScore(score.getFirst());
           		}                                
                if(child.getScore() <= min){							
					min = child.getScore();
					index = i;  
				}                                
            }

            return new Pair<Integer, Integer>(min, index);
        }
    }
	
	private PieceColor getOppositeColor(PieceColor currColor) {
		if(currColor == color)
			return oppColor;		
		return color;
	}

	private int score(Board board){
		if(color == PieceColor.WHITE){			
			return board.getWhiteScore() - board.getBlackScore();			
		} else {			
			return board.getBlackScore() - board.getWhiteScore();
		}
		
	}	
}
