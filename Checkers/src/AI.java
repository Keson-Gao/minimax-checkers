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
		gameTree = generateTree(board);
		lastMove = pickMove();
		return lastMove;
	}
	
	public static PieceColor getColor(){
		return color;
	}
	
	private Tree generateTree(Board board){		
		
		Tree root = new Tree(board, null, score(board));
		ArrayList<Move> moves;
				
		if(board.isJumped()){			
			moves = board.getJumps(lastMove.movRow, lastMove.movCol);			
		}else{
			moves = board.getAllPossibleMovesForColor(color);
		}
		
		for(Move move: moves){
			Board temp = copyBoard(board);
			temp.movePiece(move);
			temp.handleJump(move);
									
			Tree firstLayer = new Tree(temp, move, score(temp));			
			ArrayList<Move> secondMoves = temp.getAllPossibleMovesForColor(oppColor);
			
			for(Move sMove: secondMoves){
				Board temp2 = copyBoard(temp);
				temp2.movePiece(sMove);
				temp2.handleJump(sMove);								
				
				Tree secondLayer = new Tree(temp2, sMove, score(temp2));				
				ArrayList<Move> thirdMoves = temp2.getAllPossibleMovesForColor(color);
				
				for(Move tMove: thirdMoves){
					Board temp3 =  copyBoard(temp2);
					temp3.movePiece(tMove);
					temp3.handleJump(tMove);
										
					Tree thirdLayer = new Tree(temp3, tMove, score(temp3));				
					ArrayList<Move> fourthMoves = temp3.getAllPossibleMovesForColor(color);
					
					for(Move fMove: fourthMoves){
						Board temp4 =  copyBoard(temp3);
						temp4.movePiece(fMove);
						temp4.handleJump(fMove);
																					
						thirdLayer.addChild(new Tree(temp4, fMove, score(temp4)));
					}
					secondLayer.addChild(thirdLayer);
				}			
				
				firstLayer.addChild(secondLayer);			
			}		
			root.addChild(firstLayer);
		}
		
		return root;
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

	private Move pickMove(){				
		
		int max = -13;
		int index = 0;
						
		for(int i = 0; i < gameTree.getNumberOfChildren(); i++){
			Tree child = gameTree.getChild(i);																
			int sMin = 13;
			
			for(Tree sChild: child.getChildren()){
				int tMax = -13;				
										
				for(Tree tChild: sChild.getChildren()){																			
					if(tChild.getScore() >= tMax){							
						tMax = tChild.getScore();
					}					
				}				
				sChild.updateScore(tMax);							
				if(sChild.getScore() <= sMin){
					sMin = sChild.getScore();
				}
			}				
			child.updateScore(sMin);
			
			if(child.getScore() >= max){								
				max = child.getScore();
				index = i;
			}						
		}

		return gameTree.getChild(index).getMove(); 
	}
	
	private int score(Board board){
		if(color == PieceColor.WHITE){
			return board.getWhiteScore() - board.getBlackScore();			
		} else {
			return board.getBlackScore() - board.getWhiteScore();
		}
		
	}	
}