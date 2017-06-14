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
	
	//Number of Pieces left of the players.
	private static int aiNumberOfPieces = 12;
	private static int humanNumberOfPieces = 12;
	
	private int[] aiPlayer_moves = new int[50000];
	private int aiMovesCount = 0; 
	
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
						
		// Initialize
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
		eatSquares.removeAll(eatSquares);
	}
	
	private boolean isKing(int square){
		return greenSquares[square].getIcon().toString().equals(humanPlayerPieceKing.toString());		
	}
	
	private boolean isNormalPiece(int square){
		return greenSquares[square].getIcon().toString().equals(humanPlayerPiece.toString());
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
		return turn;
	}
	
	private boolean isHumansTurn(){
		
		if(humanPlayerPiece.equals(whiteChip))
			return turn;
		
		return !turn;
	}
	
	private boolean isAiTurn(){
		
		if(aiPiece.equals(blackChip)){
			return !turn;
		}
		
		return turn;
	}
	
	public class MovementHandler extends MouseAdapter{
	
		public void mouseClicked(MouseEvent e){
			Object source = e.getSource();		
			
			if(gameMode.equals(OnePlayer)){
				HumanVsAi(source);
			}
			
			if(gameMode.equals(TwoPlayer)){
				
				//HumanVsHuman(source);
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
							movePiece(index);								
						
						}else{	//If the square was clicked once.
														
							if( isValid(index) ){
								//System.out.println("" + index + " is valid");
								markAsPath(index);								
							}
							else 
								resetAndClean();
						}
					
					// Clicked square is either a King or a normal piece.						
					}else if( isNormalPiece(i) || isKing(i) ){
												
						resetAndClean();	
						storePath(i);

						//Make path of chosen piece glow.						
						if(isKing(i))					
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
				
					//Make the path of the piece glow.
					storePath(i);
					
					if(isKing(i))													
						newPieceClick(humanPlayerPieceKing, currSquareIndex, index);							
					
					if(isNormalPiece(i))					
						newPieceClick(humanPlayerPiece, currSquareIndex, index);						
										
					indexOfClickedPath.add(index);	
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
			
			resetBoardColor();
		}		
		
	}
		
	private void movePiece(int index){		
						
		Icon piece = greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(0))].getIcon();
		
		// If piece is a king.
		if(piece.toString().equals(humanPlayerPieceKing.toString())){
		
			moveKing(index);
		
		// If piece is a normal piece.
		}else{						
			
			boolean isAtKingsEdge = false;
			if(aiEdge.contains(index)) isAtKingsEdge = true;		
					
			// If the move will make the piece a king.
			if(isAtKingsEdge){
				
				greenSquares[currSquareIndex].setIcon(humanPlayerPieceKing);	//Move piece and appoint it King.				
			
			// If the move will not make the piece a king.
			}else{								
				greenSquares[currSquareIndex].setIcon(humanPlayerPiece); 	//Move the piece.				
			}					
						
			greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(0))].setIcon(null);									
			
			resetBoardColor();									
			turn = !turn;																		
			prevChip = false;		
			
			
			//Eat an enemy piece.
			eatEnemyPiece();
						
		}

		//Clean path storage.
		
		indexOfClickedPath.removeAll(indexOfClickedPath);
		indexOfPath.removeAll(indexOfPath);
				
	}
	
	private void moveKing(int index){
		
		//Move king.
		greenSquares[currSquareIndex].setIcon(humanPlayerPieceKing);
		greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(0))].setIcon(null);
		
		//Reset and clean board.
		resetBoardColor();
												
		turn = !turn;																		
		prevChip = false;		
		
		eatKingsEnemyPiece();		
	}
	
	private void eatKingsEnemyPiece(){

		for(int i = 0 ; i + 1 < indexOfClickedPath.size(); i++){
			
			int var1 =  indexOfClickedPath.get(i);
			int var2 = indexOfClickedPath.get(i+1);			
			
			if(var1%9 == var2%9){
				
				if(var1 < var2){
					
					int index = var2;										
					while(true){
																		
						index -= 9; 						
						
						if(index == var1)
							break;

						if(greenSquares[greenSqrIndex.indexOf(index)].getIcon() == null 
								|| greenSquares[greenSqrIndex.indexOf(index)].getIcon().toString().equals(aiPiece.toString()))
							greenSquares[greenSqrIndex.indexOf(index)].setIcon(null);						
					}

				}else if(var1 > var2){
					
					int index = var2;
					
					while(true){
						
						index += 9; 						
						
						if(index == var1)
							break;

						if(greenSquares[greenSqrIndex.indexOf(index)].getIcon() == null 
								|| greenSquares[greenSqrIndex.indexOf(index)].getIcon().toString().equals(aiPiece.toString()))
							greenSquares[greenSqrIndex.indexOf(index)].setIcon(null);						
					}
										
				}
				
			}
			
			if(var1%7 == var2%7){
				
				if(var1 < var2){
					
					int index = var2;										
					while(true){
						
						index -= 7; 						
						
						if(index == var1)
							break;

						if(greenSquares[greenSqrIndex.indexOf(index)].getIcon() == null 
								|| greenSquares[greenSqrIndex.indexOf(index)].getIcon().toString().equals(aiPiece.toString()))
							greenSquares[greenSqrIndex.indexOf(index)].setIcon(null);						
					}

				
				}else if(var1 > var2){
					
					int index = var2;
					while(true){
						
						index += 7; 						
						
						if(index == var1)
							break;

						if(greenSquares[greenSqrIndex.indexOf(index)].getIcon() == null 
								|| greenSquares[greenSqrIndex.indexOf(index)].getIcon().toString().equals(aiPiece.toString()))
							greenSquares[greenSqrIndex.indexOf(index)].setIcon(null);						
					}
									
				}
			}

			aiNumberOfPieces--;
		}

	}
	
	private void eatEnemyPiece(){
		
		for(int i = 0 ; i + 1 < indexOfClickedPath.size(); i++){
			
			int var = (indexOfClickedPath.get(i+1) - indexOfClickedPath.get(i));					
			
			if(var < 0){
				
				if(var == -14)					
					greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) - 7)].setIcon(null);
				
				if(var == -18)
					greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) - 9)].setIcon(null);
			}
			
			if(var > 0){
				
				if(var == 14)					
					greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) + 7)].setIcon(null);
				
				if(var == 18)
					greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(i) + 9)].setIcon(null);
				
			}
			
			aiNumberOfPieces--;			
		}
		
	}
	
	private boolean isValid(int index){
					
		// If piece is King.
		int indexOf = greenSqrIndex.indexOf(indexOfClickedPath.get(0));
		
		if(isKing(indexOf)){
			
			int index2 = indexOfClickedPath.get(indexOfClickedPath.size()-1);			
			return (index%9 == index2%9 || index%7 == index2%7);												

		}else{						
			
			if(indexOfClickedPath.size() > 1){
								
				int nIndex1 = indexOfClickedPath.get(indexOfClickedPath.size()-1);					
				
				return ((nIndex1%9 == index%9 && Math.abs(nIndex1-index) == 18) || (nIndex1%7 == index%7 && 
						Math.abs(nIndex1-index) == 14));
				
			}else if(indexOfClickedPath.size() == 1){
												
				int nIndex1 = indexOfClickedPath.get(0);								
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
									
		if(chipColor.toString().equals(humanPlayerPieceKing.toString())){
			
			//Glow king's path.
			//kingspathGlowForward(currSquareIndex, index, chipColor, false);	
			checkForKingsFood(index);
			
		}else{
			
			pathGlowForward(currSquareIndex, index, chipColor);		
			checkForFood(index);			
			
		}
		
		indexOfPath.removeAll(indexOfPath);		
		prevChip = true;
	}
		
	private void checkForFood(int index){
		
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
				
		
		String aiPlayer = aiPiece.toString();		
		for(int i = 0; i < indices.length; i++){
			
			int index2 = index + indices[i];												
			if(index2 != indexOfPath.get(indexOfPath.size()-1)){
				
				Icon icon = getNeighbor(index, indices[i]);
				if(icon != null){
				
				String neighbor = icon.toString();
				
					if(neighbor.equals(aiPlayer) && !isAtEdges(index2)
						&& getNeighbor(index2, indices[i]) == null){
						
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
	
	private void checkForKingsFood(int index){		
		
		int[] indicesMid = { 7, -7, 9, -9};
		int[] indicesRight = { 7, -9}, indicesLeft = { -7, 9};
		int[] indicesEEdge = {7, 9}, indicesMEdge = {-7, -9};
		int[] indexRightEdge = {-9}, indexLeftEdge = {9};
		int[] indices;
		
		if(rightEdge.contains(index)){
			
			if(myEdge.contains(index)){
				//System.out.println("indexRightEdge");
				indices = indexRightEdge;
			}else{
				//System.out.println("indicesRight");
				indices = indicesRight;
			}
			
		}else if(leftEdge.contains(index)){
			
			if(aiEdge.contains(index)){
				System.out.println("indexLeftEdge");
				indices = indexLeftEdge;
			}else{
				System.out.println("indicesLeft");
				indices = indicesLeft;
			}
		}
		else if(aiEdge.contains(index)){	
			System.out.println("indicesEEdge");
			indices  = indicesEEdge;	
		
		}else if(myEdge.contains(index)){
			System.out.println("indicesMEdge");
			indices = indicesMEdge;
		}
		else{
			System.out.println("indicesMid");
			indices = indicesMid;
		}
				
		
		String aiPlayer = aiPiece.toString();		
		for(int i = 0; i < indices.length; i++){
			
			/*System.out.println("index: " + index +
					"indexOfPath: " +  indexOfPath.get(indexOfPath.size()-1) + 
					" || " + (index != indexOfPath.get(indexOfPath.size()-1)));*/
			
			
			int nIndex = index + indices[i];
			Icon icon = getNeighbor(index, indices[i]);				
			
			System.out.println("nIndex: " + nIndex + "\n Checking if in indexOfPath...");			
			if(!indexOfPath.contains(nIndex)){
				
				while(true){					
					
					if(icon != null){

						int nIndex2 = nIndex + indices[i];
						String neighbor = icon.toString();		
						
						//System.out.println("nIndex: " + nIndex + "  nIndex2: " + nIndex2);
						
						//System.out.println("neighbor.equals(aiPlayer): " + neighbor.equals(aiPlayer));
						//System.out.println("getNeighbor(nIndex + indices[i], indices[i]) == null: " + (getNeighbor(nIndex, indices[i]) == null));
						//System.out.println("nIndex: " + nIndex);
						
						if(neighbor.equals(aiPlayer) && !isAtEdges(nIndex)
								&& getNeighbor(nIndex, indices[i]) == null){
							
														
																
							squarePanels[nIndex].setBackground(Color.RED);
							squarePanels[nIndex2].setBackground(Color.CYAN);
							
							System.out.println("pass " + nIndex2);
							storePath(nIndex2);
							eatSquares.add(nIndex2);
							checkForKingsFood(nIndex2);
						}
					
						break;					
					}
						
					squarePanels[nIndex].setBackground(Color.CYAN);
					storePath(nIndex);
					
					if(isAtEdges(nIndex)) break;
					
					icon = getNeighbor(nIndex, indices[i]);
					nIndex += indices[i];
					storePath(nIndex);
					
				}
			}
			
		}
		
		
	}
	
	private boolean isAtEdges(int index){		
		return (rightEdge.contains(index) || leftEdge.contains(index) || aiEdge.contains(index));
	}
	
	private Icon getNeighbor(int index, int direction){								
		
		int var = index + direction;		
		Icon icon = greenSquares[greenSqrIndex.indexOf(var)].getIcon();

		//System.out.println("var: " + var + " index: " + index + " direction: " + direction);
		if(icon != null){
			
			String square = greenSquares[greenSqrIndex.indexOf(var)].getIcon().toString();
			String ai = aiPiece.toString();
			String human = humanPlayerPiece.toString();
			
			if(square.equals(ai)){								
					return aiPiece;
			}		
			
			if(square.equals(human)){
				return humanPlayerPiece;
			}
			
			if(isKing(greenSqrIndex.indexOf(var))){
				return humanPlayerPieceKing;
			}
		}		
		
		return null;		
	}	
	
	private void kingspathGlowForward(int currSquareIndex, int index, Icon color, boolean inRecur){
		
		if((index - 9 < 0 || index - 7 < 0 ) && (index + 9 > 64 || index + 7 > 64 )){ 
			return;
		}
			
		boolean isLeftEdge = false, isRightEdge = false;
		
		if(rightEdge.contains(index))	isRightEdge = true; 		
		else if(leftEdge.contains(index)) isLeftEdge = true;
		
					
		if(isRightEdge){

				if(index - 9 > 0){
					
					int nIndex1 = index - 9;
					int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
					
					boolean lEdge = false;
					while(nIndex1 > 0 && greenSquares[indexOfn1].getIcon() == null){
																		
						squarePanels[nIndex1].setBackground(Color.CYAN);
						
						if(leftEdge.contains(nIndex1)){
							lEdge = true;
							break;
						}
						
						nIndex1 -= 9;
						indexOfn1 = greenSqrIndex.indexOf(nIndex1);
						
					}															
					
					if( nIndex1 > 0 && !lEdge && greenSquares[indexOfn1].getIcon() != null && 
							greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){

						boolean isLEdge = false;																											
							if(leftEdge.contains(nIndex1))	isLEdge = true;
						
						
						if(!isLEdge && (nIndex1 - 9) > 0){
												
							if(greenSquares[greenSqrIndex.indexOf(nIndex1 - 9)].getIcon() == null){
								
								squarePanels[nIndex1].setBackground(Color.RED);
								squarePanels[nIndex1 - 9].setBackground(Color.CYAN);
																		
								storePath(nIndex1 - 9);							
								kingspathGlowForward(greenSqrIndex.indexOf(nIndex1 - 9), nIndex1 - 9, color, true);
							}
							
						}									
						
					}
				}
				
				if(index + 7 < 64 ){
					
					int nIndex1 = index + 7;
					int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
					
					boolean lEdge = false;
					while(nIndex1 <  64 && greenSquares[indexOfn1].getIcon() == null){
												
						squarePanels[nIndex1].setBackground(Color.CYAN);
						
						if(leftEdge.contains(nIndex1)){
							lEdge = true;
							break;
						}
						
						nIndex1 += 7;
						indexOfn1 = greenSqrIndex.indexOf(nIndex1);
					}
					
					if(nIndex1 <  64 && !lEdge && greenSquares[indexOfn1].getIcon() != null && 
							greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
																	
						boolean isLEdge = false;																											
							if(leftEdge.contains(nIndex1))	isLEdge = true;
						
						
						if(!isLEdge && (nIndex1 + 7) > 0){
												
							if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 7)].getIcon() == null){
								
								squarePanels[nIndex1].setBackground(Color.RED);
								squarePanels[nIndex1 + 7].setBackground(Color.CYAN);
																		
								storePath(nIndex1 + 7);							
								kingspathGlowForward(greenSqrIndex.indexOf(nIndex1 + 7), nIndex1 + 7, color, true);
							}
							
						}									
						
					}
				}
													
		}else if(isLeftEdge){
								
			if(index - 7 > 0){
				
				if(indexOfPath.isEmpty() || (!(index%7 == indexOfPath.get(indexOfPath.size()-1)%7)
						&& index < indexOfPath.get(indexOfPath.size()-1))){
					
					int nIndex1 = index - 7;
					int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
					
					boolean rEdge = false;
					
					while(nIndex1 > 0 && greenSquares[indexOfn1].getIcon() == null){
						
						squarePanels[nIndex1].setBackground(Color.CYAN);
						
						if(rightEdge.contains(nIndex1)){
							rEdge = true;
							break;
						}
						
						nIndex1 -= 7;
						indexOfn1 = greenSqrIndex.indexOf(nIndex1);
						
					}	

					if(nIndex1 > 0 && !rEdge && greenSquares[indexOfn1].getIcon() != null && 
							greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){

						boolean isREdge = false;																											
							if(rightEdge.contains(nIndex1))	isREdge = true;
						
						
						if(!isREdge && (nIndex1 - 7) > 0){
												
							if(greenSquares[greenSqrIndex.indexOf(nIndex1 - 7)].getIcon() == null){
								
								squarePanels[nIndex1].setBackground(Color.RED);
								squarePanels[nIndex1 -7].setBackground(Color.CYAN);
																		
								storePath(nIndex1 - 7);							
								kingspathGlowForward(greenSqrIndex.indexOf(nIndex1 - 7), nIndex1 - 7, color, true);
							}
							
						}									
						
					}

					
				}
			}
			
			if(index + 9 < 64 ){
				
				int nIndex1 = index + 9;
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
				
				boolean rEdge = false;
				while(nIndex1 < 64 && greenSquares[indexOfn1].getIcon() == null){
											
					squarePanels[nIndex1].setBackground(Color.CYAN);
					
					if(rightEdge.contains(nIndex1)){
						rEdge = true;
						break;
					}
					
					nIndex1 += 9;
					indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				}
				
				if(nIndex1 < 64 && !rEdge && greenSquares[indexOfn1].getIcon() != null && 
						greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
																
					boolean isREdge = false;																											
						if(rightEdge.contains(nIndex1))	isREdge = true;
					
					
					if(!isREdge && (nIndex1 + 9) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 9)].getIcon() == null){
							
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 + 9].setBackground(Color.CYAN);
																	
							storePath(nIndex1 + 9);							
							kingspathGlowForward(greenSqrIndex.indexOf(nIndex1 + 9), nIndex1 + 9, color, true);
						}
						
					}									
					
				}
			}
				
		}else{
								
				int nIndex1 = index - 9;
				int nIndex2 = index - 7;															
									
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				int indexOfn2 = greenSqrIndex.indexOf(nIndex2);								
				
				// Towards left diagonal up. 
				if(indexOfPath.isEmpty() || (!(index % 9 == indexOfPath.get(indexOfPath.size()-1)%9)
						&& nIndex1 < indexOfPath.get(indexOfPath.size()-1))){
				
					boolean lEdge = false;					
					while( nIndex1 > 0 && isEmpty(indexOfn1) ){
												
						squarePanels[nIndex1].setBackground(Color.CYAN);
						
						if(leftEdge.contains(nIndex1)){
							lEdge = true;
							break;
						}							
						
						nIndex1 -= 9;
						indexOfn1 = greenSqrIndex.indexOf(nIndex1);
																								
					}					
															
					if(nIndex1 > 0 && !lEdge && greenSquares[indexOfn1].getIcon() != null && 
							greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
						
						boolean isLEdge = false;																	
						if(leftEdge.contains(nIndex1)) isLEdge = true;														
						
						if(!isLEdge && (nIndex1 - 9) > 0){
												
							if(greenSquares[greenSqrIndex.indexOf(nIndex1 - 9)].getIcon() == null){
								squarePanels[nIndex1].setBackground(Color.RED);
								squarePanels[nIndex1 - 9].setBackground(Color.CYAN);
								
								storePath(nIndex1 - 9);	
								kingspathGlowForward(greenSqrIndex.indexOf(nIndex1 - 9), nIndex1 - 9, color, true);
							}
							
						}									
						
					}
				}								
				
				// Towards right diagonal up.
				if(indexOfPath.isEmpty() || (!(index%7 == indexOfPath.get(indexOfPath.size()-1)%7)
						&& nIndex2 < indexOfPath.get(indexOfPath.size()-1))){
					
					boolean rEdge = false;			
					
					while(nIndex2 > 0 && greenSquares[indexOfn2].getIcon() == null){												
						
						squarePanels[nIndex2].setBackground(Color.CYAN);
						
						if(rightEdge.contains(nIndex2)){
							rEdge = true;
							break;
						}
						
						if(nIndex2 - 7 < 0) break;
						
						nIndex2 -= 7;
						indexOfn2 = greenSqrIndex.indexOf(nIndex2);
					}				
																
					if(nIndex2 > 0 && !rEdge && greenSquares[indexOfn2].getIcon() != null && 
							greenSquares[indexOfn2].getIcon().toString().equals(aiPiece.toString())){						
						
						boolean isREdge = false;									
						if(rightEdge.contains(nIndex2)) isREdge = true;						
								
						
						if(!isREdge && (nIndex2 - 7) > 0){															
							
							if(greenSquares[greenSqrIndex.indexOf(nIndex2 - 7)].getIcon() == null){						
								squarePanels[nIndex2].setBackground(Color.RED);
								squarePanels[nIndex2 - 7].setBackground(Color.CYAN);
								
								storePath(nIndex2 - 7);	
								kingspathGlowForward(greenSqrIndex.indexOf(nIndex2 - 7), nIndex2 - 7, color, true);							
							}
						}
						
					}
					
				}
				
				// Towards Down.				
				nIndex1 = index + 9;
				nIndex2 = index + 7;															
									
				indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				indexOfn2 = greenSqrIndex.indexOf(nIndex2);																	
				
				// Towards right diagonal down. 
				if(indexOfPath.isEmpty() || (!(index%9 == indexOfPath.get(indexOfPath.size()-1)%9)
						&& nIndex1 > indexOfPath.get(indexOfPath.size()-1))){

					boolean rEdge = false;					
					while(nIndex1 < 64 && greenSquares[indexOfn1].getIcon() == null){
												
						squarePanels[nIndex1].setBackground(Color.CYAN);
						
						if(rightEdge.contains(nIndex1)){
							rEdge = true;
							break;
						}							
						
						nIndex1 += 9;
						indexOfn1 = greenSqrIndex.indexOf(nIndex1);
																								
					}					
					
					if(nIndex1 < 64 && !rEdge && greenSquares[indexOfn1].getIcon() != null && 
							greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){												
						
						boolean isREdge = false;																	
						if(rightEdge.contains(nIndex1)) isREdge = true;														
						
						if(!isREdge && (nIndex1 + 9) < 64){
												
							if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 9)].getIcon() == null){
								squarePanels[nIndex1].setBackground(Color.RED);
								squarePanels[nIndex1 + 9].setBackground(Color.CYAN);
								
								storePath(nIndex1 + 9);	
								kingspathGlowForward(greenSqrIndex.indexOf(nIndex1 + 9), nIndex1 + 9, color, true);
							}
							
						}									
						
					}						
				}							
				
				// Towards left diagonal down.
				if(indexOfPath.isEmpty() || (!(index%7 == indexOfPath.get(indexOfPath.size()-1)%7)
						&& nIndex2 > indexOfPath.get(indexOfPath.size()-1))){
					
					boolean lEdge = false;
					
					while(nIndex2 < 64 && greenSquares[indexOfn2].getIcon() == null){												
						
						squarePanels[nIndex2].setBackground(Color.CYAN);
						
						if(leftEdge.contains(nIndex2)){
							lEdge = true;
							break;
						}
						
						nIndex2 += 7;
						indexOfn2 = greenSqrIndex.indexOf(nIndex2);
					}

					if(nIndex2 < 64 && !lEdge && greenSquares[indexOfn2].getIcon() != null && 
							greenSquares[indexOfn2].getIcon().toString().equals(aiPiece.toString())){						
						
						boolean isLEdge = false;									
						if(leftEdge.contains(nIndex2)) isLEdge = true;						
								
						
						if(!isLEdge && (nIndex2 + 7) > 0){
							
							if(greenSquares[greenSqrIndex.indexOf(nIndex2 + 7)].getIcon() == null){						
								squarePanels[nIndex2].setBackground(Color.RED);
								squarePanels[nIndex2 + 7].setBackground(Color.CYAN);
								
								storePath(nIndex2 + 7);	
								kingspathGlowForward(greenSqrIndex.indexOf(nIndex2 + 7), nIndex2+7, color, true);							
							}
						}
						
					}						
				}
								
		}
	}
	
	public void pathGlowForward(int currSquareIndex, int index, Icon color){			
				
		if(index - 9 < 0 || index - 7 < 0 ){ 
			return;
		}
			
		boolean isLeftEdge = false, isRightEdge = false;
							
		if(rightEdge.contains(index)) isRightEdge = true; 						
		else if(leftEdge.contains(index)) isLeftEdge = true;
									
			if(isRightEdge){

				int nIndex = index - 9;
				int indexOfn = greenSqrIndex.indexOf(nIndex);		
				
				if(isEmpty(indexOfn)){
										
					squarePanels[nIndex].setBackground(Color.CYAN);
				}																				
																	
			}			
			else if(isLeftEdge){
				
				int nIndex = index - 7;
				int indexOfn = greenSqrIndex.indexOf(nIndex);
				
				if(isEmpty(indexOfn)){
										
					squarePanels[nIndex].setBackground(Color.CYAN);
				}													
			}
			else{
				
				int nIndex1 = index - 9;
				int nIndex2 = index - 7;															
								
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				int indexOfn2 = greenSqrIndex.indexOf(nIndex2);
								
				
				if(isEmpty(indexOfn1)){									
					squarePanels[nIndex1].setBackground(Color.CYAN);
				}									
				
				if(isEmpty(indexOfn2)){										
					squarePanels[nIndex2].setBackground(Color.CYAN);
				}				
			}
	}
	
	public void pathGlowBackward(int currSquareIndex, int index, Icon color){							
		
		if(index + 9 > 64 || index + 7 > 64 ){ 
			return;
		}
				
		
		boolean isLeftEdge = false, isRightEdge = false;
		
		if(rightEdge.contains(index)) isRightEdge = true; 						
		else if(leftEdge.contains(index)) isLeftEdge = true;
		
		if(color.toString().equals(humanPlayerPieceKing.toString())){
			
			if(isRightEdge){
				
				int nIndex1 = index + 7;				
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		

				if(greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isREdge = false;												
					if(rightEdge.contains(nIndex1)) isREdge = true;												
					
					if(!isREdge && (nIndex1 + 7) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 7)].getIcon() == null){
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 + 7].setBackground(Color.CYAN);
																								
							
							pathGlowBackward(greenSqrIndex.indexOf(nIndex1 + 7), nIndex1 + 7, humanPlayerPieceKing);
						}
						
					}									
					
				}
			}			
			else if(isLeftEdge){
				
				int nIndex = index + 9;
				int indexOfn = greenSqrIndex.indexOf(nIndex);
				
				
				if(greenSquares[indexOfn].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isLEdge = false;																
					if(leftEdge.contains(nIndex)) isLEdge = true;
					
					if(!isLEdge && (nIndex + 9) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex + 9)].getIcon() == null){
							squarePanels[nIndex].setBackground(Color.RED);
							squarePanels[nIndex + 9].setBackground(Color.CYAN);
							
							pathGlowBackward(greenSqrIndex.indexOf(nIndex + 9), nIndex + 9, humanPlayerPieceKing);
						}
						
					}									
					
				}
			}
			else{
				
				int nIndex1 = index + 9;
				int nIndex2 = index + 7;															
								
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				int indexOfn2 = greenSqrIndex.indexOf(nIndex2);
								
				if(greenSquares[indexOfn1].getIcon() == null || greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString()))
				{
										
					boolean isREdge = false;															
					if(rightEdge.contains(nIndex1)) isREdge = true;							
					
					if(!isREdge && (nIndex1 + 9) < 64){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 9)].getIcon() == null){
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 + 9].setBackground(Color.CYAN);
							
							pathGlowBackward(greenSqrIndex.indexOf(nIndex1 + 9), nIndex1 + 9, humanPlayerPieceKing);
						}
						
					}									
					
				}
															
				if(greenSquares[indexOfn2].getIcon() == null || greenSquares[indexOfn2].getIcon().toString().equals(aiPiece.toString()))
				{
						
						boolean isLEdge = false;									
						if(leftEdge.contains(nIndex2)) isLEdge = true;																
						
						if(!isLEdge && (nIndex2 + 7) < 64 ){
							
							if(greenSquares[greenSqrIndex.indexOf(nIndex2 + 7)].getIcon() == null){						
								squarePanels[nIndex2].setBackground(Color.RED);
								squarePanels[nIndex2 + 7].setBackground(Color.CYAN);
								
								pathGlowBackward(greenSqrIndex.indexOf(nIndex2 + 7), nIndex2 + 7, humanPlayerPieceKing);							
							}
						}

				}				

			}
			
		}else{
			
			if(isRightEdge){
				
				int nIndex1 = index + 7;				
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		

				if(greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isREdge = false;																
					if(rightEdge.contains(nIndex1)) isREdge = true;
					
					if(!isREdge && (nIndex1 + 7) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 7)].getIcon() == null){
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 + 7].setBackground(Color.CYAN);
																								
							
							pathGlowBackward(greenSqrIndex.indexOf(nIndex1 + 7), nIndex1 + 7, humanPlayerPiece);
						}
						
					}									
					
				}
			}			
			else if(isLeftEdge){
				
				int nIndex = index + 9;
				int indexOfn = greenSqrIndex.indexOf(nIndex);
				
				
				if(greenSquares[indexOfn].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isLEdge = false;
					if(leftEdge.contains(nIndex)) isLEdge = true;							
					
					if(!isLEdge && (nIndex + 9) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex + 9)].getIcon() == null){
							squarePanels[nIndex].setBackground(Color.RED);
							squarePanels[nIndex + 9].setBackground(Color.CYAN);
							
							pathGlowBackward(greenSqrIndex.indexOf(nIndex + 9), nIndex + 9, humanPlayerPiece);
						}
						
					}									
					
				}
			}
			else{
				
				int nIndex1 = index + 9;
				int nIndex2 = index + 7;															
								
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				int indexOfn2 = greenSqrIndex.indexOf(nIndex2);
								
				if(greenSquares[indexOfn1].getIcon() != null && greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString()))
				{
										
					boolean isREdge = false;
					if(rightEdge.contains(nIndex1)) isREdge = true;
					
					if(!isREdge && (nIndex1 + 9) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 9)].getIcon() == null){
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 + 9].setBackground(Color.CYAN);
							
							pathGlowBackward(greenSqrIndex.indexOf(nIndex1 + 9), nIndex1 + 9, humanPlayerPiece);
						}
						
					}									
					
				}
															
				if(greenSquares[indexOfn2].getIcon() != null && 
						greenSquares[indexOfn2].getIcon().toString().equals(aiPiece.toString())){
						
						boolean isLEdge = false;
						if(leftEdge.contains(nIndex2)) isLEdge = true;								
						
						if(!isLEdge && (nIndex2 + 7) < 64 ){
							
							if(greenSquares[greenSqrIndex.indexOf(nIndex2 + 7)].getIcon() == null){						
								squarePanels[nIndex2].setBackground(Color.RED);
								squarePanels[nIndex2 + 7].setBackground(Color.CYAN);
								
								pathGlowBackward(greenSqrIndex.indexOf(nIndex2 + 7), nIndex2 + 7, humanPlayerPiece);							
							}
						}

				}				

			}
			
		}
	}			
	
	private void storePath(int indexOfSquarePath){		
				
		indexOfPath.add(indexOfSquarePath);		
	}
}
