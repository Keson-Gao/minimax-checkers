package app.modules.gui;

import app.modules.board.Board;
import app.modules.board.Piece;
import app.modules.ai.AI;
import app.utils.enums.PieceColor;
import app.utils.helper.*;
import app.utils.helper.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckerBoard extends JPanel{
	private static final long serialVersionUID = 1L;
	
	//Board gameBoard = new Board();
	
	private JPanel board = new JPanel(new GridLayout(8, 8, 2, 2));
	private JPanel[] squarePanels = new JPanel[64];	
	private JLabel[] greenSquares = new JLabel[32];				
	
	private Icon whiteChip = new ImageIcon(getClass().getResource("/white.png"));
	private Icon whiteKing = new ImageIcon(getClass().getResource("/whiteKing.png"));
	private Icon blackChip = new ImageIcon(getClass().getResource("/black.png"));
	private Icon blackKing = new ImageIcon(getClass().getResource("/blackKing.png"));
	private Icon dottedWhite = new ImageIcon(getClass().getResource("/eatpathwhite.png"));
	
	//Number of Pieces left of the players.
	private static int aiNumberOfPieces = 12;
	private static int humanNumberOfPieces = 12;
	
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
	
	
	//	Corresponding specific POINTS assigned to black and white pieces.
	int indices[] = {0, 2, 4, 6, 9, 11, 13, 15, 16, 18, 20, 22, 25, 27, 29, 31, 32, 34, 36, 38, 41, 	
			43, 45, 47, 48, 50, 52, 54, 57, 59, 61, 63};		
	
	//	Edge of Boards
	int[] lEdge = {0, 16, 32, 48}, rEdge = {15, 31, 47, 63};		
	int[] kingsEdge = {0, 2, 4, 6};

	private static Icon humanPlayerPiece, aiPiece, humanPlayerPieceKing;
	
	public CheckerBoard(){
		
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
		
		add(board);						
	}	
	
	public class MovementHandler extends MouseAdapter{
		int index;
		
		public void mouseClicked(MouseEvent e){
			Object source = e.getSource();
			
			
			int i;
			for(i = 0; i < 32; i++){
				
				if(source == squarePanels[greenSqrIndex.get(i)]){				
					currSquareIndex = i;
					index = greenSqrIndex.get(i);
					break;
				}								
			}
							
			
			//If user clicked a green square.
			if(i < 32){
				
				// Human player's turn.
				if(turn == true){																												
					
					//If a piece was previously clicked.
					
					if(prevChip == true){							
													
						//And if the clicked square is a dotted white.
						
						if(greenSquares[currSquareIndex].getIcon() == null || 
							greenSquares[currSquareIndex].getIcon().toString().equals(dottedWhite.toString())){
									
							//If the square was already clicked twice.							
							if(!indexOfClickedPath.isEmpty() && 
								greenSqrIndex.get(currSquareIndex) == indexOfClickedPath.get(indexOfClickedPath.size()-1)){								
											
								// Erase dotted lines.
								for(int j = 1; j < indexOfClickedPath.size(); j++){
									greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);
								}
								
								// Move piece.
								movePiece(index);
								turn = true;

								// Winning condition already?

								// We can just invoke the AI after we move the piece.
								Board currBoard = generateBoard();
								setBoard(new AI().getMove(currBoard, PieceColor.BLACK, 4));

								// Winning condition already?
							}else{	//If the square was clicked once.
								
								//Check if the square is valid as path.							
								if(isValid(index)){
									
									//Add index of square to consecutive collection of paths.
									indexOfClickedPath.add(index);		

									//Mark square with dotted piece.
									greenSquares[greenSqrIndex.indexOf(index)].setIcon(dottedWhite);
									
									
								//Square is not valid as a path.
								}else{
									
									//Reset board color back to green.
									resetBoardColor();									
									
									//Erase marks (dotted pieces).
									for(int j = 1; j < indexOfClickedPath.size(); j++){
										greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);
									}								
									
									//Clean path collection.
									indexOfClickedPath.removeAll(indexOfClickedPath);									
								}

							}
								
						
						// Clicked square is either a King or a normal piece.
							
						}else if(greenSquares[i].getIcon().toString().equals(humanPlayerPiece.toString())
								|| greenSquares[i].getIcon().toString().equals(humanPlayerPieceKing.toString())){
							
							//The user chose another piece. 
							//So reset and clean all previous paths.
							
							resetBoardColor();
							indexOfClickedPath.removeAll(indexOfClickedPath);	
							

							//Make path of chosen piece glow.
							
							if(greenSquares[i].getIcon().toString().equals(humanPlayerPieceKing.toString()))					
								newPieceClick(humanPlayerPieceKing, currSquareIndex, index);
							else
								newPieceClick(humanPlayerPiece, currSquareIndex, index);

 							//Add piece to collection of paths.
							indexOfClickedPath.add(index);
						
						
							
						// The user clicked the opponent's (AI) piece.
							
						}else if(greenSquares[i].getIcon().toString().equals(aiPiece.toString())){							
							
							// This is an invalid move.
							// Reset and clean board.
							
							resetBoardColor();							
							
							for(int j = 1; j < indexOfClickedPath.size(); j++)
								greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);							
							
							indexOfClickedPath.removeAll(indexOfClickedPath);
						

						// The user clicked a non-green square.
						}else{
							
							// This is an invalid move. 
							// Reset and clean board.
							
							resetBoardColor();
							
							for(int j = 1; j < indexOfClickedPath.size(); j++){
								greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);
							}
							
							indexOfClickedPath.removeAll(indexOfClickedPath);
						}
					}
					
					
					// A chip was not previously clicked.
					
					else if(greenSquares[i].getIcon() != null && prevChip == false){
					
						//Make the path of the piece glow.
						
						if(greenSquares[currSquareIndex].getIcon().toString().equals(humanPlayerPieceKing.toString()))													
							newPieceClick(humanPlayerPieceKing, currSquareIndex, index);							
						else							
							newPieceClick(humanPlayerPiece, currSquareIndex, index);						
						
						// Add to collection of paths.
						indexOfClickedPath.add(index);	
					}														
					
									
				}else{
					
					// The AI's turn.					
				}
				
				
			//User did not clicked a green square.
			}else{
				
				resetBoardColor();
			}
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

				// UPDATE THE CORRESPONDING KEY (index) OF THIS PIECE IN HASHMAP.
				//gameBoard.setKingPieceAt(new Point(greenSqrIndex.get(currSquareIndex)));
				
			
			// If the move will not make the piece a king.
			}else{
								
				greenSquares[currSquareIndex].setIcon(humanPlayerPiece); 	//Move the piece.				
				
				//UPDATE KEY/POINT/INDEX OF PIECE IN HASHMAP.
			}					
						
			//Clear the piece from its previous place.
			greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(0))].setIcon(null);									

			
			// Reset board color.
			// Toggle turns.
			// Assign necessary values.
			
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

	private Board generateBoard()
    {
        HashMap<Point, Piece> blackPieces = new HashMap<>();
        HashMap<Point, Piece> whitePieces = new HashMap<>();

        int squarePos = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                Point newPoint = new Point(getPieceColumn(j, (i % 2) == 0), i);
                if (greenSquares[squarePos].getIcon() != null) {
                    if (greenSquares[squarePos].getIcon().toString().equals(humanPlayerPiece.toString())) {
                        whitePieces.put(newPoint, new Piece(PieceColor.WHITE, newPoint));
                    } else if (greenSquares[squarePos].getIcon().toString().equals(humanPlayerPieceKing.toString())) {
                        whitePieces.put(newPoint, new Piece(PieceColor.WHITE, newPoint, true));
                    } else if (greenSquares[squarePos].getIcon().toString().equals(aiPiece.toString())) {
                        blackPieces.put(newPoint, new Piece(PieceColor.BLACK, newPoint));
                    }
                }

                squarePos++;
            }
        }

        return new Board(blackPieces, whitePieces);
    }

	private void setBoard(Board board)
	{
        Piece[] blackPieces = board.getBlackPieces();
        Piece[] whitePieces = board.getWhitePieces();

        clearGreenSquares();

        for (Piece blackPiece : blackPieces) {
            placePieceToBoard(blackPiece);
        }

        for (Piece whitePiece : whitePieces) {
            placePieceToBoard(whitePiece);
        }
	}

	private void placePieceToBoard(Piece piece)
    {
        /*
         * Here, we're actually getting the ordinality of each piece which will map to the right green square.
         * The first (zeroth, rather) square is the top leftmost green square, and the 32nd square is the
         * bottom rightmost green square.
         */
        int xPos = piece.getPoint().x;
        int yPos = piece.getPoint().y;
        int multiplier = 4 * yPos; // This makes sure the piece is placed in the right green square, and just does not
                                   // repeat in the first, second, third, or fourth square.
        int squarePos = (yPos % 2 == 0) ? (xPos / 2) : (xPos - 1) / 2;
        squarePos *= multiplier;

        if (piece.getColor() == PieceColor.BLACK) {
            if (piece.isKing()) greenSquares[squarePos].setIcon(blackKing);
            else greenSquares[squarePos].setIcon(aiPiece);
        } else /* if (piece.getColor() == PieceColor.WHITE) */ {
            if (piece.isKing()) greenSquares[squarePos].setIcon(humanPlayerPieceKing);
            else greenSquares[squarePos].setIcon(humanPlayerPiece);
        }
    }

    private void clearGreenSquares()
    {
        for (JLabel square : greenSquares) {
            square.setIcon(null);
        }
    }

	private int getPieceColumn(int pieceNumber, boolean isEven)
    {
        return (isEven) ? 2 * pieceNumber : (2 * pieceNumber) + 1;
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
			
			int var1 = indexOfClickedPath.get(i);
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
					
		Icon piece = greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(0))].getIcon();
		
		// If piece is King.
		if(piece.toString().equals(humanPlayerPieceKing.toString())){
			
			// Diagonal up/down.
			if(index%9 == indexOfClickedPath.get(indexOfClickedPath.size()-1)%9)				
				return true;				
			
			// Diagonal up/down
			if(index%7 == indexOfClickedPath.get(indexOfClickedPath.size()-1)%7)				
				return true;
			
		}else{						
			
			if(indexOfClickedPath.size() > 1){
				
				int nIndex1 = indexOfClickedPath.get(indexOfClickedPath.size()-1);
				int nIndex2 = indexOfClickedPath.get(indexOfClickedPath.size()-2);							
				
				if(index == nIndex1 - 7 || index == nIndex1 - 9){
						
					if( !((nIndex2 - 7) == nIndex1) && !((nIndex2 - 9) == nIndex1))
						return true;
						
				}else if(index == nIndex1 - 14 || index == nIndex1 - 18 ||
						index == nIndex1 + 14 || index == nIndex1 + 18){
													
					if(greenSquares[greenSqrIndex.indexOf((nIndex1 - 7))].getIcon().toString().equals(aiPiece.toString()) 
						|| greenSquares[greenSqrIndex.indexOf((nIndex1 - 9))].getIcon().toString().equals(aiPiece.toString())
						|| greenSquares[greenSqrIndex.indexOf((nIndex1 + 7))].getIcon().toString().equals(aiPiece.toString())
						|| greenSquares[greenSqrIndex.indexOf((nIndex1 + 9))].getIcon().toString().equals(aiPiece.toString())){
						
						return true;
						
					}				
					
				}
				
			}else{
				
				int nIndex1 = indexOfClickedPath.get(indexOfClickedPath.size()-1);						
				
				if(index == nIndex1 - 7 || index == nIndex1 - 9){					
					return true;
						
				}else{
				
					if(index == nIndex1 - 14 ){
						
						if(greenSquares[greenSqrIndex.indexOf((nIndex1 - 7))].getIcon().toString().equals(aiPiece.toString())){
							return true;
						}
					}
					else if(index == nIndex1 - 18){
						
						Icon arbPiece = greenSquares[greenSqrIndex.indexOf((nIndex1 - 9))].getIcon();
						
						if(arbPiece != null && arbPiece.toString().equals(aiPiece.toString()))
							return true;
						
					}else if(index == nIndex1 + 14){
						
						if(greenSquares[greenSqrIndex.indexOf((nIndex1 + 7))].getIcon().toString().equals(aiPiece.toString()))
							return true;
						
					}else if(index == nIndex1 + 18){
							
						if(greenSquares[greenSqrIndex.indexOf((nIndex1 + 9))].getIcon().toString().equals(aiPiece.toString()))
							return true;
					}
															
				}
								
				
			}
		
			
		}
		
		return false;
	}
	
	public void setHumanPlayerPiece(String piece){		
		
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
		
			//	Placing the chips in places		
			for(int j = 0; j < 32; j++){
				
				greenSquares[j] =  new JLabel();				
				squarePanels[greenSqrIndex.get(j)].add(greenSquares[j], BorderLayout.CENTER);			
				
				//	placement of Black Chips
				if(greenSqrIndex.get(j) >= 0 && greenSqrIndex.get(j) <= 22)
					greenSquares[j].setIcon(aiPiece);
				
				//	placement of White Chips
				if(greenSqrIndex.get(j) >= 41 && greenSqrIndex.get(j) <= 63)
					greenSquares[j].setIcon(humanPlayerPiece);			
			}			
		
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
			kingspathGlowForward(currSquareIndex, index, chipColor, false);		
			
		}else{
			
			//Glow piece's path.
			pathGlowForward(currSquareIndex, index, chipColor, false);			
			for(int i1 = 0; i1 < indexOfPath.size(); i1++)						
				pathGlowBackward(greenSqrIndex.indexOf(indexOfPath.get(i1)), indexOfPath.get(i1), chipColor);
			
		}
		
		indexOfPath.removeAll(indexOfPath);		
		prevChip = true;
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
					while(greenSquares[indexOfn1].getIcon() == null){
																		
						squarePanels[nIndex1].setBackground(Color.CYAN);
						
						if(leftEdge.contains(nIndex1)){
							lEdge = true;
							break;
						}
						
						nIndex1 -= 9;
						indexOfn1 = greenSqrIndex.indexOf(nIndex1);
						
					}															
					
					if(!lEdge && greenSquares[indexOfn1].getIcon() != null && 
							greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){

						boolean isLEdge = false;																											
							if(leftEdge.contains(nIndex1))	isLEdge = true;
						
						
						if(!isLEdge && (nIndex1 - 9) > 0){
												
							if(greenSquares[greenSqrIndex.indexOf(nIndex1 - 9)].getIcon() == null){
								
								squarePanels[nIndex1].setBackground(Color.RED);
								squarePanels[nIndex1 - 9].setBackground(Color.CYAN);
																		
								storePath(nIndex1 - 9);							
								pathGlowForward(greenSqrIndex.indexOf(nIndex1 - 9), nIndex1 - 9, color, true);
							}
							
						}									
						
					}
				}
				
				if(index + 7 < 64 ){
					
					int nIndex1 = index + 7;
					int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
					
					boolean lEdge = false;
					while(greenSquares[indexOfn1].getIcon() == null){
												
						squarePanels[nIndex1].setBackground(Color.CYAN);
						
						if(leftEdge.contains(nIndex1)){
							lEdge = true;
							break;
						}
						
						nIndex1 += 7;
						indexOfn1 = greenSqrIndex.indexOf(nIndex1);
					}
					
					if(!lEdge && greenSquares[indexOfn1].getIcon() != null && 
							greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
																	
						boolean isLEdge = false;																											
							if(leftEdge.contains(nIndex1))	isLEdge = true;
						
						
						if(!isLEdge && (nIndex1 + 7) > 0){
												
							if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 7)].getIcon() == null){
								
								squarePanels[nIndex1].setBackground(Color.RED);
								squarePanels[nIndex1 + 7].setBackground(Color.CYAN);
																		
								storePath(nIndex1 + 7);							
								pathGlowForward(greenSqrIndex.indexOf(nIndex1 + 7), nIndex1 + 7, color, true);
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
					
					while(greenSquares[indexOfn1].getIcon() == null){
						
						squarePanels[nIndex1].setBackground(Color.CYAN);
						
						if(rightEdge.contains(nIndex1)){
							rEdge = true;
							break;
						}
						
						nIndex1 -= 7;
						indexOfn1 = greenSqrIndex.indexOf(nIndex1);
						
					}	

					if(!rEdge && greenSquares[indexOfn1].getIcon() != null && 
							greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){

						boolean isREdge = false;																											
							if(rightEdge.contains(nIndex1))	isREdge = true;
						
						
						if(!isREdge && (nIndex1 - 7) > 0){
												
							if(greenSquares[greenSqrIndex.indexOf(nIndex1 - 7)].getIcon() == null){
								
								squarePanels[nIndex1].setBackground(Color.RED);
								squarePanels[nIndex1 -7].setBackground(Color.CYAN);
																		
								storePath(nIndex1 - 7);							
								pathGlowForward(greenSqrIndex.indexOf(nIndex1 - 7), nIndex1 - 7, color, true);
							}
							
						}									
						
					}

					
				}
			}
			
			if(index + 9 < 64 ){
				
				int nIndex1 = index + 9;
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
				
				boolean rEdge = false;
				while(greenSquares[indexOfn1].getIcon() == null){
											
					squarePanels[nIndex1].setBackground(Color.CYAN);
					
					if(rightEdge.contains(nIndex1)){
						rEdge = true;
						break;
					}
					
					nIndex1 += 9;
					indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				}
				
				if(!rEdge && greenSquares[indexOfn1].getIcon() != null && 
						greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
																
					boolean isREdge = false;																											
						if(rightEdge.contains(nIndex1))	isREdge = true;
					
					
					if(!isREdge && (nIndex1 + 9) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 9)].getIcon() == null){
							
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 + 9].setBackground(Color.CYAN);
																	
							storePath(nIndex1 + 9);							
							pathGlowForward(greenSqrIndex.indexOf(nIndex1 + 9), nIndex1 + 9, color, true);
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
				if(index - 9 > 0){								
					
					if(indexOfPath.isEmpty() || (!(index%9 == indexOfPath.get(indexOfPath.size()-1)%9)
							&& index < indexOfPath.get(indexOfPath.size()-1))){
					
						boolean lEdge = false;					
						while(greenSquares[indexOfn1].getIcon() == null){
													
							squarePanels[nIndex1].setBackground(Color.CYAN);
							
							if(leftEdge.contains(nIndex1)){
								lEdge = true;
								break;
							}							
							
							nIndex1 -= 9;
							indexOfn1 = greenSqrIndex.indexOf(nIndex1);
																									
						}					
																
						if(!lEdge && greenSquares[indexOfn1].getIcon() != null && 
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
				}
				
				// Towards right diagonal up.
				if(index - 7 > 0){
					
					if(indexOfPath.isEmpty() || (!(index%7 == indexOfPath.get(indexOfPath.size()-1)%7)
							&& index < indexOfPath.get(indexOfPath.size()-1))){
						
						boolean rEdge = false;			
						
						while(greenSquares[indexOfn2].getIcon() == null){												
							
							squarePanels[nIndex2].setBackground(Color.CYAN);
							
							if(rightEdge.contains(nIndex2)){
								rEdge = true;
								break;
							}
							
							if(nIndex2 - 7 < 0) break;
							
							nIndex2 -= 7;
							indexOfn2 = greenSqrIndex.indexOf(nIndex2);
						}				
																	
						if(!rEdge && greenSquares[indexOfn2].getIcon() != null && 
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
				}
				
				// Towards Down.				
				nIndex1 = index + 9;
				nIndex2 = index + 7;															
									
				indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				indexOfn2 = greenSqrIndex.indexOf(nIndex2);
													
				// Towards right diagonal down. 
				if(index + 9 < 64){
				
					if(indexOfPath.isEmpty() || (!(index%9 == indexOfPath.get(indexOfPath.size()-1)%9)
							&& index > indexOfPath.get(indexOfPath.size()-1))){
						
						boolean rEdge = false;					
						while(greenSquares[indexOfn1].getIcon() == null){
													
							squarePanels[nIndex1].setBackground(Color.CYAN);
							
							if(rightEdge.contains(nIndex1)){
								rEdge = true;
								break;
							}							
							
							nIndex1 += 9;
							indexOfn1 = greenSqrIndex.indexOf(nIndex1);
																									
						}					
						
						if(!rEdge && greenSquares[indexOfn1].getIcon() != null && 
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
				}
				
				// Towards left diagonal down.
				if(index + 7 < 64){
					
					if(indexOfPath.isEmpty() || (!(index%7 == indexOfPath.get(indexOfPath.size()-1)%7)
							&& index > indexOfPath.get(indexOfPath.size()-1))){
						
						boolean lEdge = false;
						
						while(greenSquares[indexOfn2].getIcon() == null){												
							
							squarePanels[nIndex2].setBackground(Color.CYAN);
							
							if(leftEdge.contains(nIndex2)){
								lEdge = true;
								break;
							}
							
							nIndex2 += 7;
							indexOfn2 = greenSqrIndex.indexOf(nIndex2);
						}

						if(!lEdge && greenSquares[indexOfn2].getIcon() != null && 
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
	}
	
	public void pathGlowForward(int currSquareIndex, int index, Icon color, boolean inRecur){			
				
		if(index - 9 < 0 || index - 7 < 0 ){ 
			return;
		}
			
		boolean isLeftEdge = false, isRightEdge = false;
							
		if(rightEdge.contains(index)) isRightEdge = true; 						
		else if(leftEdge.contains(index)) isLeftEdge = true;
									
			if(isRightEdge){

				int nIndex1 = index - 9;
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
				
				if(greenSquares[indexOfn1].getIcon() == null){
					
					if(!inRecur)
						squarePanels[nIndex1].setBackground(Color.CYAN);
				}									
							
				else if(greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isLEdge = false;								
					if(leftEdge.contains(nIndex1)) isLEdge = true;
					
					if(!isLEdge && (nIndex1 - 9) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 - 9)].getIcon() == null){
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 - 9].setBackground(Color.CYAN);
																	
							storePath(nIndex1 - 9);							
							pathGlowForward(greenSqrIndex.indexOf(nIndex1 - 9), nIndex1-9, color, true);
						}
						
					}									
					
				}
																	
			}			
			else if(isLeftEdge){
				
				int nIndex = index - 7;
				int indexOfn = greenSqrIndex.indexOf(nIndex);
				if(greenSquares[indexOfn].getIcon() == null){
					
					if(!inRecur)
						squarePanels[nIndex].setBackground(Color.CYAN);
				}									
				else if(greenSquares[indexOfn].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isREdge = false;															
					if(rightEdge.contains(nIndex)) isREdge = true;
					
					if(!isREdge && (nIndex - 7) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex - 7)].getIcon() == null){
							squarePanels[nIndex].setBackground(Color.RED);
							squarePanels[nIndex - 7].setBackground(Color.CYAN);
							
							storePath(nIndex - 7);	
							pathGlowForward(greenSqrIndex.indexOf(nIndex - 7), nIndex-7, color, true);
						}
						
					}									
					
				}
			}
			else{
				
				int nIndex1 = index - 9;
				int nIndex2 = index - 7;															
								
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);
				int indexOfn2 = greenSqrIndex.indexOf(nIndex2);
								
				
				if(greenSquares[indexOfn1].getIcon() == null){
					
					if(!inRecur)
						squarePanels[nIndex1].setBackground(Color.CYAN);
				}									
				else if(greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isLEdge = false;
					if(leftEdge.contains(nIndex1)) isLEdge = true;
					
					if(!isLEdge && (nIndex1 - 9) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 - 9)].getIcon() == null){
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 - 9].setBackground(Color.CYAN);
							
							storePath(nIndex1 - 9);	
							pathGlowForward(greenSqrIndex.indexOf(nIndex1 - 9), nIndex1-9, color, true);
						}
						
					}									
					
				}
				
				if(greenSquares[indexOfn2].getIcon() == null){
					
					if(!inRecur)
						squarePanels[nIndex2].setBackground(Color.CYAN);
				}
				else if(greenSquares[indexOfn2].getIcon().toString().equals(aiPiece.toString())){
					
					boolean isREdge = false;
					if(rightEdge.contains(nIndex2)) isREdge = true;													
					
					if(!isREdge && (nIndex2 - 7) > 0){
						
						if(greenSquares[greenSqrIndex.indexOf(nIndex2 - 7)].getIcon() == null){						
							squarePanels[nIndex2].setBackground(Color.RED);
							squarePanels[nIndex2 - 7].setBackground(Color.CYAN);
							
							storePath(nIndex2 - 7);	
							pathGlowForward(greenSqrIndex.indexOf(nIndex2 - 7), nIndex2-7, color, true);							
						}
					}
										
					
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
															
				if(greenSquares[indexOfn2].getIcon() != null && greenSquares[indexOfn2].getIcon().toString().equals(aiPiece.toString()))
				{
						
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
