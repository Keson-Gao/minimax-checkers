/**	
	Filename:		CheckerBoard.java	
	Purpose:		Board functions: path glow, move, 2 players	
	
**/


import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;

public class CheckerBoard extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JPanel board = new JPanel(new GridLayout(8, 8, 2, 2));
	private JPanel[] squarePanels = new JPanel[64];		//	----> panels to store squares
	private JLabel[] squares = new JLabel[32];	//	----> chip placements
	
	private Icon whiteChip = new ImageIcon(getClass().getResource("white.png"));	
	private Icon blackChip = new ImageIcon(getClass().getResource("black.png"));			
	
	private static int currSquareIndex;
	protected static boolean turn = true; 	//Alternating turns: 'true' == white,'false': black.
	private static int[] white_moves = new int[5000];	//Previous paths of White. (must be changed)
	private static int[] black_moves = new int[5000];	//Previous paths of Black.	
	
	private static int wMovesCount = 0;		//Total number of white's moves
	private static int bMovesCount = 0;		//Total number of black's moves
	
	private static boolean n1 = false;
	private static boolean n2 = false;	
	
	//	Location of green blocks where chips can move
	int[] greenSquares = {0, 2, 4, 6, 9, 11, 13, 15, 16, 18, 20, 22, 25, 27, 29, 31, 32, 34, 36, 38, 41, 	
		43, 45, 47, 48, 50, 52, 54, 57, 59, 61, 63};		
	
	//	Edge of Boards
	int[] leftEdge = {0, 16, 32, 48}, rightEdge = {15, 31, 47, 63};						
	
	public CheckerBoard(){
		
		setOpaque(false);
		setLayout(null);
		setBounds(0, 0, 1000, 700);
		
		board.setBackground(Color.BLACK);
		board.setBounds(160, 46, 470, 470);	//	board coordinates, size
		
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
			
		
		//	Placing the chips in places
		
		for(int j = 0; j < 32; j++){
			
			squares[j] =  new JLabel();				
			squarePanels[greenSquares[j]].add(squares[j], BorderLayout.CENTER);			
			
			//	placement of Black Chips
			if(greenSquares[j] >= 0 && greenSquares[j] <= 22)
				squares[j].setIcon(blackChip);
			
			//	placement of White Chips
			if(greenSquares[j] >= 41 && greenSquares[j] <= 63)
				squares[j].setIcon(whiteChip);			
		}			
				
		add(board);						
	}	
	
	public class MovementHandler extends MouseAdapter
	{
		
		public void mouseClicked(MouseEvent e)
		{
			Object source = e.getSource();
			
			for(int i = 0; i< 32; i++)
			{
				//If a block is clicked.
				if(source == squarePanels[greenSquares[i]])	
				{				
					//The index where the clicked block lies.
					currSquareIndex = i;
					int index = greenSquares[i];		
					
					//Resets the colors of all except 
					//the chosen green block to GREEN
					//(Why is this necessary?)
					for(int j = 0; j < 32; j++){
						if(greenSquares[j] ==  index){
							j++;
							continue;
						}
						squarePanels[greenSquares[j]].setBackground(Color.GREEN);
					}
					
					//(White's turn) Initially, if a green block is clicked..
					if(turn == true){						
						
						// If a chip is NOT previously clicked..
						if(n1 == false){
							
							// If the clicked block is OCCUPIED with a white chip.
							if(squares[i].getIcon() == whiteChip){	
																
								white_moves[wMovesCount++] = index;		// Why store this move in record?
								
								// Make the clicked block glow.
								squarePanels[index].setBackground(Color.YELLOW);
								
								// Make the path of the block glow.
								chipsGlow(currSquareIndex, index, whiteChip);
								
								// Set user choosed a pawn.
								n1 = true;								
							}
						}
						
						if(n1 == true){		//And if a (white) chip is previously selected..
							
							if(squares[i].getIcon() == null){	//And if the clicked green block is UNoccupied.
								
								// Check if the clicked block and 
								// the previously chosen chip is adjacent
								// to the right.								
								if(index+9 == white_moves[wMovesCount-1]){
									
									// Moves the chip forward.									
									squares[i].setIcon(whiteChip);

									// Stores the index to record.
									white_moves[wMovesCount++] = index;

									// Erases the chip in its previous area.
									int temp = index+9;
									for(int a = 0; a < 32; a++){
										if(temp == greenSquares[a]){
											squares[a].setIcon(null);									
										}
									}			
									
									// Change board colors back to green.
									origColor();
									
									// Toggles to opponent's turn.
									turn = !turn;
									
									// Set isPreviouslySelectedchip false
									n1 = false;									
									
								}
								
								// The block and the chosen chip is NOT adjacent
								// to the right BUT is adjacent to the left.
								else if(index+7 == white_moves[wMovesCount-1])
								{
									// Moves the chip forward.									
									squares[i].setIcon(whiteChip);
									
									// Stores this move to record.
									white_moves[wMovesCount++] = index;
									
									// Erases the chip at its previous area.
									int temp = index + 7;
									for(int a = 0; a < 32; a++){
										if(temp == greenSquares[a]){
											squares[a].setIcon(null);									
										}
									}					
									
									// Change board colors back to green. 
									origColor();									
									
									// Toggles turn to opponent.
									turn = !turn;
									
									// Set 'previously-selected-a-chip' false
									n1 = false;																		
									
								}
								
								// The block and the chosen chip are not adjacent.
								else{	
									
									// Change board colors back to green.
									origColor();									
								}
								
								break;	// Loop ends.
								
							}
							
							// The block is OCCUPIED with white chip.
							else if(squares[i].getIcon() == whiteChip){
								
								// Another chip is chosen.
								n1 = false;
								
							}
							
							// The block is OCCUPIED with black chip.
							else if(squares[i].getIcon() == blackChip){
								
								//	An invalid move, so change board color back to green.
								origColor();
							}		
							
							break;	// End loop.
						}
						
						
						break;
					}					
					
					//	If it is BLACK'S turn.
					if(turn == false){
						/*if(squares[i].getIcon() == blackChip && turn == false)	
						{
							//	code here					
							squarePanels[index].setBackground(Color.YELLOW);
							chipsGlow(currSquareIndex, index, blackChip);
							
							turn = !turn;
							break;						
						}else{
							//	otherwise code here
							//	@Play Sound & Display a Message
						}*/
						
						// If a black chip was previously chosen.
						if(n2 == true){		
							
							// And if the clicked block is EMPTY.
							if(squares[i].getIcon() == null){	
								
								// Checks if the block and the chosen chip 
								// are adjacent to the right.
								if(index-9 == black_moves[bMovesCount-1]){
									
									// Moves the chip forward.									
									squares[i].setIcon(blackChip);
									
									// Store this move in record.
									black_moves[bMovesCount++] = index;

									// Erase the chip on its previous area. 
									int temp = index-9;
									for(int a = 0; a < 32; a++){
										if(temp == greenSquares[a]){
											squares[a].setIcon(null);							
											break;
										}
									}										
									
									// Change board colors back to green.
									origColor();						
									
									// Toggles turn to opponent.
									turn = !turn;
									
									// The user did not choose a black chip.
									n2 = false;									
								
								//	The empty block and the chosen chip are adjecent from the left.
								}else if(index-7 == black_moves[bMovesCount-1]){
									
									//	Moves the chip forward.									
									squares[i].setIcon(blackChip);
									
									// Stores this move in record.
									black_moves[bMovesCount++] = index;
																		
									// Erases the chip on its previous area.
									int temp = index-7;
									for(int a = 0; a < 32; a++){
										if(temp == greenSquares[a]){
											squares[a].setIcon(null);
											break;
										}
									}					
									
									// Change board colors back to green.
									origColor();						
									
									// Toggle turn to opponent.
									turn = !turn;
									
									// The user did not choose a chip.
									n2 = false;																		
									
								}
								
								//	The empty block and chosen chip are not adjacent.
								else{
									
									// Change board colors back to green.
									origColor();										
								}

							}
							
							// The block is OCCUPIED with a black chip.
							else if(squares[i].getIcon() == blackChip){
								
								// The user chose another black chip.
								n2 = false;
								
							}
							
							// The block is OCCUPIED with a white chip.
							else if(squares[i].getIcon() == whiteChip){
								
								// Change board color back to green.
								origColor();	
							}		
							
							break;
						}
						
						// User did not choose a black chip yet. 
						if(n2 == false){
							
							// The chosen block is occupied with a black chip.
							if(squares[i].getIcon() == blackChip){	
								
								// Store this move in record.
								black_moves[bMovesCount++] = index;
								
								// Make the block glow.
								squarePanels[index].setBackground(Color.YELLOW);
								
								// Make the path of the block glow.
								chipsGlow(currSquareIndex, index, blackChip);
								
								//	User choosed a chip.
								n2 = true;								
							}
						}
						
						break;
						
					}
					
				}
				
				// User clicked a non-green block.
				else{		

					// Change board colors back to green.
					origColor();
				}
			}
		}
	}
	
	public void origColor(){
		for(int j = 0; j < 32; j++){			
			squarePanels[greenSquares[j]].setBackground(Color.GREEN);
		}
	}
	
	//	*Path Glow*
	public void chipsGlow(int currSquareIndex, int index, Icon color){
		
		if(color == whiteChip)
		{
			//First Forward & Backward
			boolean toggle = false;
			int temp = index-9;			
			int disk_temp = currSquareIndex;		//	---> initialized to avoid error
						
			
			while(temp >= 0)
			{			
				for(int a = 0; a < 32; a++){
					if(temp == greenSquares[a]){
						disk_temp = a;		//	--> assigns the value of disk_temp
					}					
				}
				//	Checks if the next adjacent path is at left edge
				for(int m = 0; m < 4; m++){
					if(temp+9 == leftEdge[m]){
						toggle = true;
						break;					
					}
				}
				
				//	Loop breaks when next adjacent path is at left edge
				if(toggle == true)				
					break;				
				
				//	Checks if next is path is empty, occupied by opponent, 
				//	or occupied by ally respectively				
				if(squares[disk_temp].getIcon() == null)		
				{								
					squarePanels[temp].setBackground(Color.CYAN);														
					break;
					
				}else if(squares[disk_temp].getIcon() == blackChip){
					
					if(squares[disk_temp-1].getIcon() == blackChip)	//	added new code!
						break;
					
					boolean edge = false;
					
					//	Loop ends if the current chosen chip is at left edge
					for(int m = 0; m < 4; m++){
						if(temp == leftEdge[m]){
							edge = true;
							break;					
						}
					}
					
					//	The next immediate adjacent path glows
					if(edge == false){
						if(squares[disk_temp+1].getIcon() != null)	// added new code!
							squarePanels[temp].setBackground(Color.RED);							
					}
					
				}else if(squares[disk_temp].getIcon() == color)
						break;	
					
				temp-=9;				
								
			}
			
			//	Forward to the right
			boolean toggle2 = false;
			int temp2 = index-7;
			int disk_temp2 = currSquareIndex;
									
			while(temp2 >= 0)
			{				
				for(int a = 0; a < 32; a++){
					if(temp2 == greenSquares[a]){
						disk_temp2 = a;
						break;
					}
				}
				
				//	Checks if the current chosen chip is placed at right edge
				for(int m = 0; m < 4; m++){
					if(temp2+7 == rightEdge[m]){
						toggle2 = true;
						break;					
					}
				}
				
				//	Loop breaks when chip is at right edge
				if(toggle2 == true)				
					break;
				
				//	Checks if the next immediate path is empty,
				//	occupied by opponent, or occupied by ally
				if(squares[disk_temp2].getIcon() == null)		//path is not occupied
				{											
					squarePanels[temp2].setBackground(Color.CYAN);					
					break;						
					
				}else if(squares[disk_temp2].getIcon() == blackChip)	//path is occupied and of opponent's
				{	
					if(squares[disk_temp2-1].getIcon() == blackChip)	//	added new code!
						break;
					
					boolean edge2 = false;					
					//	Loop ends if opponent's chip is at right edge
					for(int m = 0; m < 4; m++){
						if(temp2 == rightEdge[m]){
							edge2 = true;
							break;					
						}
					}
					
					if(edge2 == false){
						if(squares[disk_temp2+1].getIcon() != null)	// added new code!
							squarePanels[temp2].setBackground(Color.RED);							
					}
						
						
				}else if(squares[disk_temp2].getIcon() == color)
						break;	//	Loop breaks if next path is occupied by ally
				
				temp2-=7;
			}
			
			/*
			
			//	First Backward to the Right
			toggle = false;
			temp = index+9;
			disk_temp = currSquareIndex;
			
			while(temp < 64)
			{
				for(int i = 0; i < 32; i++){
					if(temp == greenSquares[i]){
						disk_temp = i;
						break;
					}
				}				
				
				for(int m = 0; m < 4; m++){
					if(temp == rightEdge[m]){
						toggle = true;					
						break;
					}
				}			
				
				if(toggle == true)
					break;
				
				if(squares[disk_temp].getIcon() == blackChip){
					squarePanels[temp].setBackground(Color.RED);	
					//
				}
				
				/*
				if(squares[disk_temp].getIcon() == null)		//path is not occupied
				{								
					squarePanels[temp].setBackground(Color.CYAN);														
					break;
				}else if(squares[disk_temp].getIcon() != whiteChip && squares[disk_temp].getIcon() != null){					
						squarePanels[temp].setBackground(Color.RED);											
				}else if(squares[disk_temp].getIcon() == color)
					break;	//	stop the search				
				
				
				temp+=9;
			}*/
		}

		if(color == blackChip){			
		
			//First Forward
			boolean toggle = false;
			int temp = index+9;			
			int disk_temp = currSquareIndex;			
					
			while(temp >= 0){			
			
				for(int a = 0; a < 32; a++){
					if(temp == greenSquares[a]){
						disk_temp = a;
						break;
					}
				}			
				
				for(int m = 0; m < 4; m++){
					if(temp-9 == rightEdge[m]){
						toggle = true;
						break;					
					}
				}
				
				if(toggle == true)				
					break;				
					
				if(squares[disk_temp].getIcon() == null)		//path is not occupied
				{								
					squarePanels[temp].setBackground(Color.CYAN);														
					break;
				}else if(squares[disk_temp].getIcon() == whiteChip)	//path is occupied and of opponent's
				{
					squarePanels[temp].setBackground(Color.RED);					
					//	@@search further
				}else if(squares[disk_temp].getIcon() == color)
					break;	//	stop the search
					
				temp+=9;				
			}
			
			//	Second Forward
			boolean toggle2 = false;
			int temp2 = index+7;
			int disk_temp2 = currSquareIndex;			
			
			while(temp2 >= 0){
				
				for(int a = 0; a < 32; a++){
					if(temp2 == greenSquares[a]){
						disk_temp2 = a;
						break;
					}
				}
				
				for(int m = 0; m < 4; m++){
					if(temp2-7 == leftEdge[m]){
						toggle2 = true;
						break;					
					}
				}
				if(toggle2 == true)				
					break;				
				if(squares[disk_temp2].getIcon() == null)		//path is not occupied
				{									
					squarePanels[temp2].setBackground(Color.CYAN);					
					break;						
				}else if(squares[disk_temp2].getIcon() == whiteChip)	//path is occupied and of opponent's
				{							
					squarePanels[temp2].setBackground(Color.RED);
					//	search further
				}else if(squares[disk_temp2].getIcon() == color)
					break;	//	stop the search
				
				temp2+=7;
			}
			
			/*
			//	First Backward
			toggle = false;
			temp = index+9;
			
			while(temp < 64){
				for(int m = 0; m < 4; m++){
					if(temp == rightEdge[m]){
						toggle = true;					
						break;
					}
				}			
				if(toggle == true)
					break;
				
				if(squarePanels[temp].getIcon() != color && squarePanels[temp].getIcon() != null){
					if(squarePanels[temp+9].getIcon() == null){
						squarePanels[temp].setBackground(Color.RED);
						squarePanels[temp+9].setBackground(Color.CYAN);
					}					
				}else break;
				
				temp+9;
			}*/
		}
		
		
	}	
}