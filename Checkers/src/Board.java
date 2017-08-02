import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board
{
    HashMap<Point, Piece> blackPieces = new HashMap<>();
    HashMap<Point, Piece> whitePieces = new HashMap<>();     
    public PieceColor player;
    public boolean jumped = false;
	private int whiteKingsCount = 0;
	private int blackKingsCount = 0;
	private int blackCount = 12;
	private int whiteCount = 12;	
	
    public Board()
    {    	                

        // Set the position of the pieces.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if (row == 1) {
                    if (col % 2 == 0) {
                        addPieceToPieces(blackPieces, row, col);
                    } else {
                        addPieceToPieces(whitePieces, 7 - row, col);
                    }
                } else {
                    if (col % 2 == 0) {
                        addPieceToPieces(whitePieces, 7 - row, col);
                    } else {
                        addPieceToPieces(blackPieces, row, col);
                    }
                }
            }
        }                
    }

    public Board(HashMap<Point, Piece> blackPieces, HashMap<Point, Piece> whitePieces)
    {
        this.blackPieces = blackPieces;
        this.whitePieces = whitePieces;        
    }

    public Board(HashMap<Point, Piece> blackPieces, HashMap<Point, Piece> whitePieces, int whiteCount, int blackCount, 
    		int whiteKingsCount, int blackKingsCount){
    	
    	this.blackPieces = blackPieces;
    	this.whitePieces = whitePieces;
    	this.whiteCount = whiteCount;
    	this.blackCount = blackCount;
    	this.whiteKingsCount = whiteKingsCount;
    	this.blackKingsCount = blackKingsCount;
    }
    
    public Piece[] getBlackPieces()
    {
        return blackPieces.values().toArray(new Piece[0]);
    }

    public Piece[] getWhitePieces()
    {
        return whitePieces.values().toArray(new Piece[0]);
    }

    public boolean isJumped(){
    	return jumped;
    }
    
    public void setPlayer(PieceColor color){ 
    	player = color; 
    }
    
    public boolean hasPieceAt(Point p){
        return (blackPieces.containsKey(p)) || (whitePieces.containsKey(p)) && (p.x < 8 && p.y < 8);
    }

    public Piece getPieceAt(Point p)
    {
        if (blackPieces.containsKey(p)) {
            return blackPieces.get(p);
        } else if (whitePieces.containsKey(p)) {
            return whitePieces.get(p);
        }

        return null;
    }

    public void setKingPieceAt(Point p)
    {
        if (blackPieces.containsKey(p)) {
            blackPieces.get(p).setKing();
        } else if (whitePieces.containsKey(p)) {
            whitePieces.get(p).setKing();
        }
    }

    public void removePieceAt(Point p)
    {
        if (blackPieces.containsKey(p)) {
            blackPieces.remove(p);
        } else if (whitePieces.containsKey(p)) {
            whitePieces.remove(p);
        } else {
            throw new NullPointerException("Piece does not exist at (" + p.x + ", " + p.y + ".");
        }
    }

    public boolean movePiece(Move move)
    {  	
    	Point currPoint = new Point(move.currCol, move.currRow);
    	Point newPoint = new Point(move.movCol, move.movRow);    	    	
    	boolean crowned = false;  	    	    	
    	
    	int kingsEdge = 0;
    	if(player == AI.getColor())
    		kingsEdge = 7;
    	else{
    		kingsEdge = 0;
    	}
    	    	
        if (blackPieces.containsKey(currPoint)){
        	
        	Piece currPiece = blackPieces.get(currPoint);            
        	if(currPiece.getColor() == player &&  move.movRow == kingsEdge){        		
        		currPiece.setKing();
        		currPiece.setColor(PieceColor.BLACK_KING);        		
                blackKingsCount++;
                crowned = true;
        	}       	
        	
        	currPiece.setPoint(newPoint);
        	blackPieces.remove(currPoint);
            blackPieces.put(newPoint, currPiece);
            
            return crowned;        	
            
        } else if (whitePieces.containsKey(currPoint)) {
        	Piece currPiece = whitePieces.get(currPoint);        	
        	if(currPiece.getColor() == player && move.movRow == kingsEdge){
        		currPiece.setKing();
        		currPiece.setColor(PieceColor.WHITE_KING);
        		whiteKingsCount++;
        		crowned = true;
        	}                        
        	
        	currPiece.setPoint(newPoint);
    		whitePieces.remove(currPoint);
            whitePieces.put(newPoint, currPiece);            
            return crowned;
            
        }
        
        return crowned;/* else {
            throw new NullPointerException("Piece does not exist at (" + move.currCol + ", " + move.currRow + ".");
        }*/
        
        
    }

    public int getBoardValue(PieceColor color)
    {
        HashMap<Point, Piece> currentPieces;
        if (color == PieceColor.BLACK) currentPieces = blackPieces;
        else currentPieces = whitePieces;

        int totalValue = 0;

        // The value of each piece is determined by its distance from the center of the board with
        // the center having a value of 1.
        for (Map.Entry<Point, Piece> pieceEntry : currentPieces.entrySet()) {
            // We get the distance of each coordinate to the nearest coordinate edge of a piece to help compute
            // the value of each piece. The coordinate edge is where the board ends in a given coordinate. For
            // example, the x-coordinate edge starts from the leftmost cell to the rightmost cell (disregarding
            // the y-position). On the other hand, the y-coordinate edge starts from the uppermost cell to the
            // bottommost cell (disregarding the x-position).
            Point p = pieceEntry.getKey();
            int xDistanceFromEdge = (p.x > 3) ? (4 - (p.x - 3)) : p.x;
            int yDistanceFromEdge = (p.y > 3) ? (4 - (p.y - 3)) : p.y;
            int multiplier = ((pieceEntry.getValue()).isKing()) ? 2 : 1;

            totalValue += (4 - Math.min(xDistanceFromEdge, yDistanceFromEdge)) * multiplier;
        }

        return totalValue;
    }

    @Override public Board clone()
    {
        return new Board(deepCopyHashMap(blackPieces), deepCopyHashMap(whitePieces));
    }

    private HashMap<Point, Piece> deepCopyHashMap(HashMap<Point, Piece> source)
    {
        HashMap<Point, Piece> target = new HashMap<>();
        for (Map.Entry<Point, Piece> pieceEntry : source.entrySet()) {
            target.put(pieceEntry.getKey().clone(), pieceEntry.getValue().clone());
        }

        return target;
    }

    private void addPieceToPieces(HashMap<Point, Piece> pieces, int row, int col)
    {
        Point p = new Point(col, row);
        if (pieces == blackPieces) pieces.put(p, new Piece(PieceColor.BLACK, p));
        else if (pieces == whitePieces) pieces.put(p, new Piece(PieceColor.WHITE, p));
    }
        
    public ArrayList<Move> getPossibleMovesForPlayer(int row, int col){
    	return getPossibleMovesForColorAt(player, row, col);
    }
    
    public ArrayList<Move> getAllPossibleMovesForColor(PieceColor color){
    	ArrayList<Move> moves = new ArrayList<Move>();
    	int count = 0;
    	
    	for(int row = 0; row < 8; row++){
    		for(int col = 0; col < 8; col++){
    			
    			if(getPieceAt(new Point(col, row)) != null){
    				PieceColor currPieceColor = getPieceAt(new Point(col, row)).getColor();    			
        			
        			// Normal pieces    				
        			if(currPieceColor == color){
        				moves.addAll(getPossibleMovesForColorAt(color, row, col));        				
        				count++;
        			}    			
        			// King pieces
        			if(color == PieceColor.WHITE && currPieceColor == PieceColor.WHITE_KING){
        				moves.addAll(getPossibleMovesForColorAt(color, row, col));
        				count++;
        				
        			}else if(color == PieceColor.BLACK && currPieceColor == PieceColor.BLACK_KING){
        				moves.addAll(getPossibleMovesForColorAt(color, row, col));
        				count++;
        			}
        			
        			if(count == 12)
        				return moves;
    			}    			
    		}
    	}    	
    	return moves;
    }
    
    public ArrayList<Move> getPossibleMovesForColorAt(PieceColor color, int row, int col){
    	    	
    	if(getPieceAt(new Point(col, row)) == null)
    		return null;    	
    	    	
    	PieceColor currPiece = getPieceAt(new Point(col, row)).getColor();
    	ArrayList<Move> moves = new ArrayList<Move>();
    	
    	int rowDirection = 0;    	
    	if(color == AI.getColor()){    	    		
    		rowDirection = 1;
    	} else {    		
    		rowDirection = -1;
    	}

    	if(color == PieceColor.WHITE){
    		if(currPiece == PieceColor.WHITE || currPiece == PieceColor.WHITE_KING){
    			
    			
    			if(isPointInBoard(new Point(col + 1, row + rowDirection)) && 
    					getPieceAt(new Point(col + 1, row + rowDirection)) == null){       					
    				moves.add(new Move(row, col, row + rowDirection, col + 1));     				
    			}    			
    			
    			if(isPointInBoard(new Point(col - 1, row + rowDirection)) && 
    					getPieceAt(new Point(col - 1, row + rowDirection)) == null){    				
    				moves.add(new Move(row, col, row + rowDirection, col - 1));
    			}    			    	
    		}
    		
    		if(currPiece == PieceColor.WHITE_KING){    			
    			if(isPointInBoard(new Point(col + 1, row - rowDirection)) &&
    					getPieceAt(new Point(col + 1, row - rowDirection)) == null){    				
    				moves.add(new Move(row, col, row - rowDirection, col + 1));    			
    			}
    			if(isPointInBoard(new Point(col - 1, row - rowDirection)) &&
    					getPieceAt(new Point(col - 1, row - rowDirection)) == null){    				
    				moves.add(new Move(row, col, row - rowDirection, col - 1));
    			}    			
    				
    		}
    	}else if(color == PieceColor.BLACK){
    		
    		if(currPiece == PieceColor.BLACK || currPiece == PieceColor.BLACK_KING){
    			if(isPointInBoard(new Point(col + 1, row + rowDirection)) &&
    					getPieceAt(new Point(col + 1, row + rowDirection)) == null){    				
    				moves.add(new Move(row, col, row + rowDirection, col + 1));
    			}    			
    			if(isPointInBoard(new Point(col - 1, row + rowDirection)) &&
    					getPieceAt(new Point(col - 1, row + rowDirection)) == null){    				
    				moves.add(new Move(row, col, row + rowDirection, col - 1));
    			}    			    	
    		}
    		
    		if(currPiece == PieceColor.BLACK_KING){    			
    			if(isPointInBoard(new Point(col + 1, row - rowDirection)) && 
    					getPieceAt(new Point(col + 1, row - rowDirection)) == null){    				
    				moves.add(new Move(row, col, row - rowDirection, col + 1));
    			}
    			if(isPointInBoard(new Point(col - 1, row - rowDirection)) && 
    					getPieceAt(new Point(col - 1, row - rowDirection)) == null){    				
    				moves.add(new Move(row, col, row - rowDirection, col - 1));
    			}
    				
    		}
    	}
    	
    	ArrayList<Move> jumps = getJumps(row, col);
    	moves.addAll(jumps);
    	
    	return moves;    	
    }
    
    private boolean isPointInBoard(Point pos)
    {
        return (pos.x >= 0 && pos.x < 8) && (pos.y >= 0 && pos.y < 8);
    }    

	public ArrayList<Move> getJumps(int row, int col){    	    	
    	ArrayList<Move> jumps = new ArrayList<Move>();    	    	
    	PieceColor currPiece = getPieceAt(new Point(col, row)).getColor();    	
    	
    	// Get white jumps
    	if(player == PieceColor.WHITE){
    		if(currPiece == PieceColor.WHITE || currPiece == PieceColor.WHITE_KING){
        		if(getPieceAt(new Point(col + 1, row + 1)) != null && (col + 1) != 7 && (row + 1) != 7 && 
        		(getPieceAt(new Point(col + 1, row + 1)).getColor() == PieceColor.BLACK ||
        		   getPieceAt(new Point(col + 1, row + 1)).getColor() == PieceColor.BLACK_KING)){
        			if(getPieceAt(new Point(col + 2, row + 2)) == /*PieceColor.EMPTY*/ null){
        				jumps.add(new Move(row, col, row + 2, col + 2));
        			}
        		}
        		
        		if(getPieceAt(new Point(col - 1, row + 1)) != null && (col - 1) != 0 && (row + 1) != 7 &&
        				(getPieceAt(new Point(col - 1, row + 1)).getColor() == PieceColor.BLACK ||
        		   getPieceAt(new Point(col - 1, row + 1)).getColor() == PieceColor.BLACK_KING)){
        	    	if(getPieceAt(new Point(col - 2, row + 2)) == /*PieceColor.EMPTY*/ null){
        	    		jumps.add(new Move(row, col, row + 2, col - 2));
        	    	}
        	    }
        		
        		if(getPieceAt(new Point(col + 1, row - 1)) != null && (col + 1) != 7 && (row - 1) != 0 &&
        				(getPieceAt(new Point(col + 1, row - 1)).getColor() == PieceColor.BLACK ||
        	       getPieceAt(new Point(col + 1, row - 1)).getColor() == PieceColor.BLACK_KING)){
        	    	if(getPieceAt(new Point(col + 2, row - 2)) == /*PieceColor.EMPTY*/ null){
        	    		jumps.add(new Move(row, col, row - 2, col + 2));
        	    	}
        	    }
        	    		
        	    if(getPieceAt(new Point(col - 1, row - 1)) != null && (col - 1) != 0 &&  (row - 1) != 0 &&
        	    		(getPieceAt(new Point(col - 1, row - 1)).getColor() == PieceColor.BLACK ||
        	       getPieceAt(new Point(col - 1, row - 1)).getColor() == PieceColor.BLACK_KING)){
        	    	 if(getPieceAt(new Point(col - 2, row - 2)) == /*PieceColor.EMPTY*/ null){
        	    	    jumps.add(new Move(row, col, row - 2, col - 2));
        	    	  }
        	    }
        	}
    		
    	}else{
    		
    		if(currPiece == PieceColor.BLACK || currPiece == PieceColor.BLACK_KING){
        		if(getPieceAt(new Point(col + 1, row + 1)) != null && (col + 1) != 7 && (row + 1) != 7 &&
        				(getPieceAt(new Point(col + 1, row + 1)).getColor() == PieceColor.WHITE ||
        		   getPieceAt(new Point(col + 1, row + 1)).getColor() == PieceColor.WHITE_KING)){
        			if(getPieceAt(new Point(col + 2, row + 2)) == /*PieceColor.EMPTY*/ null){
        				jumps.add(new Move(row, col, row + 2, col + 2));
        			}
        		}
        		
        		if(getPieceAt(new Point(col - 1, row + 1)) != null && (col - 1) != 0 && (row + 1) != 7 &&
        				(getPieceAt(new Point(col - 1, row + 1)).getColor() == PieceColor.WHITE ||
        		   getPieceAt(new Point(col - 1, row + 1)).getColor() == PieceColor.WHITE_KING)){
        	    	if(getPieceAt(new Point(col - 2, row + 2)) == /*PieceColor.EMPTY*/ null){
        	    		jumps.add(new Move(row, col, row + 2, col - 2));
        	    	}
        	    }
        		 
        		if(getPieceAt(new Point(col + 1, row - 1)) != null && (col + 1) != 7 && (row - 1) != 0 &&
        				(getPieceAt(new Point(col + 1, row - 1)).getColor() == PieceColor.WHITE ||
        	       getPieceAt(new Point(col + 1, row - 1)).getColor() == PieceColor.WHITE_KING)){
        	    	if(getPieceAt(new Point(col + 2, row - 2)) == /*PieceColor.EMPTY*/ null){
        	    		jumps.add(new Move(row, col, row - 2, col + 2));
        	    	}
        	    }
        	    		
        	    if(getPieceAt(new Point(col - 1, row - 1)) != null && (col - 1) != 0 && (row - 1) != 0 &&
        	    		(getPieceAt(new Point(col - 1, row - 1)).getColor() == PieceColor.WHITE ||
        	       getPieceAt(new Point(col - 1, row - 1)).getColor() == PieceColor.WHITE_KING)){
        	    	 if(getPieceAt(new Point(col - 2, row - 2)) == /*PieceColor.EMPTY*/ null){
        	    	    jumps.add(new Move(row, col, row - 2, col - 2));
        	    	  }
        	    }
        	}
    	}
    	return jumps;
    }
	
	public void handleJump(Move move){		
		Pair<Integer, Integer> squareSkipped = move.getSquareSkipped();
		
		if(squareSkipped.getFirst() != move.currRow && squareSkipped.getFirst() != move.movRow &&
				squareSkipped.getSecond() != move.currCol && squareSkipped.getSecond() != move.movCol){
					
			PieceColor pieceSkipped = getPieceAt(new Point(squareSkipped.getSecond(), squareSkipped.getFirst())).getColor(); 
			
			if(pieceSkipped == PieceColor.WHITE_KING){
				whiteKingsCount--;
			}else if(pieceSkipped == PieceColor.BLACK_KING){
				blackKingsCount--;
			}				
			
			removePieceAt(new Point(squareSkipped.getSecond(), squareSkipped.getFirst()));

			if(player != CheckerBoard.getPlayerColor())
				jumped = true;
			
			if(player == PieceColor.WHITE){
				blackCount--;
				
			}else{
				whiteCount--;
				
			}
			
		}else{
			jumped = false;
		}
	}
	
	public void updateBoard(Move lastMove){
		
	}
	
	public int getWhiteScore(){
		return getBoardValue(PieceColor.WHITE);
	}
	
	public int getBlackScore(){
		return getBoardValue(PieceColor.BLACK);
	}
	
	public int getWhiteCount(){
		return whiteCount;
	}
	
	public int getBlackCount(){
		return blackCount;
	}
	
	public int getWhiteKingsCount(){
		return whiteKingsCount;
	}
	
	public int getBlackKingsCount(){
		return blackKingsCount;
	}
	
	
	
}
