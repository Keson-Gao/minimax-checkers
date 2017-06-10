package app.modules.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstructionPanel extends JPanel{
		
	String[] bg = {"/HowToPlay1.png", "/HowToPlay2.png", "/HowToPlay3.png", "/HowToPlay4.png", "/HowToPlay5.png", "/HowToPlay6.png",
					"/HowToPlay7.png", "/credits1.png", "/credits2.png"};
	
	private Icon imageIcon;
	private Icon nextIcon = new ImageIcon(getClass().getResource("/next.png"));
	private Icon backIcon = new ImageIcon(getClass().getResource("/back_1.png"));
	private Icon exitIcon  = new ImageIcon(getClass().getResource("/close.png"));
	
	private JLabel imageLabel;
	public JLabel nextLabel, backLabel, exitLabel;
	
	private int tracker = 0; 
	
	public InstructionPanel(){
		
		setOpaque(false);
		setBounds(0, 0, 1000, 600);
		setLayout(null);
		
		imageIcon  = new ImageIcon(getClass().getResource(bg[0]));
		
		imageLabel = new JLabel(imageIcon);
		imageLabel.setBounds(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
		
		nextLabel = new JLabel(nextIcon);
		nextLabel.setBounds(680, 490, nextIcon.getIconWidth(), nextIcon.getIconHeight());
		
		backLabel = new JLabel(backIcon);
		backLabel.setBounds(50, 490, backIcon.getIconWidth(), backIcon.getIconHeight());
		
		exitLabel = new JLabel(exitIcon);
		exitLabel.setBounds(750, 10, exitIcon.getIconWidth(), exitIcon.getIconHeight());
		
		add(exitLabel);
		add(nextLabel);	
		add(imageLabel);			
		
	}
	
	public void next(int t){
						
		if(t > 0 && t < bg.length){
						
			remove(imageLabel);
			
			imageIcon  = new ImageIcon(getClass().getResource(bg[t]));
				
			imageLabel = new JLabel(imageIcon);
			imageLabel.setBounds(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
				
			add(backLabel);
			add(nextLabel);
			add(imageLabel);
						
		}
		
		if(t == (bg.length-1)){
			
			remove(nextLabel);						
		}
		
		repaint();
		revalidate();
	}
	
	public void back(int t){			
		
		if(t >= 0 &&  t < bg.length){
			
			remove(imageLabel);
			
			imageIcon  = new ImageIcon(getClass().getResource(bg[t]));
				
			imageLabel = new JLabel(imageIcon);
			imageLabel.setBounds(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
				
			add(backLabel);
			add(nextLabel);
			add(imageLabel);
			
			if(t == 0) remove(backLabel);	
		}
		
		repaint();
		revalidate();
	}
}