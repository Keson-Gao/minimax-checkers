import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class CheckerGamePanel extends JPanel{	
	
	private Icon whiteTextIcon = new ImageIcon(getClass().getResource("whitetext.png"));	
	
	public CheckerGamePanel(JLabel pieceTextColor){		
		
		CheckerBoard board;
		
		init();
		
		if(pieceTextColor.getIcon().toString().equals(whiteTextIcon.toString()))
			board = new CheckerBoard("white");								
		else
			board = new CheckerBoard("black");
		
				
		add(board);	
	}
	
	public CheckerGamePanel(){		
		
		CheckerBoard board = new CheckerBoard();
		init();
				
		add(board);	
		
	}
	
	private void init(){
		
		setOpaque(false);	
		setLayout(null);	
		setBounds(0, 0, 1000, 600);
	}
}