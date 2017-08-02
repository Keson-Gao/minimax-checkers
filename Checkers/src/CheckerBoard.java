import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CheckerBoard extends JPanel{
	private static final long serialVersionUID = 1L;

	private JPanel board = new JPanel(new GridLayout(8, 8, 2, 2));
	private JPanel[] squarePanels = new JPanel[64];	
	private JLabel[] greenSquares = new JLabel[32];				
	
	private Icon whiteChip = new ImageIcon(getClass().getResource("white.png"));
	private Icon whiteKing = new ImageIcon(getClass().getResource("whiteKing.png"));	
	private Icon blackChip = new ImageIcon(getClass().getResource("black.png"));
	private Icon blackKing = new ImageIcon(getClass().getResource("blackKing.png"));	
	private Icon dottedWhite = new ImageIcon(getClass().getResource("eatpathwhite.png"));			
	private Icon humanTurn = new ImageIcon(getClass().getResource("eatpathwhite.png"));
	
	private static int whiteMoves = 0, blackMoves = 0; 			
	private static int currIndex;
	protected static boolean turn; 	
	private static boolean prevChip = false;	
	
	ArrayList<Integer> indexOfPathTaken = new ArrayList<Integer>();
	ArrayList<Integer> indexOfGreenSquare = new ArrayList<Integer>();
	ArrayList<Integer> indexOfClickedPath = new ArrayList<Integer>();
	ArrayList<Integer> leftEdge = new ArrayList<Integer>();
	ArrayList<Integer> rightEdge = new ArrayList<Integer>();
	ArrayList<Integer> upperEdge = new ArrayList<Integer>();
	ArrayList<Integer> bottomEdge = new ArrayList<Integer>();	
	ArrayList<Integer> foodSquares = new ArrayList<Integer>();
		
	int indices[] = {0, 2, 4, 6, 9, 11, 13, 15, 16, 18, 20, 22, 25, 27, 29, 31, 32, 34, 36, 38, 41, 	
			43, 45, 47, 48, 50, 52, 54, 57, 59, 61, 63};		
	
	int[] lEdge = {0, 16, 32, 48}, rEdge = {15, 31, 47, 63};		
	int[] upEdge = {0, 2, 4, 6}, botEdge = {57, 59, 61, 63};
	
	private static Icon humanPlayerPiece, aiPiece, humanPlayerPieceKing, aiPieceKing;	
	public static PieceColor playerColor;
	public static PieceColor aiColor;
	private PieceColor aiKing;
	
	ArrayList<Move> moves = new ArrayList<Move>();
	public static AI ai;
	Board gameBoard;
	private boolean tie;	
	
	public CheckerBoard(PieceColor piece){	
		initBoard();			
		setPlayers(piece);				
		placePiecesOnePlayer();		
		
		gameBoard = generateBoard();
		aiColor = (aiColor == PieceColor.WHITE)? PieceColor.WHITE: PieceColor.BLACK;
		
		if(playerColor == PieceColor.WHITE){
			gameBoard.setPlayer(playerColor);			
		}else{
			gameBoard.setPlayer(aiColor);
		}
		
		
		ai = new AI(aiColor);
		if(aiColor == PieceColor.WHITE){
			if(gameBoard.player == AI.getColor()){
				 new Thread(new Runnable() {
			         @Override
			         public void run() {
			        	 				         							        	 
			             // do the long-running work here				        	 
			        	 CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource("hourglass.png")));							        	 
			        	 Move aiMove = ai.getAIMove(gameBoard);																
						 boolean crownedAi = gameBoard.movePiece(aiMove);								
						 gameBoard.handleJump(aiMove);
						 handleJump(aiMove);
						 updateBoard(gameBoard, aiMove, crownedAi);										 										 																	
						
			             // at the end:
			             SwingUtilities.invokeLater(new Runnable() {
			                 @Override
			                 public void run() {		                	 
			                	 CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource((playerColor == PieceColor.BLACK)? "blackturn.png" : "whiteturn.png")));
			                 }
			             });
			          }
			     }).start();								 							
			}
		}
	}	
	
	private void initBoard(){				
						
		setOpaque(false);
		setLayout(null);
		setBounds(0, 0, 1000, 700);						
		board.setBackground(Color.BLACK);
		board.setBounds(160, 46, 470, 470);			
		MovementHandler handler = new MovementHandler();		
		int tog = 0;					
		
		for(int i = 0; i<64; i++){	
			squarePanels[i] = new JPanel();
			board.add(squarePanels[i]);
			
			//	Setting the board colors
			if((i >= 0 && i < 8) || (i >= 16 && i < 24) || (i >= 32 && i < 40) || (i >= 48 && i < 56))
				tog = 0;		
			if((i >= 8 && i < 16) || (i >= 24 && i < 32) || (i >= 40 && i < 48) || (i >= 56))
				tog = 1;									
			if(tog == 0){
				if(i%2 == 0)				
					squarePanels[i].setBackground(Color.GREEN);
			}						
			if(tog == 1){
				if(i%2 != 0) squarePanels[i].setBackground(Color.GREEN);
			}
			squarePanels[i].addMouseListener(handler);
		}		
			
		for(int i = 0; i < indices.length; i++){
			indexOfGreenSquare.add(indices[i]);
		}					
		for(int i = 0; i < rEdge.length; i++){
			rightEdge.add(rEdge[i]);
			leftEdge.add(lEdge[i]);
		}		
		for(int i = 0; i < upEdge.length; i++){
			upperEdge.add(upEdge[i]);
		}		
		for(int i = 0; i < botEdge.length; i++){
			bottomEdge.add(botEdge[i]);
		}		
		add(board);						
	}	
	
	public class MovementHandler extends MouseAdapter{	
		public void mouseClicked(MouseEvent e){
			Object source = e.getSource();								
			HumanVsAi(source);							
		}
	}
	
	private void HumanVsAi(Object source){				
		int index = 0;		
		boolean isGreenSquare = false;		
		int i;
		
		for(i = 0; i < 32; i++){			
			if(source == squarePanels[indexOfGreenSquare.get(i)]){				
				currIndex = i;
				index = indexOfGreenSquare.get(i);
				isGreenSquare = true;
				break;
			}								
		}					
		
		if(isGreenSquare){			
			if(isHumansTurn()){					
				if(aPieceWasPreviouslyChosen()){				
					if( cellIsEmpty(currIndex) || isPath(currIndex) ){						
						if(cellIsClickedTwice(currIndex)){								
							eraseDottedLines();								
														
							Pair<Integer, Integer> rowCol = getRowColAt(currIndex);
							int row = rowCol.getFirst(), col = rowCol.getSecond();									
							boolean crowned;
							
							if(moves == null)
								moves = getJumps(indexOfClickedPath);
							else
								moves.addAll(getJumps(indexOfClickedPath));							
							
							boolean isMultipleJumps = false;
							if(gameBoard.player != AI.getColor()){		
								
								ArrayList<Move> jumps = getJumps(indexOfClickedPath);								
								if(jumps.size() > 2) isMultipleJumps = true;	
								
								for(int j = 0; j < jumps.size()-1; j++){											
									gameBoard.handleJump(jumps.get(j));																																	
								}		
								
								for(Move move: moves){									
									if(move.movRow == row && move.movCol == col){										
										
										if(isMultipleJumps){
											Move leapMove = jumps.get(jumps.size()-1);											
											move = leapMove;											
										}																				
										
										crowned = gameBoard.movePiece(move);									
										handleJump(indexOfClickedPath);																																
										updateBoard(gameBoard, move, crowned);										
										break;								        								        									        
									}
								}								
							}
														
							if(gameBoard.player == AI.getColor()){
								 new Thread(new Runnable() {
							         @Override
							         public void run() {
							        	 				         							        	 
							             // do the long-running work here				        	 
							        	 CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource("hourglass.png")));							        	 
							        	 Move aiMove = ai.getAIMove(gameBoard);																
										 boolean crownedAi = gameBoard.movePiece(aiMove);								
										 gameBoard.handleJump(aiMove);
										 handleJump(aiMove);
										 updateBoard(gameBoard, aiMove, crownedAi);										 										 																	
										
							             // at the end:
							             SwingUtilities.invokeLater(new Runnable() {
							                 @Override
							                 public void run() {		                	 
							                	 CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource((playerColor == PieceColor.BLACK)? "blackturn.png" : "whiteturn.png")));
							                 }
							             });
							          }
							     }).start();								 							
							}
													
						}else{
							
							if( isAValidMoveAt(index) )								
								markAsPath(index);													
							else
								clearBoard();
						}										
					}else if( isNormalPiece(currIndex) || isHumanKing(currIndex) ){							

						Pair<Integer, Integer> rowCol = getRowColAt(currIndex);						
						if(rowCol.getFirst() >= 0 && rowCol.getFirst() < 8 && rowCol.getSecond() >= 0 && rowCol.getSecond() < 8){																					
							moves = gameBoard.getPossibleMovesForPlayer(rowCol.getFirst(), rowCol.getSecond());									
						}
						
						clearBoard();	
						storePath(currIndex);						
						setAPieceWasChosen(true);
						
						if(isHumanKing(currIndex))					
							newPieceClick(humanPlayerPieceKing, currIndex, index);						
						if(isNormalPiece(currIndex))
							newPieceClick(humanPlayerPiece, currIndex, index);
												
						indexOfClickedPath.add(index);											
					}else if(isAIPiece(currIndex)){							
						clearBoard();	
					}else{
						clearBoard();
					}
					
				}											
				else if( !cellIsEmpty(currIndex) && !aPieceWasPreviouslyChosen()){	
					if(isHumanKing(currIndex) || isNormalPiece(currIndex)){						
						
						Pair<Integer, Integer> rowCol = getRowColAt(currIndex);						
						if(rowCol.getFirst() >= 0 && rowCol.getFirst() < 8 && rowCol.getSecond() >= 0 && rowCol.getSecond() < 8){																										
							moves = gameBoard.getPossibleMovesForPlayer(rowCol.getFirst(), rowCol.getSecond());															
						}
						
						storePath(currIndex);
						if(isHumanKing(currIndex))													
							newPieceClick(humanPlayerPieceKing, currIndex, index);													
						if(isNormalPiece(currIndex))					
							newPieceClick(humanPlayerPiece, currIndex, index);																					
						indexOfClickedPath.add(index);	
												
						if(gameBoard.getAllPossibleMovesForColor(gameBoard.player).size() == 0){
							
							if(gameBoard.player == PieceColor.WHITE)
								gameBoard.player = PieceColor.BLACK;
							else
								gameBoard.player = PieceColor.WHITE;
							
							if(gameBoard.getAllPossibleMovesForColor(gameBoard.player).size() == 0){
								tie = true;							
							}else{
								if(gameBoard.player == PieceColor.BLACK){
									CheckerFrame.gameOver(new GameOver(), gameBoard.getWhiteCount(), 0, blackMoves, whiteMoves, false);									
								}else if(gameBoard.player == PieceColor.WHITE)
									CheckerFrame.gameOver(new GameOver(), 0 , gameBoard.getWhiteCount(), blackMoves, whiteMoves, false);
							}
						}							
					}					
				}	
				
			}
			
		}else clearBoard();		
	}
	

	private ArrayList<Move> getJumps(ArrayList<Integer> indexOfClickedPath2) 
	{		
		ArrayList<Move> jumps = new ArrayList<Move>();		
		for(int i = 0 ; i + 1 < indexOfClickedPath.size(); i++){						
			//int var = (indexOfClickedPath.get(i+1) - indexOfClickedPath.get(i));									
		
			Move newMove = new Move(getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(i))).getFirst(),
					getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(i))).getSecond(),
					getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(i+1))).getFirst(),
					getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(i+1))).getSecond()); 
			
			jumps.add(newMove);			
		}
		
		Move newMove = new Move(getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(0))).getFirst(),
				getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(0))).getSecond(),
				getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(indexOfClickedPath.size()-1))).getFirst(),
				getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(indexOfClickedPath.size()-1))).getSecond());
		jumps.add(newMove);			
		
		return jumps;
	}

	private Board generateBoard()
    {
        HashMap<Point, Piece> blackPieces = new HashMap<>();
        HashMap<Point, Piece> whitePieces = new HashMap<>();

        int squarePos = 0;
        int squarePosCtr = 0;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
            	
                Point newPoint = new Point(getPieceColumn(j, (i % 2) == 0), i);
                squarePos = getPieceColumn(squarePosCtr++, (i % 2) == 0); 
                
                int index = indexOfGreenSquare.indexOf(squarePos);

                if (greenSquares[index].getIcon() != null) {
                	
                    if (getPieceAt(index) == PieceColor.WHITE) {                    	
                        whitePieces.put(newPoint, new Piece(PieceColor.WHITE, newPoint));
                    }else if (getPieceAt(index) == PieceColor.BLACK) {                       	                    	                    	
                        blackPieces.put(newPoint, new Piece(PieceColor.BLACK, newPoint));
                    }
                }
                                
            }                       
        }

        return new Board(blackPieces, whitePieces);
    }

	private void updateBoard(Board gameBoard, Move move, boolean crowned)
	{		
        placePieceToBoard(move, crowned);
		
        if(gameBoard.player == AI.getColor() && !crowned && gameBoard.jumped){
        	if(gameBoard.getJumps(move.movRow, move.movCol).isEmpty()){
        		gameBoard.jumped = false;
        	
        		if(gameBoard.player == PieceColor.WHITE)
        			gameBoard.player = PieceColor.BLACK;
        		else if(gameBoard.player == PieceColor.BLACK)
        			gameBoard.player = PieceColor.WHITE;
        	}else{
        		
        		Move aiMove = ai.getAIMove(gameBoard);
        		crowned = gameBoard.movePiece(aiMove);
        		gameBoard.handleJump(aiMove);
				handleJump(aiMove);
				updateBoard(gameBoard, aiMove, crowned);
        	}
        	
        } else {
        	        	
            if(gameBoard.player == PieceColor.WHITE){            	
            	gameBoard.player = PieceColor.BLACK;
            }else if(gameBoard.player == PieceColor.BLACK){
            	gameBoard.player = PieceColor.WHITE; 
            }                                
        }    
        
        changeTurn();				
		setAPieceWasChosen(false);	
		
		if(gameBoard.getAllPossibleMovesForColor(gameBoard.player).size() == 0){
			
			if(gameBoard.player == PieceColor.WHITE)
				gameBoard.player = PieceColor.BLACK;
			else
				gameBoard.player = PieceColor.WHITE;
			
			if(gameBoard.getAllPossibleMovesForColor(gameBoard.player).size() == 0){
				tie = true;							
			}else{
				if(gameBoard.player == PieceColor.BLACK){
					CheckerFrame.gameOver(new GameOver(), gameBoard.getWhiteCount(), 0, blackMoves, whiteMoves, false);									
				}else if(gameBoard.player == PieceColor.WHITE)
					CheckerFrame.gameOver(new GameOver(), 0 , gameBoard.getWhiteCount(), blackMoves, whiteMoves, false);
			}
		}			
        
	}

	private Pair<Integer, Integer> getRowColAt(int index){		
		int squarePos = indexOfGreenSquare.get(index);
		boolean bool = false;
		int row = 0, col = 0, count = 0;
		for( row = 0; row < 8; row++){
			for( col = 0; col < 8 ; col++){
				if(count == squarePos){
					bool = true;
					break;
				}
				count++;								
			}
			if(bool) break;
		}

		return new Pair<Integer, Integer>(row, col);
	}
	
	private void placePieceToBoard(Move move, boolean crowned)
    {
        /*
         * Here, we're actually getting the ordinality of each piece which will map to the right green square.
         * The first (zeroth, rather) square is the top leftmost green square, and the 32nd square is the
         * bottom rightmost green square.
         */		
		resetBoardColor();
		
        int newXPos = move.movCol;
        int newYPos = move.movRow;
                
        int newPos = 0;
		int x = 0, y = 0;
				
		for( y = 0; y < 8; y++){
			for( x = 0; x < 8 ; x++){
				if(y == newYPos && x == newXPos) break;
				newPos++;
			}
			if(y == newYPos && x == newXPos) break;
		}			
		
		int currXPos = move.currCol;
        int currYPos = move.currRow;        
        int currPos = 0;			
        
		for( y = 0; y < 8; y++){
			for( x = 0; x < 8 ; x++){
				if(y == currYPos && x == currXPos) break;
				currPos++;
			}
			if(y == currYPos && x == currXPos) break;
		}
		
        if (gameBoard.player == PieceColor.BLACK) {        	
            if (crowned || isBlackKing(indexOfGreenSquare.indexOf(currPos))){ 
            	greenSquares[indexOfGreenSquare.indexOf(newPos)].setIcon(blackKing);            
            } else {
            	greenSquares[indexOfGreenSquare.indexOf(newPos)].setIcon(blackChip);
            }
            blackMoves++;
            
        } else  if (gameBoard.player == PieceColor.WHITE) {        	
            if(crowned || isWhiteKing(indexOfGreenSquare.indexOf(currPos))){
            	greenSquares[indexOfGreenSquare.indexOf(newPos)].setIcon(whiteKing);
            } else { 
            	greenSquares[indexOfGreenSquare.indexOf(newPos)].setIcon(whiteChip);
            }            
            whiteMoves++;
        }                        
        
        removePieceAt(currPos);        
        indexOfClickedPath.removeAll(indexOfClickedPath);
		indexOfPathTaken.removeAll(indexOfPathTaken);
		repaint();
    }
	
	private int getIndexFromRowCol(int row, int col){
		 
		int index = 0;
		int x = 0, y = 0;		
		
		for( y = 0; y < 8; y++){
			for( x = 0; x < 8 ; x++){
				if(y == row && x == col) break;
				index++;
			}
			if(y == row && x == col) break;
		}
		
		return index;		
	}
	
	//Deletes the square skipped in jump
    private void handleJump(ArrayList<Integer> indexOfClickedPath2) {
		
    	int count = 0;
    	while(true){
			Move move = new Move(getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(count))).getFirst(), 
					getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(count++))).getSecond(), 
					getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(count))).getFirst(),
					getRowColAt(indexOfGreenSquare.indexOf(indexOfClickedPath2.get(count))).getSecond());
			
			Pair<Integer, Integer> squareSkipped = move.getSquareSkipped();
			if(squareSkipped.getFirst() != move.currRow && squareSkipped.getFirst() != move.movRow &&
					squareSkipped.getSecond() != move.currCol && squareSkipped.getSecond() != move.movCol){
				
				PieceColor pieceSkipped = getPieceAt(indexOfGreenSquare.indexOf(getIndexFromRowCol(squareSkipped.getFirst(), squareSkipped.getSecond())));
				
				if(pieceSkipped == PieceColor.WHITE_KING){
					blackMoves++;					
				}else if(pieceSkipped == PieceColor.BLACK_KING){
					whiteMoves++;					
				}				
				
				if(gameBoard.player == PieceColor.WHITE){
					whiteMoves++;
					
				}else{
					blackMoves++;					
				}				
				
				removePieceAt(getIndexFromRowCol(squareSkipped.getFirst(), squareSkipped.getSecond()));
				
				if(gameBoard.getBlackCount() == 0 || gameBoard.getWhiteCount() == 0){
					CheckerFrame.gameOver(new GameOver(), gameBoard.getBlackCount() , gameBoard.getWhiteCount(), blackMoves, whiteMoves, false);
				}
			}
			
			if(count == indexOfClickedPath2.size()-1) break;
		}		
	}   
    
    private void handleJump(Move move) {
    	
    	Pair<Integer, Integer> squareSkipped = move.getSquareSkipped();
		if(squareSkipped.getFirst() != move.currRow && squareSkipped.getFirst() != move.movRow &&
				squareSkipped.getSecond() != move.currCol && squareSkipped.getSecond() != move.movCol){
						
			PieceColor pieceSkipped = getPieceAt(indexOfGreenSquare.indexOf(getIndexFromRowCol(squareSkipped.getFirst(), squareSkipped.getSecond())));
			
			if(pieceSkipped == PieceColor.WHITE_KING){
				blackMoves++;						
			}else if(pieceSkipped == PieceColor.BLACK_KING){
				whiteMoves++;
			}				
			
			if(gameBoard.player == PieceColor.WHITE){
				whiteMoves++;
				
			}else{
				blackMoves++;
				
			}				
			
			removePieceAt(getIndexFromRowCol(squareSkipped.getFirst(), squareSkipped.getSecond()));
			
			if(gameBoard.getBlackCount() == 0 || gameBoard.getWhiteCount() == 0){
				CheckerFrame.gameOver(new GameOver(), gameBoard.getBlackCount() , gameBoard.getWhiteCount(), blackMoves, whiteMoves, false);
			}
		}	
	}   

	private void removePieceAt(int indexFromRowCol) {
		greenSquares[indexOfGreenSquare.indexOf(indexFromRowCol)].setIcon(null);		
	}

	private int getPieceColumn(int pieceNumber, boolean isEven)
    {
        return (isEven) ? 2 * pieceNumber : (2 * pieceNumber) + 1;
    }
	
	private void placePiecesOnePlayer(){		
		for(int j = 0; j < 32; j++){		
			greenSquares[j] =  new JLabel();				
			squarePanels[indexOfGreenSquare.get(j)].add(greenSquares[j], BorderLayout.CENTER);						
			if(indexOfGreenSquare.get(j) >= 0 && indexOfGreenSquare.get(j) <= 22)
				greenSquares[j].setIcon(aiPiece);			
			if(indexOfGreenSquare.get(j) >= 41 && indexOfGreenSquare.get(j) <= 63)
				greenSquares[j].setIcon(humanPlayerPiece);			
		}				
	}			
	
	public void setPlayers(PieceColor piece){		
		if(piece == PieceColor.WHITE){			
			
			playerColor = PieceColor.WHITE;
			aiColor = PieceColor.BLACK;
			aiKing = PieceColor.BLACK_KING;
			humanPlayerPiece = whiteChip;
			humanPlayerPieceKing = whiteKing;
			aiPiece = blackChip;	
			aiPieceKing = blackKing;
			turn = true;	
		}else{
			
			playerColor = PieceColor.BLACK;			
			aiColor = PieceColor.WHITE;			
			aiKing = PieceColor.WHITE_KING;
			humanPlayerPiece = blackChip;
			humanPlayerPieceKing = blackKing;
			aiPiece = whiteChip;
			aiPieceKing = whiteKing;
			turn = false;	
		}
	}
	
	public static PieceColor getPlayerColor(){
		return playerColor;
	}
			
	public PieceColor getAiColor(){
		return aiColor;		
	}
	
	private void eraseDottedLines(){
		for(int j = 1; j < indexOfClickedPath.size(); j++){
			greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(j))].setIcon(null);
		}		
	}
	
	private boolean cellIsClickedTwice(int index){
		return !indexOfClickedPath.isEmpty() && 
				indexOfGreenSquare.get(index) == indexOfClickedPath.get(indexOfClickedPath.size()-1);
	}
	
	private void markAsPath(int index){
		indexOfClickedPath.add(index);		
		greenSquares[indexOfGreenSquare.indexOf(index)].setIcon(dottedWhite);		
	}
	
	private void clearBoard(){
		resetBoardColor();									
		for(int j = 1; j < indexOfClickedPath.size(); j++){
			greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(j))].setIcon(null);
		}								
		setAPieceWasChosen(false);
		indexOfClickedPath.removeAll(indexOfClickedPath);
		indexOfPathTaken.removeAll(indexOfClickedPath);
		foodSquares.removeAll(foodSquares);
	}
	
	private PieceColor getPieceAt(int squareIndex){

		if(greenSquares[squareIndex].getIcon() == null)
			return PieceColor.EMPTY;
		if(greenSquares[squareIndex].getIcon().toString().equals(blackKing.toString()))
			return PieceColor.BLACK_KING;
		if(greenSquares[squareIndex].getIcon().toString().equals(whiteKing.toString()))
			return PieceColor.WHITE_KING;
		if(greenSquares[squareIndex].getIcon().toString().equals(blackChip.toString()))
			return PieceColor.BLACK;
		if(greenSquares[squareIndex].getIcon().toString().equals(whiteChip.toString()))
			return PieceColor.WHITE;		
		if(greenSquares[squareIndex].getIcon().toString().equals(dottedWhite.toString()))
			return PieceColor.MARKED_PATH;
		
		return PieceColor.EMPTY;
	}

	private boolean isHumanKing(int squareIndex){
		
		if(playerColor == PieceColor.WHITE)
			return getPieceAt(squareIndex) == PieceColor.WHITE_KING;
		else
			return getPieceAt(squareIndex) == PieceColor.BLACK_KING;
	}
	
	private boolean isWhiteKing(int squareIndex){
		return getPieceAt(squareIndex) == PieceColor.WHITE_KING;
	}
	
	private boolean isBlackKing(int squareIndex){
		return getPieceAt(squareIndex) == PieceColor.BLACK_KING;
	}
	
	private boolean isNormalPiece(int squareIndex){				
		if(playerColor == PieceColor.WHITE)
			return getPieceAt(squareIndex) == PieceColor.WHITE;
		else
			return getPieceAt(squareIndex) == PieceColor.BLACK;
	}
	
	private boolean cellIsEmpty(int squareIndex){		
		return getPieceAt(squareIndex) == PieceColor.EMPTY;
	}	
	private boolean isPath(int squareIndex){
		return getPieceAt(squareIndex) == PieceColor.MARKED_PATH;
	}
	
	private boolean isAIPiece(int squareIndex){		
		return getPieceAt(squareIndex) == aiColor;		
	}		
	
	private boolean aPieceWasPreviouslyChosen(){ 
		return prevChip; 
	}	
	private void setAPieceWasChosen(boolean bool){ prevChip = bool; }	
	
	private boolean isHumansTurn(){								
		return gameBoard.player == playerColor;
	}
			
	private void changeTurn() {
		turn = !turn;		
	}	
	
	private boolean isAValidMoveAt(int index){			
		int indexOf = indexOfGreenSquare.indexOf(indexOfClickedPath.get(0));		
		if(isBlackKing(indexOf) || isWhiteKing(indexOf)){						
			if(indexOfClickedPath.size() > 1){				
				int nIndex1 = indexOfClickedPath.get(indexOfClickedPath.size()-1);									
				return ((nIndex1%9 == index%9 && Math.abs(nIndex1-index) == 18) || 
						(nIndex1%7 == index%7 && Math.abs(nIndex1-index) == 14));				
			}else if(indexOfClickedPath.size() == 1){												
				int nIndex1 = indexOfClickedPath.get(0);								
				return ((nIndex1%9 == index%9 && (Math.abs(nIndex1-index) == 9 || 
						(Math.abs(nIndex1-index) == 18 && foodSquares.contains(index)))) 
					|| (nIndex1%7 == index%7 && (Math.abs(nIndex1-index) == 7 || 
						(Math.abs(nIndex1-index) == 14 && foodSquares.contains(index)))));												
			}			
		}else{														
			if(indexOfClickedPath.size() > 1){								
				int nIndex1 = indexOfClickedPath.get(indexOfClickedPath.size()-1);									
				return ((nIndex1%9 == index%9 && Math.abs(nIndex1-index) == 18) || (nIndex1%7 == index%7 && 
						Math.abs(nIndex1-index) == 14));				
			}else if(indexOfClickedPath.size() == 1){												
				int nIndex1 = indexOfClickedPath.get(0);
				
				if(gameBoard.player == aiColor){						
					return ((nIndex1%9 == index%9 && ((index - nIndex1) == 9 || 
							(Math.abs(nIndex1-index) == 18 && foodSquares.contains(index)))) 
						|| (nIndex1%7 == index%7 && ((index - nIndex1) == 7 || 
							(Math.abs(nIndex1-index) == 14 && foodSquares.contains(index)))));						
				}					
				if(gameBoard.player == playerColor){					
					return ((nIndex1%9 == index%9 && ((nIndex1-index) == 9 || 
							(Math.abs(nIndex1-index) == 18 && foodSquares.contains(index)))) 
						|| (nIndex1%7 == index%7 && ((nIndex1-index) == 7 || 
							(Math.abs(nIndex1-index) == 14 && foodSquares.contains(index)))));
				}
							
				return ((nIndex1%9 == index%9 && ((nIndex1-index) == 9 || 
						(Math.abs(nIndex1-index) == 18 && foodSquares.contains(index)))) 
					|| (nIndex1%7 == index%7 && ((nIndex1-index) == 7 || 
						(Math.abs(nIndex1-index) == 14 && foodSquares.contains(index)))));
			}			
		}		
		return false;
	}
	
	public void resetBoardColor(){		
		for(int j = 0; j < 32; j++){			
			squarePanels[indexOfGreenSquare.get(j)].setBackground(Color.GREEN);
		}
	}
	
	private void newPieceClick(Icon chipColor, int currSquareIndex, int index){										
		squarePanels[index].setBackground(Color.YELLOW);									
		if(isWhiteKing(currSquareIndex) || isBlackKing(currSquareIndex)){			
			kingspathGlow(index, chipColor);	
			glowJump(index);
		}else{			
			pathGlow(index, chipColor);		
			glowJump(index);
		}		
		indexOfPathTaken.removeAll(indexOfPathTaken);		
		prevChip = true;
	}
		
	
	private void glowJump(int index){			
		int[] indicesMid = { 7, -7, 9, -9};
		int[] indicesRight = { 7, -9};
		int[] indicesLeft = { -7, 9};
		int[] indicesEEdge = {7, 9};
		int[] indicesMEdge = {-9, -7};
		int[] indexRightEdge = {-9};
		int[] indexLeftEdge = {9};
		int[] indices;
		
		if(rightEdge.contains(index)){			
			if(bottomEdge.contains(index))
				indices = indexRightEdge;
			else
				indices = indicesRight;						
		}else if(leftEdge.contains(index)){			
			if(upperEdge.contains(index))
				indices = indexLeftEdge;
			else
				indices = indicesLeft;			
		}else if(upperEdge.contains(index)){				
			indices  = indicesEEdge;			
		}else if(bottomEdge.contains(index)){
			indices = indicesMEdge;			
		}else{ 
			indices = indicesMid; 			
		}		
		
		for(int i = 0; i < indices.length; i++){			
			int index2 = index + indices[i];						
			if(indexOfGreenSquare.indexOf(index2) != indexOfPathTaken.get(indexOfPathTaken.size()-1)){

				PieceColor neighborColor = getNeighbor(index, indices[i]);
				if(neighborColor != null){
				
					if((neighborColor == aiColor || neighborColor == aiKing)							
							&& !isAtEdges(index2) && getNeighbor(index2, indices[i]) == PieceColor.EMPTY){						
						squarePanels[index2].setBackground(Color.RED);
						squarePanels[index2 + indices[i]].setBackground(Color.CYAN);	
						storePath(indexOfGreenSquare.indexOf(index2));
						foodSquares.add(index2 + indices[i]);
						glowJump(index2 + indices[i]);			
					}				
				}
			}
		}
	}	
	
	private boolean isAtEdges(int index){		
		return (rightEdge.contains(index) || leftEdge.contains(index) || upperEdge.contains(index) || bottomEdge.contains(index));
	}
	
	private PieceColor getNeighbor(int index, int direction){														
		int var = index + direction;
		Icon icon = greenSquares[indexOfGreenSquare.indexOf(var)].getIcon();
		
		if(icon != null){			
			String square = greenSquares[indexOfGreenSquare.indexOf(var)].getIcon().toString();		
			Icon enemyPiece = null;
			Icon currPiece = null;			
							
			enemyPiece = aiPiece;
			currPiece = humanPlayerPiece;				
						
			if(square.equals(enemyPiece.toString())){								
				return aiColor;
			}					
			if(square.equals(currPiece.toString())){
				return playerColor;
			}			
			
			if(isBlackKing(indexOfGreenSquare.indexOf(var))){				
				return PieceColor.BLACK_KING;
			}
			
			if(isWhiteKing(indexOfGreenSquare.indexOf(var))){				
				return PieceColor.WHITE_KING;
			}
		}				
		return PieceColor.EMPTY;		
	}	
	
	private void kingspathGlow(int index, Icon color){					
		int[] right = {-9, 7}, left = {-7, 9};
		int[] kingsEdge = {7, 9}, startEdge = {-9, -7};
		int[] leftKings = {9}, rightStart = {-9};
		int[] mid = {-7, -9, 7, 9};
		int[] array;		
							
		if(rightEdge.contains(index)){			
			if(bottomEdge.contains(index))
				array = rightStart;
			else array = right;
		}else if(leftEdge.contains(index)){		
			if(upperEdge.contains(index))
				array = leftKings;
			else array = left;
		}else if(upperEdge.contains(index))
			array = kingsEdge;		
		else if(bottomEdge.contains(index))			
			array = startEdge;		
		else array = mid;		
			
		for(int i = 0; i < array.length; i++){						
			int indexOfn = indexOfGreenSquare.indexOf(index + array[i]);			
			if(cellIsEmpty(indexOfn)){				
				squarePanels[index + array[i]].setBackground(Color.CYAN);
			}									
		}			
	}
	
	public void pathGlow(int index, Icon color){							
		int[] right = null, left = null;
		int[] startEdge = {-9, -7};
		int[] rightStart = {-9};
		int[] mid = null;
		int[] array;											
		int[] whiteMid = {-7, -9};
		int[] whiteRight = {-9};
		int[] whiteLeft = {-7};
		
		
		mid = whiteMid;
		right = whiteRight;
		left = whiteLeft;
		
		if(rightEdge.contains(index)){		
			if(bottomEdge.contains(index))
				array = rightStart;
			else array = right;
		}else if(leftEdge.contains(index))				
			array = left;		
		else if(bottomEdge.contains(index))			
			array = startEdge;		
		else array = mid;
					
		for(int i = 0; i < array.length; i++){					
			int indexOfn = indexOfGreenSquare.indexOf(index + array[i]);			
			if(cellIsEmpty(indexOfn)){				
				squarePanels[index + array[i]].setBackground(Color.CYAN);
			}									
		}		
	}		
	
	private void storePath(int indexOfSquarePath){			
		indexOfPathTaken.add(indexOfSquarePath);			
	}	

}

