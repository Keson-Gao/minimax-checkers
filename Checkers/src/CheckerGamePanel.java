import javax.swing.JPanel;

public class CheckerGamePanel extends JPanel{	
	
	public CheckerGamePanel(PieceColor pieceTextColor){				
		init();		
		CheckerBoard board = new CheckerBoard((pieceTextColor == PieceColor.WHITE)? PieceColor.WHITE : PieceColor.BLACK);										
		add(board);	
	}
	
	public CheckerGamePanel(){				
		init();
		BoardTwoPlayer board = new BoardTwoPlayer();						
		add(board);			
	}
	
	private void init(){		
		setOpaque(false);	
		setLayout(null);	
		setBounds(0, 0, 1000, 600);
	}
}