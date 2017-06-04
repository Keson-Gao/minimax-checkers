package app.modules.gui;

/**
	Filename:		CheckerBoard.java	
	Purpose:		Board functions: path glow, move, 2 players	
	
**/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CheckerBoard extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JPanel board = new JPanel(new GridLayout(8, 8, 2, 2));
	private JPanel[] blocks = new JPanel[64];		//	----> panels to store squares
	private JLabel[] squares = new JLabel[32];	//	----> stone placements
	
	private Icon white_chip = new ImageIcon(getClass().getResource("/white.png"));
	private Icon black_chip = new ImageIcon(getClass().getResource("/black.png"));
	
	private int tog = 0;			//Used intializing board colors.
	private static int disk_index;	//Turrent index of the stone.
	protected static boolean turn = true; 	//Alternating turns: 'true' == white,'false': black.
	private static int[] white_moves = new int[5000];	//Previous paths of White. (must be changed)
	private static int[] black_moves = new int[5000];	//Previous paths of Black.	
	
	private static int wMovesCount = 0;		//Total number of white's moves
	private static int bMovesCount = 0;		//Total number of black's moves
	
	private static boolean n1 = false;
	private static boolean n2 = false;	
	
	//	Location of green blocks where chips can move
	int[] k = {0, 2, 4, 6, 9, 11, 13, 15, 16, 18, 20, 22, 25, 27, 29, 31, 32, 34, 36, 38, 41, 	
		43, 45, 47, 48, 50, 52, 54, 57, 59, 61, 63};		
	
	//	Edge of Boards
	int[] left_edge = {0, 16, 32, 48}, right_edge = {15, 31, 47, 63};						
	
	public CheckerBoard(){
		
		setOpaque(false);
		setLayout(null);
		setBounds(0, 0, 1000, 700);
		
		board.setBackground(Color.BLACK);
		board.setBounds(258, 46, 470, 470);	//	board coordinates, size
		
		MovementHandler handler = new MovementHandler();
		
		for(int i = 0; i<64; i++){
			
			blocks[i] = new JPanel();
			board.add(blocks[i]);
			
			//	Setting the board colors
			if((i >= 0 && i < 8) || (i >= 16 && i < 24) || (i >= 32 && i < 40) || (i >= 48 && i < 56))
				tog = 0;
			
			if((i >= 8 && i < 16) || (i >= 24 && i < 32) || (i >= 40 && i < 48) || (i >= 56))
				tog = 1;						
			
			if(tog == 0){
				if(i%2 == 0)				
					blocks[i].setBackground(Color.GREEN);
			}			
			if(tog == 1){
				if(i%2 != 0)
					blocks[i].setBackground(Color.GREEN);
			}

			blocks[i].addMouseListener(handler);
		}		
			
		
		//	Placing the stones in places
		
		for(int j = 0; j < 32; j++){
			
			squares[j] =  new JLabel();				
			blocks[k[j]].add(squares[j], BorderLayout.CENTER);			
			
			//	placement of Black Chips
			if(k[j] >= 0 && k[j] <= 22)
				squares[j].setIcon(black_chip);
			
			//	placement of White Chips
			if(k[j] >= 41 && k[j] <= 63)
				squares[j].setIcon(white_chip);			
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
				//	when user clicks a green block
				if(source == blocks[k[i]])	
				{								
					disk_index = i;
					int index = k[i];		
					
					for(int j = 0; j < 32; j++){
						if(k[j] ==  index){
							j++;
							continue;
						}
						blocks[k[j]].setBackground(Color.GREEN);
					}
					
					//	@White's turn					
					if(turn == true){												
						if(n1 == true){		//	if user previously choosed a pawn
							if(squares[i].getIcon() == null){	// user clicked an empty block
								
								//	check if the empty block and the chosen chip is adjacent
								if(index+9 == white_moves[wMovesCount-1]){
									
									//	@move the chip forward									
									squares[i].setIcon(white_chip);
									white_moves[wMovesCount++] = index;

									int temp = index+9;
									for(int a = 0; a < 32; a++){
										if(temp == k[a]){
											squares[a].setIcon(null);									
										}
									}																		
									origColor();									
									turn = !turn;
									n1 = false;									
									
								}else if(index+7 == white_moves[wMovesCount-1]){
									//	@move the chip forward
									
									squares[i].setIcon(white_chip);
									white_moves[wMovesCount++] = index;
									
									int temp = index+7;
									for(int a = 0; a < 32; a++){
										if(temp == k[a]){
											squares[a].setIcon(null);									
										}
									}					
									origColor();									
									turn = !turn;
									n1 = false;																		
									
								}else{	//	empty block and chosen chip is not adjacent
									origColor();	//	change board colors back to green									
								}
								break;							
							}else if(squares[i].getIcon() == white_chip){
								n1 = false;	// the user chose another pawn
							}else if(squares[i].getIcon() == black_chip){
								origColor();	//	change board color back to green
							}		
							
							break;
						}
						
						if(n1 == false){
							if(squares[i].getIcon() == white_chip){	
								
								white_moves[wMovesCount++] = index;																
								blocks[index].setBackground(Color.YELLOW);						
								chipsGlow(disk_index, index, white_chip);						
								n1 = true;	//	user choosed a pawn								
							}
						}
						break;
					}					
					//	@BLACK'S turn
					if(turn == false){
						/*if(squares[i].getIcon() == black_chip && turn == false)	
						{
							//	code here					
							blocks[index].setBackground(Color.YELLOW);
							chipsGlow(disk_index, index, black_chip);
							
							turn = !turn;
							break;						
						}else{
							//	otherwise code here
							//	@Play Sound & Display a Message
						}*/

						if(n2 == true){		//	if user previously choosed a pawn
							if(squares[i].getIcon() == null){	// user clicked an empty block
								
								//	checks if the empty block and the chosen chip is adjacent to the right
								if(index-9 == black_moves[bMovesCount-1]){
									
									//	@move the chip forward									
									squares[i].setIcon(black_chip);
									black_moves[bMovesCount++] = index;

									int temp = index-9;
									for(int a = 0; a < 32; a++){
										if(temp == k[a]){
											squares[a].setIcon(null);							
											break;
										}
									}																		
									origColor();									
									turn = !turn;
									n2 = false;									
								
								//	checks if the empty block and the chosen chip are adjecent from the left
								}else if(index-7 == black_moves[bMovesCount-1]){
									
									//	@move the chip forward									
									squares[i].setIcon(black_chip);									
									black_moves[bMovesCount++] = index;		// ---> store index of block
																		
									int temp = index-7;
									for(int a = 0; a < 32; a++){
										if(temp == k[a]){
											squares[a].setIcon(null);
											break;
										}
									}					
									origColor();									
									turn = !turn;
									n2 = false;																		
									
								}else{	//	empty block and chosen chip is not adjacent
									origColor();	//	change board colors back to green									
								}

							}else if(squares[i].getIcon() == black_chip){
								n2 = false;	// the user chose another pawn
							}else if(squares[i].getIcon() == black_chip){
								origColor();	//	change board color back to green
							}		
							
							break;
						}
						
						if(n2 == false){
							if(squares[i].getIcon() == black_chip){	
								
								black_moves[bMovesCount++] = index;
								blocks[index].setBackground(Color.YELLOW);						
								chipsGlow(disk_index, index, black_chip);						
								n2 = true;	//	user choosed a pawn								
							}
						}
						break;
					}
				}else{		//	user did not click a green block
					//	code here
					//	@Play Sound
				}
			}
		}
	}
	
	public void origColor(){
		for(int j = 0; j < 32; j++){			
			blocks[k[j]].setBackground(Color.GREEN);
		}
	}
	
	//	*Path Glow*
	public void chipsGlow(int disk_index, int index, Icon color){
		
		if(color == white_chip)
		{
			//First Forward & Backward
			boolean toggle = false;
			int temp = index-9;			
			int disk_temp = disk_index;		//	---> initialized to avoid error
						
			
			while(temp >= 0)
			{			
				for(int a = 0; a < 32; a++){
					if(temp == k[a]){
						disk_temp = a;		//	--> assigns the value of disk_temp
					}					
				}
				//	Checks if the next adjacent path is at left edge
				for(int m = 0; m < 4; m++){
					if(temp+9 == left_edge[m]){
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
					blocks[temp].setBackground(Color.CYAN);														
					break;
					
				}else if(squares[disk_temp].getIcon() == black_chip){
					
					if(squares[disk_temp-1].getIcon() == black_chip)	//	added new code!
						break;
					
					boolean edge = false;
					
					//	Loop ends if the current chosen chip is at left edge
					for(int m = 0; m < 4; m++){
						if(temp == left_edge[m]){
							edge = true;
							break;					
						}
					}
					
					//	The next immediate adjacent path glows
					if(edge == false){
						if(squares[disk_temp+1].getIcon() != null)	// added new code!
							blocks[temp].setBackground(Color.RED);							
					}
					
				}else if(squares[disk_temp].getIcon() == color)
						break;	
					
				temp-=9;				
								
			}
			
			//	Forward to the right
			boolean toggle2 = false;
			int temp2 = index-7;
			int disk_temp2 = disk_index;
									
			while(temp2 >= 0)
			{				
				for(int a = 0; a < 32; a++){
					if(temp2 == k[a]){
						disk_temp2 = a;
						break;
					}
				}
				
				//	Checks if the current chosen chip is placed at right edge
				for(int m = 0; m < 4; m++){
					if(temp2+7 == right_edge[m]){
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
					blocks[temp2].setBackground(Color.CYAN);					
					break;						
					
				}else if(squares[disk_temp2].getIcon() == black_chip)	//path is occupied and of opponent's
				{	
					if(squares[disk_temp2-1].getIcon() == black_chip)	//	added new code!
						break;
					
					boolean edge2 = false;					
					//	Loop ends if opponent's chip is at right edge
					for(int m = 0; m < 4; m++){
						if(temp2 == right_edge[m]){
							edge2 = true;
							break;					
						}
					}
					
					if(edge2 == false){
						if(squares[disk_temp2+1].getIcon() != null)	// added new code!
							blocks[temp2].setBackground(Color.RED);							
					}
						
						
				}else if(squares[disk_temp2].getIcon() == color)
						break;	//	Loop breaks if next path is occupied by ally
				
				temp2-=7;
			}
			
			/*
			
			//	First Backward to the Right
			toggle = false;
			temp = index+9;
			disk_temp = disk_index;
			
			while(temp < 64)
			{
				for(int i = 0; i < 32; i++){
					if(temp == k[i]){
						disk_temp = i;
						break;
					}
				}				
				
				for(int m = 0; m < 4; m++){
					if(temp == right_edge[m]){
						toggle = true;					
						break;
					}
				}			
				
				if(toggle == true)
					break;
				
				if(squares[disk_temp].getIcon() == black_chip){
					blocks[temp].setBackground(Color.RED);	
					//
				}
				
				/*
				if(squares[disk_temp].getIcon() == null)		//path is not occupied
				{								
					blocks[temp].setBackground(Color.CYAN);														
					break;
				}else if(squares[disk_temp].getIcon() != white_chip && squares[disk_temp].getIcon() != null){					
						blocks[temp].setBackground(Color.RED);											
				}else if(squares[disk_temp].getIcon() == color)
					break;	//	stop the search				
				
				
				temp+=9;
			}*/
		}

		if(color == black_chip){			
		
			//First Forward
			boolean toggle = false;
			int temp = index+9;			
			int disk_temp = disk_index;			
					
			while(temp >= 0){			
			
				for(int a = 0; a < 32; a++){
					if(temp == k[a]){
						disk_temp = a;
						break;
					}
				}			
				
				for(int m = 0; m < 4; m++){
					if(temp-9 == right_edge[m]){
						toggle = true;
						break;					
					}
				}
				
				if(toggle == true)				
					break;				
					
				if(squares[disk_temp].getIcon() == null)		//path is not occupied
				{								
					blocks[temp].setBackground(Color.CYAN);														
					break;
				}else if(squares[disk_temp].getIcon() == white_chip)	//path is occupied and of opponent's
				{
					blocks[temp].setBackground(Color.RED);					
					//	@@search further
				}else if(squares[disk_temp].getIcon() == color)
					break;	//	stop the search
					
				temp+=9;				
			}
			
			//	Second Forward
			boolean toggle2 = false;
			int temp2 = index+7;
			int disk_temp2 = disk_index;			
			
			while(temp2 >= 0){
				
				for(int a = 0; a < 32; a++){
					if(temp2 == k[a]){
						disk_temp2 = a;
						break;
					}
				}
				
				for(int m = 0; m < 4; m++){
					if(temp2-7 == left_edge[m]){
						toggle2 = true;
						break;					
					}
				}
				if(toggle2 == true)				
					break;				
				if(squares[disk_temp2].getIcon() == null)		//path is not occupied
				{									
					blocks[temp2].setBackground(Color.CYAN);					
					break;						
				}else if(squares[disk_temp2].getIcon() == white_chip)	//path is occupied and of opponent's
				{							
					blocks[temp2].setBackground(Color.RED);
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
					if(temp == right_edge[m]){
						toggle = true;					
						break;
					}
				}			
				if(toggle == true)
					break;
				
				if(blocks[temp].getIcon() != color && blocks[temp].getIcon() != null){
					if(blocks[temp+9].getIcon() == null){
						blocks[temp].setBackground(Color.RED);
						blocks[temp+9].setBackground(Color.CYAN);
					}					
				}else break;
				
				temp+9;
			}*/
		}
		
		
	}	
}