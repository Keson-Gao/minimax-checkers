import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
	
	private static int aiNumberOfPieces = 12;
	private static int humanNumberOfPieces = 12;
	
	private static int currSquareIndex;
	protected static boolean turn = true; 

	private static int[] humanPlayer_moves = new int[5000];		 	
	private static int[] aiPlayer_moves = new int[5000];			
	
	private static int MovesCount = 0;		
	private static int aiMovesCount = 0;	
	
	private static boolean prevChip = false;
	private static boolean aiprevChip = false;
	
	ArrayList<Integer> indexOfPath = new ArrayList<Integer>();
	ArrayList<Integer> greenSqrIndex = new ArrayList<Integer>();
	ArrayList<Integer> indexOfClickedPath = new ArrayList<Integer>();
	
	int indices[] = {0, 2, 4, 6, 9, 11, 13, 15, 16, 18, 20, 22, 25, 27, 29, 31, 32, 34, 36, 38, 41, 	
			43, 45, 47, 48, 50, 52, 54, 57, 59, 61, 63};		
	
	//	Edge of Boards
	int[] leftEdge = {0, 16, 32, 48}, rightEdge = {15, 31, 47, 63};		
	int[] aiEdge = {0, 2, 4, 6};

	private static Icon humanPlayerPiece, aiPiece, humanPlayerPieceKing;
	
	public CheckerBoard(){
		
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
							
			
			if(i < 32){
				
				// ?? HUMAN TURN
				if(turn == true){																												
					
					if(prevChip == true){							
													
						if(greenSquares[currSquareIndex].getIcon() == null || 
								greenSquares[currSquareIndex].getIcon().toString().equals(dottedWhite.toString())){
										
							if(!indexOfClickedPath.isEmpty() && 
									greenSqrIndex.get(currSquareIndex) == indexOfClickedPath.get(indexOfClickedPath.size()-1)){								
											
								for(int j = 1; j < indexOfClickedPath.size(); j++){
									greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);
								}
								
								movePiece(index);	
								
								if(aiNumberOfPieces == 0){
									
									System.out.println("You win!");									
								}
								
							}else{
								
								if(isValid(index)){
									
									indexOfClickedPath.add(index);		
									greenSquares[greenSqrIndex.indexOf(index)].setIcon(dottedWhite);
									
									
								}else{
									resetBoardColor();
									
									for(int j = 1; j < indexOfClickedPath.size(); j++){
										greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);
									}
									
									indexOfClickedPath.removeAll(indexOfClickedPath);
								}

							}
								
						}else if(greenSquares[i].getIcon().toString().equals(humanPlayerPiece.toString())
								|| greenSquares[i].getIcon().toString().equals(humanPlayerPieceKing.toString())){
							
							resetBoardColor();
							indexOfClickedPath.removeAll(indexOfClickedPath);							
							
							if(greenSquares[i].getIcon().toString().equals(humanPlayerPieceKing.toString()))					
								newPieceClick(humanPlayerPieceKing, currSquareIndex, index);
							else
								newPieceClick(humanPlayerPiece, currSquareIndex, index);
							
							indexOfClickedPath.add(index);
							
						
						}else if(greenSquares[i].getIcon().toString().equals(aiPiece.toString())){							
							
							resetBoardColor();							
							
							for(int j = 1; j < indexOfClickedPath.size(); j++)
								greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);							
							
							indexOfClickedPath.removeAll(indexOfClickedPath);
						
						}else{
							
							resetBoardColor();
							
							for(int j = 1; j < indexOfClickedPath.size(); j++){
								greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(j))].setIcon(null);
							}
							
							indexOfClickedPath.removeAll(indexOfClickedPath);
						}
					}
					
					else if(greenSquares[i].getIcon() != null && 
							prevChip == false){
						
						if(greenSquares[currSquareIndex].getIcon().toString().equals(humanPlayerPieceKing.toString())){
						
							System.out.println("YEEE");
							newPieceClick(humanPlayerPieceKing, currSquareIndex, index);
							
						}else{
							
							System.out.println("YOOOO");
							newPieceClick(humanPlayerPiece, currSquareIndex, index);
						}
						
						indexOfClickedPath.add(index);	
					}														
					
					
				// ?? AI TURN	
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
				
			}else{
				
				resetBoardColor();
			}
		}
	}
	
	private void movePiece(int index){
		
		boolean isEdge = false;
		
		for(int i = 0; i < aiEdge.length; i++){
			if(greenSqrIndex.get(currSquareIndex) == aiEdge[i]){				
				isEdge = true;
				break;
			}
		}
		
		if(!isEdge)
			greenSquares[currSquareIndex].setIcon(humanPlayerPiece);
		else
			greenSquares[currSquareIndex].setIcon(humanPlayerPieceKing);
		
		humanPlayer_moves[MovesCount++] = index;
		greenSquares[greenSqrIndex.indexOf(indexOfClickedPath.get(0))].setIcon(null);									
			
		resetBoardColor();
												
		turn = !turn;																		
		prevChip = false;		
		
		eatEnemyPiece();
		
		indexOfClickedPath.removeAll(indexOfClickedPath);		
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
	
		
		if(indexOfClickedPath.size() > 1){
			
			int nIndex1 = indexOfClickedPath.get(indexOfClickedPath.size()-1);
			int nIndex2 = indexOfClickedPath.get(indexOfClickedPath.size()-2);
				
			System.out.println("nIndex1: " + nIndex1);
			
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
					
					if(greenSquares[greenSqrIndex.indexOf((nIndex1 - 9))].getIcon().toString().equals(aiPiece.toString()))
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
		
		humanPlayer_moves[MovesCount++] = index;								
		squarePanels[index].setBackground(Color.YELLOW);
		
		pathGlowForward(currSquareIndex, index, chipColor, false);										
		
		for(int i1 = 0; i1 < indexOfPath.size(); i1++)						
			pathGlowBackward(greenSqrIndex.indexOf(indexOfPath.get(i1)), indexOfPath.get(i1), chipColor);		
		
		indexOfPath.removeAll(indexOfPath);								
		prevChip = true;
	}
	
	public void pathGlowForward(int currSquareIndex, int index, Icon color, boolean inRecur){			
				
		if(index - 9 < 0 || index - 7 < 0 ){ 
			return;
		}
			
		boolean isLeftEdge = false, isRightEdge = false;
		
		for(int i = 0; i < 4; i++){			
			if(index == rightEdge[i]){
				
				isRightEdge = true; 
				break;
				
			}			
			else if(index == leftEdge[i]){
				
				isLeftEdge = true;
				break;
			}						
		}
		
		if(color.toString().equals(humanPlayerPieceKing.toString())){

			System.out.println("inKing");
			storePath(index);
			
			if(isRightEdge){

				int nIndex1 = index - 9;
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
				
				if(greenSquares[indexOfn1].getIcon() == null){
					
					if(!inRecur)
						squarePanels[nIndex1].setBackground(Color.CYAN);
				}									
							
				else if(greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isLEdge = false;
					for(int i = 0; i < 4; i++){											
						if(nIndex1 == leftEdge[i]){
							
							isLEdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){											
						if(nIndex == rightEdge[i]){
							
							isREdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){											
						if(nIndex1 == leftEdge[i]){
							
							isLEdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){			
						if(nIndex2 == rightEdge[i]){
							
							isREdge = true;						
							break;
							
						}													
					}
					
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

			
		}else{
						
			if(isRightEdge){

				int nIndex1 = index - 9;
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		
				
				if(greenSquares[indexOfn1].getIcon() == null){
					
					if(!inRecur)
						squarePanels[nIndex1].setBackground(Color.CYAN);
				}									
							
				else if(greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isLEdge = false;
					for(int i = 0; i < 4; i++){											
						if(nIndex1 == leftEdge[i]){
							
							isLEdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){											
						if(nIndex == rightEdge[i]){
							
							isREdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){											
						if(nIndex1 == leftEdge[i]){
							
							isLEdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){			
						if(nIndex2 == rightEdge[i]){
							
							isREdge = true;						
							break;
							
						}													
					}
					
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
	}
	
	public void pathGlowBackward(int currSquareIndex, int index, Icon color){							
		
		if(index + 9 > 64 || index + 7 > 64 ){ 
			return;
		}
			
		boolean isLeftEdge = false, isRightEdge = false;
		
		for(int i = 0; i < 4; i++){			
			if(index == rightEdge[i]){
				
				isRightEdge = true; 
				break;
				
			}			
			else if(index == leftEdge[i]){
				
				isLeftEdge = true;
				break;
			}						
		}
		

		if(color.toString().equals(humanPlayerPieceKing.toString())){
						
			if(isRightEdge){
				
				int nIndex1 = index + 7;				
				int indexOfn1 = greenSqrIndex.indexOf(nIndex1);		

				if(greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString())){
										
					boolean isREdge = false;
					for(int i = 0; i < 4; i++){											
						if(nIndex1 == rightEdge[i]){
							
							isREdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){											
						if(nIndex == leftEdge[i]){
							
							isLEdge = true;
							break;
						}						
					}
					
					
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
								
				if(greenSquares[indexOfn1].getIcon() != null && greenSquares[indexOfn1].getIcon().toString().equals(aiPiece.toString()))
				{
										
					boolean isREdge = false;
					for(int i = 0; i < 4; i++){											
						if(nIndex1 == rightEdge[i]){
							
								isREdge = true;
							break;
						}						
					}
					
					
					if(!isREdge && (nIndex1 + 9) > 0){
											
						if(greenSquares[greenSqrIndex.indexOf(nIndex1 + 9)].getIcon() == null){
							squarePanels[nIndex1].setBackground(Color.RED);
							squarePanels[nIndex1 + 9].setBackground(Color.CYAN);
							
							pathGlowBackward(greenSqrIndex.indexOf(nIndex1 + 9), nIndex1 + 9, humanPlayerPieceKing);
						}
						
					}									
					
				}
															
				if(greenSquares[indexOfn2].getIcon() != null && greenSquares[indexOfn2].getIcon().toString().equals(aiPiece.toString()))
				{
						
						boolean isLEdge = false;
						for(int i = 0; i < 4; i++){			
							if(nIndex2 == leftEdge[i]){
								
								//if(greenSqrIndex.contains(nIndex2-7))
									isLEdge = true;
								
								break;
								
							}													
						}
						
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
					for(int i = 0; i < 4; i++){											
						if(nIndex1 == rightEdge[i]){
							
							isREdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){											
						if(nIndex == leftEdge[i]){
							
							isLEdge = true;
							break;
						}						
					}
					
					
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
					for(int i = 0; i < 4; i++){											
						if(nIndex1 == rightEdge[i]){
							
								isREdge = true;
							break;
						}						
					}
					
					
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
						for(int i = 0; i < 4; i++){			
							if(nIndex2 == leftEdge[i]){
								
								//if(greenSqrIndex.contains(nIndex2-7))
									isLEdge = true;
								
								break;
								
							}													
						}
						
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
		System.out.println(indexOfPath);
	}
}
