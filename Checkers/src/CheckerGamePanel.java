import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class CheckerGamePanel extends JPanel{			
	private static final long serialVersionUID = 1L;

	public CheckerGamePanel(){		
		setOpaque(false);	
		setLayout(null);	
		setBounds(0, 0, 1000, 600);
		
		CheckerBoard board = new CheckerBoard();		
		add(board);	
	}
}