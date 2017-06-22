import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CheckerBoard extends JPanel{
	private static final long serialVersionUID = 1L;
	
	//Board gameBoard = new Board();
	
	private JPanel board = new JPanel(new GridLayout(8, 8, 2, 2));
	private JPanel[] squarePanels = new JPanel[64];	
	private JLabel[] greenSquares = new JLabel[32];				
	
	private Icon whiteChip = new ImageIcon(getClass().getResource("white.png"));
	private Icon whiteKing = new ImageIcon(getClass().getResource("whiteKing.png"));	
	private Icon blackChip = new ImageIcon(getClass().getResource("black.png"));
	private Icon blackKing = new ImageIcon(getClass().getResource("blackKing.png"));	
	private Icon dottedWhite = new ImageIcon(getClass().getResource("eatpathwhite.png"));		
	
	private static int aiNumberOfPieces = 12;
	private static int humanNumberOfPieces = 12;
	private static int blacksScore;
	private static int whitesScore;
	private static int blacksMoves;
	private static int whitesMoves;
	
	//////////////////////////////
	private int[] aiPlayer_moves = new int[50000];
	private int aiMovesCount = 0; 
	///////////////////////////////	
	
	private static int currSquareIndex;
	protected static boolean turn; 
	
	private static boolean prevChip = false;
	private static boolean aiprevChip = false;
	
	ArrayList<Integer> indexOfPath = new ArrayList<Integer>();
	ArrayList<Integer> greenSqrIndex = new ArrayList<Integer>();
	ArrayList<Integer> indexOfClickedPath = new ArrayList<Integer>();
	ArrayList<Integer> leftEdge = new ArrayList<Integer>();
	ArrayList<Integer> rightEdge = new ArrayList<Integer>();
	ArrayList<Integer> aiEdge = new ArrayList<Integer>();
	ArrayList<Integer> myEdge = new ArrayList<Integer>();	
	ArrayList<Integer> eatSquares = new ArrayList<Integer>();
		
	//	Corresponding specific POINTS assigned to black and white pieces.
	int indices[] = {0, 2, 4, 6, 9, 11, 13, 15, 16, 18, 20, 22, 25, 27, 29, 31, 32, 34, 36, 38, 41, 	
			43, 45, 47, 48, 50, 52, 54, 57, 59, 61, 63};		
	
	//	Edge of Boards
	int[] lEdge = {0, 16, 32, 48}, rEdge = {15, 31, 47, 63};		
	int[] kingsEdge = {0, 2, 4, 6}, inEdge = {57, 59, 61, 63};

	String OnePlayer = "1p", TwoPlayer = "2p", gameMode;
	
	private static Icon humanPlayerPiece, aiPiece, humanPlayerPieceKing, aiPieceKing;
	
	public CheckerBoard(){
		
		init();	
		placePiece2P();
		setMode(TwoPlayer);		
		
	}	
	
	public CheckerBoard(String piece){
		
		init();		
		setPlayerPiece(piece);				
		placePiece1P();
		setMode(OnePlayer);
		
	}
	
	private void init(){
		
		blacksScore = 12;
		whitesScore = 12;
		blacksMoves = 0;
		whitesMoves = 0;
		
		turn = true;
		
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
				if(i%2 != 0)
					squarePanels[i].setBackground(Color.GREEN);
			}

			squarePanels[i].addMouseListener(handler);
		}		
			
		for(int i = 0; i < indices.length; i++){
			greenSqrIndex.add(indices[i]);
		}
						

		for(int i = 0; i < rEdge.length; i++){
			rightEdge.add(rEdge[i]);
			leftEdge.add(lEdge[i]);
		}
		
		for(int i = 0; i < kingsEdge.length; i++){
			aiEdge.add(kingsEdge[i]);
		}
		
		for(int i = 0; i < inEdge.length; i++){
			myEdge.add(inEdge[i]);
		}
		
		add(board);
				
		
	}
	
	private void placePiece1P(){
		
		for(int j = 0; j < 32; j++){
			
			greenSquares[j] =  new JLabel();				
			squarePanels[greenSqrIndex.get(j)].add(greenSquares[j], BorderLayout.CENTER);			
			
			if(greenSqrIndex.get(j) >= 0 && greenSqrIndex.get(j) <= 22)
				greenSquares[j].setIcon(aiPiece);
			
			if(greenSqrIndex.get(j) >= 41 && greenSqrIndex.get(j) <= 63)
				greenSquares[j].setIcon(humanPlayerPiece);			
		}		
		
	}
	
	private void placePiece2P(){
		
		for(int j = 0; j < 32; j++){
			
			greenSquares[j] =  new JLabel();				
			squarePanels[greenSqrIndex.get(j)].add(greenSquares[j], BorderLayout.CENTER);			
			
			//	placement of Black Chips
			if(greenSqrIndex.get(j) >= 0 && greenSqrIndex.get(j) <= 22)
				greenSquares[j].setIcon(blackChip);
			
			//	placement of White Chips
			if(greenSqrIndex.get(j) >= 41 && greenSqrIndex.get(j) <= 63)
				greenSquares[j].setIcon(whiteChip);			
		}			
		
	}
	
	public void setPlayerPiece(String piece){
		
		if(piece.equals("white")){
			humanPlayerPiece = whiteChip;
			humanPlayerPieceKing = whiteKing;
			aiPiece = blackChip;					
		}
		
		if(piece.equals("black")){
			humanPlayerPiece = blackChip;
			humanPlayerPieceKing = blackKing;
			aiPiece = whiteChip;
		}
	}
	
	private void setMode(String mode){
		gameMode = mode;
	}
	
	private void eraseDottedLines(){

		for(int j = 1; j < indexOfClickedPath.size(); j++){
			greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);
		}
		
	}
	
	private boolean isClickedTwice(int index){
		return !indexOfClickedPath.isEmpty() && 
				greenSqrIndex.get(index) == indexOfClickedPath.get(indexOfClickedPath.size()-1);
	}
	
	private void markAsPath(int index){
		
		//Add index of square to consecutive collection of paths.
		indexOfClickedPath.add(index);		

		//Mark square with dotted piece.
		greenSquares[greenSqrIndex.indexOf(index)].setIcon(dottedWhite);
		
	}
	
	private void resetAndClean(){

		//Reset board color back to green.
		resetBoardColor();									
		
		//Erase marks (dotted pieces).
		for(int j = 1; j < indexOfClickedPath.size(); j++){
			greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);
		}								
		
		//Clean path collection.
		aPieceWasChosen(false);
		indexOfClickedPath.removeAll(indexOfClickedPath);
		indexOfPath.removeAll(indexOfClickedPath);
		eatSquares.removeAll(eatSquares);
	}
	
	private boolean isHumanKing(int square){
		return greenSquares[square].getIcon().toString().equals(humanPlayerPieceKing.toString());		
	}
	
	private boolean isKing(int square){
		
		if(gameMode.equals(TwoPlayer)){
			return (greenSquares[square].getIcon().toString().equals(blackKing.toString()) ||
					greenSquares[square].getIcon().toString().equals(whiteKing.toString()));
		}else{
			
			return (greenSquares[square].getIcon().toString().equals(humanPlayerPieceKing.toString()));
		}
	}
	
	private boolean isWhiteKing(int square){
		return greenSquares[square].getIcon().toString().equals(whiteKing.toString());
	}
	
	private boolean isBlackKing(int square){
		return greenSquares[square].getIcon().toString().equals(blackKing.toString());
	}
	
	private boolean isNormalPiece(int square){
		
		if(gameMode.equals(TwoPlayer)){
			return (greenSquares[square].getIcon().toString().equals(blackChip.toString()) ||
					greenSquares[square].getIcon().toString().equals(whiteChip.toString()));
		}else{
			
			return (greenSquares[square].getIcon().toString().equals(humanPlayerPiece.toString()));
		}
	}
	
	private boolean isEmpty(int square){		
		return greenSquares[square].getIcon() == null;
	}
	
	private boolean isPath(int square){
		return greenSquares[square].getIcon().toString().equals(dottedWhite.toString());
	}
	
	private boolean isAIPiece(int square){
		return greenSquares[square].getIcon().toString().equals(aiPiece.toString());
	}
		
	private boolean aPieceWasChosen(){
		return prevChip;
	}
	
	private void aPieceWasChosen(boolean bool){
		prevChip = bool;
	}
	
	private boolean isWhitesTurn(){
		return turn;
	}
	
	private boolean isBlacksTurn(){
		return !turn;
	}
	
	private boolean isHumansTurn(){
		
		if(gameMode.equals(OnePlayer)){
			
			if(humanPlayerPiece.equals(whiteChip))
				return turn;			
		}		
		
		return !turn;
	}
	
	private boolean isAiTurn(){
		
		if(aiPiece.equals(blackChip)){
			return !turn;
		}
		
		return turn;
	}
	
	private boolean isAtKingsEdge(int index){
		return (aiEdge.contains(index) || myEdge.contains(index));
	}
	
	private boolean isWhitePiece(int square){
		return (greenSquares[square].getIcon().toString().equals(whiteChip.toString()));
	}
	
	private boolean isBlackPiece(int square){
		return (greenSquares[square].getIcon().toString().equals(blackChip.toString()));
	}
	
	public class MovementHandler extends MouseAdapter{
	
		public void mouseClicked(MouseEvent e){
			Object source = e.getSource();		
			
			if(gameMode.equals(OnePlayer)){
				HumanVsAi(source);
			}
			
			if(gameMode.equals(TwoPlayer)){
				
				HumanVsHuman(source);
			}
		}
	}
	
	private void HumanVsAi(Object source){
				
		int index = 0;		
		boolean isGreenSquare = false;
		
		int i;
		for(i = 0; i < 32; i++){
			
			if(source == squarePanels[greenSqrIndex.get(i)]){				
				currSquareIndex = i;
				index = greenSqrIndex.get(i);
				isGreenSquare = true;
				break;
			}								
		}
						
		//If user clicked a green square.
		if(isGreenSquare){
			
			// Human player's turn.
			if(isHumansTurn()){																												
				
				//If a piece was previously clicked.				
				if(aPieceWasChosen()){							
										
					//If the square is either empty or a path.					
					if( isEmpty(i) || isPath(i) ){
								
						//If the square is clicked twice.							
						if(isClickedTwice(i)){								
										
							//Move the piece.
							eraseDottedLines();
							movePiece(index, i);								
						
						}else{	//If the square was clicked once.
														
							if( isValid(index) ){
								//System.out.println("" + index + " is valid");
								markAsPath(index);								
							}
							else 
								resetAndClean();
						}
					
					// Clicked square is either a King or a normal piece.						
					}else if( isNormalPiece(i) || isHumanKing(i) ){
												
						resetAndClean();	
						storePath(i);

						//Make path of chosen piece glow.						
						if(isHumanKing(i))					
							newPieceClick(humanPlayerPieceKing, i, index);
						
						if(isNormalPiece(i))
							newPieceClick(humanPlayerPiece, i, index);
						
						//Add piece to collection of paths.						
						indexOfClickedPath.add(index);
						
					// The user clicked the opponent's (AI) piece.						
					}else if(isAIPiece(i)){							
						
						// This is an invalid move.
						// Reset and clean board.
						resetAndClean();	

					// The user clicked a non-green square.
					}else{
						
						// This is an invalid move. 
						// Reset and clean board.
						resetAndClean();
					}
				}
								
				// A chip was not previously clicked.			
				else if( !isEmpty(i) && !aPieceWasChosen()){
				
					if(isHumanKing(i) || isNormalPiece(i)){
						storePath(i);
						
						if(isHumanKing(i))													
							newPieceClick(humanPlayerPieceKing, currSquareIndex, index);							
						
						if(isNormalPiece(i))					
							newPieceClick(humanPlayerPiece, currSquareIndex, index);						
															
						indexOfClickedPath.add(index);
						
					}					
				}														
				
								
			}else{
				
				if(aiprevChip == true){		
					
					if(index-9 == aiPlayer_moves[aiMovesCount-1]){
											
						greenSquares[i].setIcon(aiPiece);
						aiPlayer_moves[aiMovesCount++] = index;

						int temp = index-9;
						for(int a = 0; a < 32; a++){
							if(temp == greenSqrIndex.get(a)){
								greenSquares[a].setIcon(null);																	
							}
						}										
						
						resetBoardColor();						
						
						turn = !turn;
						aiprevChip = false;								
					
					}else if(index-7 == aiPlayer_moves[aiMovesCount-1]){
															
						greenSquares[i].setIcon(aiPiece);
						
						aiPlayer_moves[aiMovesCount++] = index;
															
						int temp = index-7;
						for(int a = 0; a < 32; a++){
							if(temp == greenSqrIndex.get(a)){
								greenSquares[a].setIcon(null);										
							}
						}					
					
						resetBoardColor();						
						
						turn = !turn;
						aiprevChip = false;																		
																						

				}else if(greenSquares[i].getIcon().toString().equals(aiPiece.toString())){
					
					// The user chose another black chip.
					aiprevChip = false;
					
				}
				
				// The block is OCCUPIED with a white chip.
				else if(greenSquares[i].getIcon().toString().equals(humanPlayerPiece)){
					
					// Change board color back to green.
					resetBoardColor();	
				}		
				
				
			}
		
			if(aiprevChip == false){
				
				if(greenSquares[i].getIcon() == aiPiece){	
					
					aiPlayer_moves[aiMovesCount++] = index;
					squarePanels[index].setBackground(Color.YELLOW);
					
					//chipsGlow(currSquareIndex, index, blackChip);
					
					aiprevChip = true;								
				}
			}
		}				
				
		//User did not clicked a green square.
		}else{
			
			resetAndClean();
		}		
		
	}
		
	private void HumanVsHuman(Object source){
		
		int index = 0;		
		boolean isGreenSquare = false;
		
		int i;
		for(i = 0; i < 32; i++){
			
			if(source == squarePanels[greenSqrIndex.get(i)]){				
				currSquareIndex = i;
				index = greenSqrIndex.get(i);
				isGreenSquare = true;
				break;
			}								
		}
				
		
		if(isGreenSquare){
			 
			if(isWhitesTurn()){					
							
				if(aPieceWasChosen()){							
																	
					if( isEmpty(i) || isPath(i) ){
														
						if(isClickedTwice(i)){								
										
							eraseDottedLines();								
							++whitesMoves;							
							movePiece(index, i);														
						
						}else{	
														
							if( isValid(index) )								
								markAsPath(index);								
							
							else 
								resetAndClean();
						}
											
					}else if( isWhitePiece(i) || isWhiteKing(i) ){
												
						resetAndClean();	
						storePath(i);
						
						if(isWhiteKing(i))					
							newPieceClick(whiteKing, i, index);
						
						if(isWhitePiece(i))
							newPieceClick(whiteChip, i, index);
												
						indexOfClickedPath.add(index);
						
					}else if(isBlackPiece(i)){
						
						resetAndClean();	

					}else{
						
						resetAndClean();
					}
				}
											
				else if( !isEmpty(i) && !aPieceWasChosen()){
				
					if(isWhitePiece(i) || isWhiteKing(i)){
						
						storePath(i);
						
						if(isWhiteKing(i))													
							newPieceClick(whiteKing, currSquareIndex, index);							
						
						if(isWhitePiece(i))					
							newPieceClick(whiteChip, currSquareIndex, index);						
											
						indexOfClickedPath.add(index);
						
					}						
				}														
				
								
			}else if(isBlacksTurn()){
												
				if(aPieceWasChosen()){							
																	
					if( isEmpty(i) || isPath(i) ){
													
						if(isClickedTwice(i)){								
										
							eraseDottedLines();							
							++blacksMoves;							
							movePiece(index, i);														
						
						}else{	
														
							if( isValid(index) ){								
								markAsPath(index);								
							}
							else 
								resetAndClean();
						}
											
					}else if( isBlackPiece(i) || isBlackKing(i) ){
												
						resetAndClean();	
						storePath(i);

						if(isBlackKing(i))					
							newPieceClick(blackKing, i, index);
						
						if(isBlackPiece(i))
							newPieceClick(blackChip, i, index);
												
						indexOfClickedPath.add(index);
										
					}else if(isBlackPiece(i)){							
						
						resetAndClean();	

					}else{
						
						resetAndClean();
					}
				}
											
				else if( !isEmpty(i) && !aPieceWasChosen()){
				
					if(isBlackPiece(i) || isBlackKing(i)){
						storePath(i);
						
						if(isBlackKing(i))													
							newPieceClick(blackKing, currSquareIndex, index);							
						
						if(isBlackPiece(i))					
							newPieceClick(blackChip, currSquareIndex, index);						
											
						indexOfClickedPath.add(index);
						
					}						
				}		
			}
				
		}else resetAndClean();		
		
		
		
	}
	
	private void movePiece(int index, int i){		
										
		int nIndex = greenSqrIndex.indexOf(indexOfClickedPath.get(0));		
		
		if(isKing(nIndex))		
			moveKing(i);
		
		else if(isNormalPiece(nIndex)){
			
			//boolean isAtKingsEdge = false;
			//if(aiEdge.contains(index)) isAtKingsEdge = true;		
					
			if(isAtKingsEdge(index)){
				
				if(gameMode.equals(TwoPlayer)){
					
					if(isBlacksTurn()){
						
						if(myEdge.contains(index))
							greenSquares[currSquareIndex].setIcon(blackKing);
						else
							greenSquares[currSquareIndex].setIcon(blackChip);
					
					}else if(isWhitesTurn()){
						
						if(aiEdge.contains(index))
							greenSquares[currSquareIndex].setIcon(whiteKing);
						else
							greenSquares[currSquareIndex].setIcon(whiteChip);
					}
					
				}else	greenSquares[currSquareIndex].setIcon(humanPlayerPieceKing);
				
			}else{
				
				if(gameMode.equals(TwoPlayer)){
					
					if(isBlacksTurn())
						greenSquares[currSquareIndex].setIcon(blackChip);					
					
					if(isWhitesTurn())
						greenSquares[currSquareIndex].setIcon(whiteChip);
					
				}else	greenSquares[currSquareIndex].setIcon(humanPlayerPiece);
			}
						
			greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(0))].setIcon(null);									
			
			resetBoardColor();
			eatEnemyPiece(i);	
			
			turn = !turn;				
			prevChip = false;
			
			if(turn)
				CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource("whiteturn.png")));
			else
				CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource("blackturn.png")));
		
		}

		indexOfClickedPath.removeAll(indexOfClickedPath);
		indexOfPath.removeAll(indexOfPath);
				
	}
	
	private void moveKing(int i){
								
		if(gameMode.equals(TwoPlayer)){
			
			if(isBlacksTurn())														
				greenSquares[currSquareIndex].setIcon(blackKing);
			
			else if(isWhitesTurn())
				greenSquares[currSquareIndex].setIcon(whiteKing);
			
		}else greenSquares[currSquareIndex].setIcon(humanPlayerPieceKing);
		
		greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(0))].setIcon(null);
		
		resetBoardColor();
		eatEnemyPiece(i);	

		turn = !turn;																		
		prevChip = false;		

	}		
	
	private void eatEnemyPiece(int j){
		
		//int score = 0;
		for(int i = 0 ; i + 1 < indexOfClickedPath.size(); i++){
						
			int var = (indexOfClickedPath.get(i+1) - indexOfClickedPath.get(i));					
			
			if(var < 0){
				
				if(var == -14){
					
					Icon piece = greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) - 7)].getIcon();					
					
					if(piece != null){
						
						if(isWhitesTurn())
							blacksScore--;							
						else if(isBlacksTurn())
							whitesScore--;																			
					}
					
					greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) - 7)].setIcon(null);
				
				}
				
				if(var == -18){
					
					Icon piece = greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) - 9)].getIcon();					
					
					if(piece != null){
						
						if(isWhitesTurn())
							blacksScore--;
						else if(isBlacksTurn())
							whitesScore--;
													
					}
					
					greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) - 9)].setIcon(null);
				}
			}
			
			if(var > 0){
				
				if(var == 14){
					
					Icon piece = greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) + 7)].getIcon();					
					
					if(piece != null){
						
						if(isWhitesTurn())
							blacksScore--;
						else if(isBlacksTurn())
							whitesScore--;						
					}
					
					greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) + 7)].setIcon(null);
				
				}
				
				if(var == 18){
					
					Icon piece = greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) + 9)].getIcon();					
					
					if(piece != null){						
						if(isWhitesTurn())
							blacksScore--;							
						
						else if(isBlacksTurn())
							whitesScore--;						
					}
					
					greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) + 9)].setIcon(null);
				
				}
				
			}
			
			if(greenSqrIndex.get(j) == indexOfClickedPath.get(i+1)) break;
		}				
		
		if(blacksScore == 0 || whitesScore == 0){
			CheckerFrame.gameOver(new GameOver(), blacksScore, whitesScore, blacksMoves, whitesMoves);
		}
		
	}
	
	private boolean isValid(int index){
					
		int indexOf = greenSqrIndex.indexOf(indexOfClickedPath.get(0));
		
		if(isKing(indexOf)){
						
			if(indexOfClickedPath.size() > 1){
				
				int nIndex1 = indexOfClickedPath.get(indexOfClickedPath.size()-1);					
				
				return ((nIndex1%9 == index%9 && Math.abs(nIndex1-index) == 18) || (nIndex1%7 == index%7 && 
						Math.abs(nIndex1-index) == 14));
				
			}else if(indexOfClickedPath.size() == 1){
												
				int nIndex1 = indexOfClickedPath.get(0);								
				return ((nIndex1%9 == index%9 && (Math.abs(nIndex1-index) == 9 || 
						(Math.abs(nIndex1-index) == 18 && eatSquares.contains(index)))) 
					|| (nIndex1%7 == index%7 && (Math.abs(nIndex1-index) == 7 || 
						(Math.abs(nIndex1-index) == 14 && eatSquares.contains(index)))));												
			}
			
		}else{						
			
			if(indexOfClickedPath.size() > 1){
								
				int nIndex1 = indexOfClickedPath.get(indexOfClickedPath.size()-1);					
				
				return ((nIndex1%9 == index%9 && Math.abs(nIndex1-index) == 18) || (nIndex1%7 == index%7 && 
						Math.abs(nIndex1-index) == 14));
				
			}else if(indexOfClickedPath.size() == 1){
												
				int nIndex1 = indexOfClickedPath.get(0);		
				
				if(gameMode.equals(TwoPlayer)){
					
					if(isBlacksTurn()){
						
						return ((nIndex1%9 == index%9 && ((index - nIndex1) == 9 || 
								(Math.abs(nIndex1-index) == 18 && eatSquares.contains(index)))) 
							|| (nIndex1%7 == index%7 && ((index - nIndex1) == 7 || 
								(Math.abs(nIndex1-index) == 14 && eatSquares.contains(index)))));
						
					}
					
					if(isWhitesTurn()){
						
						return ((nIndex1%9 == index%9 && ((nIndex1-index) == 9 || 
								(Math.abs(nIndex1-index) == 18 && eatSquares.contains(index)))) 
							|| (nIndex1%7 == index%7 && ((nIndex1-index) == 7 || 
								(Math.abs(nIndex1-index) == 14 && eatSquares.contains(index)))));
					}
				}
				
				return ((nIndex1%9 == index%9 && ((nIndex1-index) == 9 || 
						(Math.abs(nIndex1-index) == 18 && eatSquares.contains(index)))) 
					|| (nIndex1%7 == index%7 && ((nIndex1-index) == 7 || 
						(Math.abs(nIndex1-index) == 14 && eatSquares.contains(index)))));
			}			
		}
		
		return false;
	}
	
	
	public void resetBoardColor(){		
		for(int j = 0; j < 32; j++){			
			squarePanels[greenSqrIndex.get(j)].setBackground(Color.GREEN);
		}
	}
	
	private void newPieceClick(Icon chipColor, int currSquareIndex, int index){
										
		squarePanels[index].setBackground(Color.YELLOW);
									
		if(isWhiteKing(currSquareIndex) || isBlackKing(currSquareIndex)){
			
			kingspathGlow(index, chipColor);	
			checkForKingsFood(index);
			
		}else{
			
			pathGlow(index, chipColor);		
			checkForFood(index);			
			
		}
		
		indexOfPath.removeAll(indexOfPath);		
		prevChip = true;
	}
		
	private void checkForFood(int index){
		
		System.out.println("index: " + index);
		
		int[] indicesMid = { 7, -7, 9, -9};
		int[] indicesRight = { 7, -9};
		int[] indicesLeft = { -7, 9};
		int[] indicesEEdge = {7, 9};
		int[] indicesMEdge = {-7, -9};
		int[] indexRightEdge = {-9};
		int[] indexLeftEdge = {9};
		int[] indices;
		
		if(rightEdge.contains(index)){
			
			if(myEdge.contains(index))
				indices = indexRightEdge;
			else
				indices = indicesRight;			
			
			System.out.println("1");
		}else if(leftEdge.contains(index)){
			
			if(aiEdge.contains(index))
				indices = indexLeftEdge;
			else
				indices = indicesLeft;
			System.out.println("2");
		}
		else if(aiEdge.contains(index)){				
			indices  = indicesEEdge;	
			System.out.println("3");
		}else if(myEdge.contains(index)){
			indices = indicesMEdge;
			System.out.println("4");
		}else{ indices = indicesMid; System.out.println("5");
		}
		String enemyPlayer = null;
		String enemyPlayerKing = null;
		
		if(gameMode.equals(TwoPlayer)){
			
			if(isBlacksTurn()){			
				enemyPlayer = whiteChip.toString();
				enemyPlayerKing = whiteKing.toString();
				
			}else if(isWhitesTurn()){
				enemyPlayer = blackChip.toString();
				enemyPlayerKing = blackKing.toString();
				
			}
			
		}else	enemyPlayer = aiPiece.toString();			
		
		for(int i = 0; i < indices.length; i++){
			
			//System.out.println("--------||" + indices[i]);
			int index2 = index + indices[i];
			
			//System.out.println("index2: " + index2);
			//System.out.println(indexOfPath);
			
			if(index2 != indexOfPath.get(indexOfPath.size()-1)){
				
				//System.out.println("con1");
				//System.out.println("index2: " + index2);
				
				Icon icon = getNeighbor(index, indices[i]);
				if(icon != null){
				
				//System.out.println("con2");
				String neighbor = icon.toString();
				
					if((neighbor.equals(enemyPlayer) || neighbor.equals(enemyPlayerKing)) 
							&& !isAtEdges(index2) && getNeighbor(index2, indices[i]) == null){
						
						//System.out.println("con3");
					
						squarePanels[index2].setBackground(Color.RED);
						squarePanels[index2 + indices[i]].setBackground(Color.CYAN);
						
						storePath(index2);
						//System.out.println("store " + index2);
						eatSquares.add(index2 + indices[i]);
						checkForFood(index2 + indices[i]);
						
					}
				
				}
			}
		}
		
		
	}
	
	private void checkForKingsFood(int index){		
		
		int[] indicesMid = { 7, -7, 9, -9};
		int[] indicesRight = { 7, -9};
		int[] indicesLeft = { -7, 9};
		int[] indicesEEdge = {7, 9};
		int[] indicesMEdge = {-7, -9};
		int[] indexRightEdge = {-9};
		int[] indexLeftEdge = {9};
		int[] indices;
		
		if(rightEdge.contains(index)){
			
			if(myEdge.contains(index))
				indices = indexRightEdge;
			else
				indices = indicesRight;			
			
		}else if(leftEdge.contains(index)){
			
			if(aiEdge.contains(index))				
				indices = indexLeftEdge;
			else
				indices = indicesLeft;
			
		}
		else if(aiEdge.contains(index))			
			indices  = indicesEEdge;	
		
		else if(myEdge.contains(index))
			indices = indicesMEdge;
		
		else indices = indicesMid;			
		
		
		String enemyPlayer = null;
		String enemyPlayerKing = null;
		
		if(gameMode.equals(TwoPlayer)){
			
			if(isBlacksTurn()){			
				enemyPlayerKing = whiteKing.toString();
				enemyPlayer = whiteChip.toString();
				
			}else if(isWhitesTurn()){
				enemyPlayerKing = blackKing.toString();
				enemyPlayer = blackChip.toString();
			}
				
		}else	enemyPlayer = aiPiece.toString();
					
		for(int i = 0; i < indices.length; i++){
			
			int index2 = index + indices[i];												
			if(index2 != indexOfPath.get(indexOfPath.size()-1)){
								
				Icon icon = getNeighbor(index, indices[i]);
				if(icon != null){

				String neighbor = icon.toString();
				
					System.out.println("!isAtEdges(index2): " + !isAtEdges(index2));
					if((neighbor.equals(enemyPlayer) || neighbor.equals(enemyPlayerKing)) 
							&& !isAtEdges(index2) && getNeighbor(index2, indices[i]) == null){												
						
						if(!indexOfPath.isEmpty()){
														
								squarePanels[index2].setBackground(Color.RED);
								squarePanels[index2 + indices[i]].setBackground(Color.CYAN);
								
								storePath(index2);
								eatSquares.add(index2 + indices[i]);
								checkForFood(index2 + indices[i]);							
							
						}
						
						if(indexOfPath.isEmpty()){
							squarePanels[index2].setBackground(Color.RED);
							squarePanels[index2 + indices[i]].setBackground(Color.CYAN);
							
							storePath(index2);
							eatSquares.add(index2 + indices[i]);
							checkForFood(index2 + indices[i]);
						}
						
					}
				
				}
			}
		}
		
		
	}
	
	private boolean isAtEdges(int index){		
		return (rightEdge.contains(index) || leftEdge.contains(index) || aiEdge.contains(index) || myEdge.contains(index));
	}
	
	private Icon getNeighbor(int index, int direction){								
						
		int var = index + direction;

		Icon icon = greenSquares[greenSqrIndex.indexOf(var)].getIcon();

		if(icon != null){
			
			String square = greenSquares[greenSqrIndex.indexOf(var)].getIcon().toString();
			
			Icon enemyPiece = null;
			Icon currPiece = null;
			
			if(gameMode.equals(TwoPlayer)){
				
				if(isBlacksTurn()){
					enemyPiece = whiteChip;
					currPiece = blackChip;
				}
				
				if(isWhitesTurn()){
					enemyPiece = blackChip;
					currPiece = whiteChip;
				}
				
			}else{
				
				enemyPiece = aiPiece;
				currPiece = humanPlayerPiece;
				
			}
			
			if(square.equals(enemyPiece.toString())){								
				return enemyPiece;
			}		
			
			if(square.equals(currPiece.toString())){
				return currPiece;
			}
			
			if(isKing(greenSqrIndex.indexOf(var))){
				
				if(gameMode.equals(TwoPlayer)){					
					if(isBlackKing(greenSqrIndex.indexOf(var)))
						return blackKing;
					if(isWhiteKing(greenSqrIndex.indexOf(var)))
						return whiteKing;
					
				}else	return humanPlayerPieceKing;
			}
		}		
		
		return null;		
	}	
	
	private void kingspathGlow(int index, Icon color){
						
		int[] right = {-9, 7}, left = {-7, 9};
		int[] kingsEdge = {7, 9}, startEdge = {-9, -7};
		int[] leftKings = {9}, rightStart = {-9};
		int[] mid = {-7, -9, 7, 9};
		int[] array;		
							
		if(rightEdge.contains(index)){
			
			if(myEdge.contains(index))
				array = rightStart;
			else 
				array = right;
		}
		else if(leftEdge.contains(index)){
			
			if(aiEdge.contains(index))
				array = leftKings;
			else 
				array = left;
		}
		else if(aiEdge.contains(index))
			array = kingsEdge;
		
		else if(myEdge.contains(index))			
			array = startEdge;
		
		else array = mid;
		
			
		for(int i = 0; i < array.length; i++){
						
			int indexOfn = greenSqrIndex.indexOf(index + array[i]);
			
			if(isEmpty(indexOfn)){
				
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
							
		int[] blackMid = {7, 9};
		int[] whiteMid = {-7, -9};
		int[] blackRight = {7}, whiteRight = {-9};
		int[] blackLeft =  {9}, whiteLeft = {-7};
		
		if(gameMode.equals(TwoPlayer)){
			
			if(isWhitesTurn()){
				mid = whiteMid;
				right = whiteRight;
				left = whiteLeft;
			}
			
			if(isBlacksTurn()){
				mid = blackMid;
				right = blackRight;
				left = blackLeft;
				
			}
			
		}else{
			
			mid = whiteMid;
			right = whiteRight;
			left = whiteLeft;
		}
		
		if(rightEdge.contains(index)){
			
			if(myEdge.contains(index))
				array = rightStart;
			else 
				array = right;
		}
		else if(leftEdge.contains(index))				
			array = left;
		
		else if(myEdge.contains(index))			
			array = startEdge;
		
		else array = mid;
		
			
		for(int i = 0; i < array.length; i++){
						
			int indexOfn = greenSqrIndex.indexOf(index + array[i]);
			
			if(isEmpty(indexOfn)){
				
				squarePanels[index + array[i]].setBackground(Color.CYAN);
			}						
			
		}		
	}		
	
	private void storePath(int indexOfSquarePath){		
				
		indexOfPath.add(indexOfSquarePath);		
	}
}
