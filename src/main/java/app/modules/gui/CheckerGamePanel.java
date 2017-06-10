package app.modules.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class CheckerGamePanel extends JPanel{	
	
	private Icon whiteTextIcon = new ImageIcon(getClass().getResource("/whitetext.png"));

	public CheckerGamePanel(){		
		setOpaque(false);	
		setLayout(null);	
		setBounds(0, 0, 1000, 600);
		
		CheckerBoard board = new CheckerBoard();
		
		if(CheckerFrame.pieceTextColor.getIcon().toString().equals(whiteTextIcon.toString())){
			board.setHumanPlayerPiece("white");								
		}else{
			board.setHumanPlayerPiece("black");
		}
		
		add(board);	
	}
}