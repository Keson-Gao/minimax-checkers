import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoardTwoPlayer extends JPanel{
	private static final long serialVersionUID = 1L;

	private JPanel board = new JPanel(new GridLayout(8, 8, 2, 2));
	private JPanel[] squarePanels = new JPanel[64];	
	private JLabel[] greenSquares = new JLabel[32];				
	
	private Icon whiteChip = new ImageIcon(getClass().getResource("white.png"));
	private Icon whiteKing = new ImageIcon(getClass().getResource("whiteKing.png"));	
	private Icon blackChip = new ImageIcon(getClass().getResource("black.png"));
	private Icon blackKing = new ImageIcon(getClass().getResource("blackKing.png"));	
	private Icon dottedWhite = new ImageIcon(getClass().getResource("eatpathwhite.png"));		
	private static int blackCount = 12, whiteCount = 12, blackKingNum = 0, whiteKingNum = 0, whiteMoves = 0, blackMoves = 0;  
	
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

	public BoardTwoPlayer(){				
				
		setOpaque(false);
		setLayout(null);
		setBounds(0, 0, 1000, 700);						
		board.setBackground(Color.BLACK);
		board.setBounds(160, 46, 470, 470);			
		MovementHandler handler = new MovementHandler();		
		int tog = 0;		
		turn = true;
		
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
		
		placePieces();						
	}				
	
	private void placePieces(){		
		for(int j = 0; j < 32; j++){			
			greenSquares[j] =  new JLabel();				
			squarePanels[indexOfGreenSquare.get(j)].add(greenSquares[j], BorderLayout.CENTER);						
			if(indexOfGreenSquare.get(j) >= 0 && indexOfGreenSquare.get(j) <= 22)
				greenSquares[j].setIcon(blackChip);
			if(indexOfGreenSquare.get(j) >= 41 && indexOfGreenSquare.get(j) <= 63)
				greenSquares[j].setIcon(whiteChip);			
		}					
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

	private boolean isKing(int squareIndex){				
		return (getPieceAt(squareIndex) == PieceColor.BLACK_KING || getPieceAt(squareIndex) == PieceColor.WHITE_KING); 					
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
	
	private boolean isWhiteKing(int squareIndex){
		return (getPieceAt(squareIndex) == PieceColor.WHITE_KING);
	}
	
	private boolean isBlackKing(int squareIndex){
		return (getPieceAt(squareIndex) == PieceColor.BLACK_KING);
	}
	
	private boolean isNormalPiece(int squareIndex){				
		return (getPieceAt(squareIndex) == PieceColor.BLACK || getPieceAt(squareIndex) == PieceColor.WHITE);					
	}
	
	private boolean cellIsEmpty(int squareIndex){		
		return getPieceAt(squareIndex) == PieceColor.EMPTY;
	}	
	private boolean isPath(int squareIndex){
		return getPieceAt(squareIndex) == PieceColor.MARKED_PATH;
	}
		
	private boolean aPieceWasPreviouslyChosen(){ return prevChip; }	
	private void setAPieceWasChosen(boolean bool){ prevChip = bool; }	
	private boolean isWhitesTurn(){ return turn; }	
	private boolean isBlacksTurn(){ return !turn; }
		
	private boolean toCrownAsKingBlack(){		
		for(int i: indexOfClickedPath){					
			if(bottomEdge.contains(i))
				return true;
		}		
		return false;		
	}
	
	private boolean toCrownAsKingWhite(){		
		for(int i: indexOfClickedPath){					
			if(upperEdge.contains(i))
				return true;
		}		
		return false;
	}
	
	private boolean isWhitePiece(int squareIndex){
		return (getPieceAt(squareIndex) == PieceColor.WHITE);
	}
	
	private boolean isBlackPiece(int squareIndex){
		return (getPieceAt(squareIndex) == PieceColor.BLACK);
	}
	
	public class MovementHandler extends MouseAdapter{	
		public void mouseClicked(MouseEvent e){
			Object source = e.getSource();								
			HumanVsHuman(source);
		}
	}	
		
	private void HumanVsHuman(Object source){		
		int newIndex = 0;		
		boolean isGreenSquare = false;				
		for(int i = 0; i < 32; i++){			
			if(source == squarePanels[indexOfGreenSquare.get(i)]){				
				currIndex = i;
				newIndex = indexOfGreenSquare.get(i);
				isGreenSquare = true;
				break;
			}								
		}

		if(isGreenSquare){			 
			if(isWhitesTurn()){												
				if(aPieceWasPreviouslyChosen()){																								
					if( cellIsEmpty(currIndex) || isPath(currIndex) ){														
						if(cellIsClickedTwice(currIndex)){																		
							eraseDottedLines();								
							++whiteMoves;							
							movePiece(currIndex);														
						
						}else{																		
							if( isAValidMoveAt(newIndex) )								
								markAsPath(newIndex);															
							else 
								clearBoard();
						}										
					}else if( isWhitePiece(currIndex) || isWhiteKing(currIndex) ){											
						clearBoard();	
						storePath(currIndex);
						
						if(isWhiteKing(currIndex))					
							newPieceClick(whiteKing, currIndex, newIndex);						
						if(isWhitePiece(currIndex))
							newPieceClick(whiteChip, currIndex, newIndex);												
						indexOfClickedPath.add(newIndex);
						
					}else if(isBlackPiece(currIndex)){						
						clearBoard();						
					}else{						
						clearBoard();						
					}
				}
											
				else if( !cellIsEmpty(currIndex) && !aPieceWasPreviouslyChosen()){				
					if(isWhitePiece(currIndex) || isWhiteKing(currIndex)){						
						storePath(currIndex);						
						if(isWhiteKing(currIndex))													
							newPieceClick(whiteKing, currIndex, newIndex);													
						if(isWhitePiece(currIndex))					
							newPieceClick(whiteChip, currIndex, newIndex);																	
						indexOfClickedPath.add(newIndex);						
					}						
				}														
												
			}else if(isBlacksTurn()){											
				if(aPieceWasPreviouslyChosen()){																								
					if( cellIsEmpty(currIndex) || isPath(currIndex) ){													
						if(cellIsClickedTwice(currIndex)){																		
							eraseDottedLines();							
							++blackMoves;							
							movePiece(currIndex);														
						
						}else{	
																		
							if( isAValidMoveAt(newIndex) )								
								markAsPath(newIndex);															
							else 
								clearBoard();
						}
											
					}else if( isBlackPiece(currIndex) || isBlackKing(currIndex) ){												
						clearBoard();	
						storePath(currIndex);

						if(isBlackKing(currIndex))					
							newPieceClick(blackKing, currIndex, newIndex);						
						if(isBlackPiece(currIndex))
							newPieceClick(blackChip, currIndex, newIndex);												
						indexOfClickedPath.add(newIndex);
										
					}else if(isBlackPiece(currIndex)){													
						clearBoard();	
					}else{						
						clearBoard();
					}
				}											
				else if( !cellIsEmpty(currIndex) && !aPieceWasPreviouslyChosen()){				
					if(isBlackPiece(currIndex) || isBlackKing(currIndex)){
						storePath(currIndex);
						
						if(isBlackKing(currIndex))													
							newPieceClick(blackKing, currIndex, newIndex);													
						if(isBlackPiece(currIndex))					
							newPieceClick(blackChip, currIndex, newIndex);						
											
						indexOfClickedPath.add(newIndex);						
					}						
				}		
			}				
		}else clearBoard();		
	}
	
	private void movePiece(int newIndex){		
										
		int neighbor = indexOfGreenSquare.indexOf(indexOfClickedPath.get(0));				
		if(isKing(neighbor))		
			moveKing(newIndex);		
		
		else if(isNormalPiece(neighbor)){			
			if(toCrownAsKingBlack() || toCrownAsKingWhite()){				
				if(isBlacksTurn()){						
					if(toCrownAsKingBlack()) 
						greenSquares[newIndex].setIcon(blackKing);
					else 
						greenSquares[newIndex].setIcon(blackChip);
				
				}else if(isWhitesTurn()){											
					if(toCrownAsKingWhite()) 
						greenSquares[newIndex].setIcon(whiteKing);
					else 
						greenSquares[newIndex].setIcon(whiteChip);					
				}
				
			}else{				
				if(isBlacksTurn())
					greenSquares[newIndex].setIcon(blackChip);										
				if(isWhitesTurn())
					greenSquares[newIndex].setIcon(whiteChip);
				
			}						
			greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(0))].setIcon(null);												
			resetBoardColor();
			jump(newIndex);										
			changeTurn();
			setAPieceWasChosen(false);
			
			if(turn)
				CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource("whiteturn.png")));
			else
				CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource("blackturn.png")));
		
		}
		
		indexOfClickedPath.removeAll(indexOfClickedPath);
		indexOfPathTaken.removeAll(indexOfPathTaken);			
	}
	
	private void changeTurn() {
		turn = !turn;		
	}

	private void moveKing(int newIndex){												
		if(isBlacksTurn())														
			greenSquares[currIndex].setIcon(blackKing);			
		else if(isWhitesTurn())
			greenSquares[currIndex].setIcon(whiteKing);		
		greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(0))].setIcon(null);
		
		resetBoardColor();
		jump(newIndex);	
		changeTurn();																	
		setAPieceWasChosen(false);	
		
		if(turn)
			CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource("whiteturn.png")));
		else
			CheckerFrame.playerTurn.setIcon(new ImageIcon(getClass().getResource("blackturn.png")));
	}		
	
	private void jump(int targetIndex){		
		
		for(int i = 0 ; i + 1 < indexOfClickedPath.size(); i++){						
			int var = (indexOfClickedPath.get(i+1) - indexOfClickedPath.get(i));
			
			if(var < 0){				
				if(var == -14){					
															
					if(getPieceAt(indexOfGreenSquare.indexOf(indexOfClickedPath.get(i) - 7)) != PieceColor.EMPTY){						
						if(isWhitesTurn())
							blackCount--;							
						else if(isBlacksTurn())
							whiteCount--;																			
					}					
					greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(i) - 7)].setIcon(null);				
				}				
				if(var == -18){					
					Icon piece = greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(i) - 9)].getIcon();										
					if(piece != null){						
						if(isWhitesTurn())
							blackCount--;
						else if(isBlacksTurn())
							whiteCount--;												
					}					
					greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(i) - 9)].setIcon(null);
				}
			}
			
			if(var > 0){				
				if(var == 14){					
					Icon piece = greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(i) + 7)].getIcon();										
					if(piece != null){						
						if(isWhitesTurn())
							blackCount--;
						else if(isBlacksTurn())
							whiteCount--;						
					}					
					greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(i) + 7)].setIcon(null);				
				}				
				if(var == 18){					
					Icon piece = greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(i) + 9)].getIcon();										
					if(piece != null){						
						if(isWhitesTurn())
							blackCount--;													
						else if(isBlacksTurn())
							whiteCount--;						
					}					
					greenSquares[indexOfGreenSquare.indexOf(indexOfClickedPath.get(i) + 9)].setIcon(null);				
				}				
			}			
			if(indexOfGreenSquare.get(targetIndex) == indexOfClickedPath.get(i+1)) break;
		}						
		
		if(blackCount == 0 || whiteCount == 0){
			CheckerFrame.gameOver(new GameOver(), blackCount, whiteCount, blackMoves, whiteMoves, true);
		}		
	}
	
	private boolean isAValidMoveAt(int index){			
		int indexOf = indexOfGreenSquare.indexOf(indexOfClickedPath.get(0));		
		if(isKing(indexOf)){						
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
				
				if(isBlacksTurn()){						
					return ((nIndex1%9 == index%9 && ((index - nIndex1) == 9 || 
							(Math.abs(nIndex1-index) == 18 && foodSquares.contains(index)))) 
						|| (nIndex1%7 == index%7 && ((index - nIndex1) == 7 || 
							(Math.abs(nIndex1-index) == 14 && foodSquares.contains(index)))));						
				}					
				if(isWhitesTurn()){					
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
			checkForKingsFood(index);			
		}else{			
			pathGlow(index, chipColor);		
			checkForFood(index);						
		}		
		indexOfPathTaken.removeAll(indexOfPathTaken);		
		prevChip = true;
	}
		
	private void checkForFood(int index){			
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
		
		String enemyPlayer = null;
		String enemyPlayerKing = null;		
		
		if(isBlacksTurn()){			
			enemyPlayer = whiteChip.toString();
			enemyPlayerKing = whiteKing.toString();				
		}else if(isWhitesTurn()){
			enemyPlayer = blackChip.toString();
			enemyPlayerKing = blackKing.toString();				
		}						
		
		for(int i = 0; i < indices.length; i++){			
			int index2 = index + indices[i];						
			if(indexOfGreenSquare.indexOf(index2) != indexOfPathTaken.get(indexOfPathTaken.size()-1)){												
				Icon icon = getNeighbor(index, indices[i]);
				if(icon != null){
				
				String neighbor = icon.toString();				
					if((neighbor.equals(enemyPlayer) || neighbor.equals(enemyPlayerKing)) 
							&& !isAtEdges(index2) && getNeighbor(index2, indices[i]) == null){											
						squarePanels[index2].setBackground(Color.RED);
						squarePanels[index2 + indices[i]].setBackground(Color.CYAN);															
						storePath(indexOfGreenSquare.indexOf(index2));
						foodSquares.add(index2 + indices[i]);
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
			if(bottomEdge.contains(index))
				indices = indexRightEdge;
			else indices = indicesRight;						
		}else if(leftEdge.contains(index)){			
			if(upperEdge.contains(index))				
				indices = indexLeftEdge;
			else indices = indicesLeft;			
		}else if(upperEdge.contains(index))			
			indices  = indicesEEdge;			
		else if(bottomEdge.contains(index))
			indices = indicesMEdge;		
		else indices = indicesMid;			
				
		String enemyPlayer = null;
		String enemyPlayerKing = null;		
		
		if(isBlacksTurn()){			
			enemyPlayerKing = whiteKing.toString();
			enemyPlayer = whiteChip.toString();			
		}else if(isWhitesTurn()){
			enemyPlayerKing = blackKing.toString();
			enemyPlayer = blackChip.toString();
		}		
					
		for(int i = 0; i < indices.length; i++){
			
			int index2 = index + indices[i];												
			if(indexOfGreenSquare.indexOf(index2) != indexOfPathTaken.get(indexOfPathTaken.size()-1)){								
				Icon icon = getNeighbor(index, indices[i]);
				if(icon != null){
					String neighbor = icon.toString();								
					if((neighbor.equals(enemyPlayer) || neighbor.equals(enemyPlayerKing)) 
							&& !isAtEdges(index2) && getNeighbor(index2, indices[i]) == null)
					{																	
						squarePanels[index2].setBackground(Color.RED);
						squarePanels[index2 + indices[i]].setBackground(Color.CYAN);						
						storePath(indexOfGreenSquare.indexOf(index2));
						foodSquares.add(index2 + indices[i]);
						checkForFood(index2 + indices[i]);
					}				
				}
			}
		}
	}
	
	private boolean isAtEdges(int index){		
		return (rightEdge.contains(index) || leftEdge.contains(index) || upperEdge.contains(index) || bottomEdge.contains(index));
	}
	
	private Icon getNeighbor(int index, int direction){														
		int var = index + direction;
		Icon icon = greenSquares[indexOfGreenSquare.indexOf(var)].getIcon();
		
		if(icon != null){			
			String square = greenSquares[indexOfGreenSquare.indexOf(var)].getIcon().toString();		
			Icon enemyPiece = null;
			Icon currPiece = null;	
			
			if(isBlacksTurn()){
				enemyPiece = whiteChip;
				currPiece = blackChip;
			}				
			if(isWhitesTurn()){
				enemyPiece = blackChip;
				currPiece = whiteChip;
			}
			
			if(square.equals(enemyPiece.toString())){								
				return enemyPiece;
			}					
			if(square.equals(currPiece.toString())){
				return currPiece;
			}			
			if(isKing(indexOfGreenSquare.indexOf(var))){			
				if(isBlackKing(indexOfGreenSquare.indexOf(var)))
					return blackKing;
				if(isWhiteKing(indexOfGreenSquare.indexOf(var)))
					return whiteKing;			
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
		int[] blackMid = {7, 9};
		int[] whiteMid = {-7, -9};
		int[] blackRight = {7}, whiteRight = {-9};
		int[] blackLeft =  {9}, whiteLeft = {-7};
		
		if(isWhitesTurn()){
			mid = whiteMid;
			right = whiteRight;
			left = whiteLeft;}			
		if(isBlacksTurn()){
			mid = blackMid;
			right = blackRight;
			left = blackLeft;}	
		
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

