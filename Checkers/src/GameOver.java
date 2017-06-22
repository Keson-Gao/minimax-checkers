import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOver extends JPanel{
		
	public JLabel panelTitle, ok, winnerLabel, winnerName, winnerMoves;
	public JLabel player1, player2;
	
	public Icon panelTitleIcon, okIcon, winnerIcon;	
	public Icon player1Icon, player2Icon;
	
	public GameOver(){
		
		setOpaque(false);
		setLayout(null);
		setBounds(0, 0, 1000, 600);
		
		panelTitleIcon = new ImageIcon(getClass().getResource("GameOver.png"));
		okIcon = new ImageIcon(getClass().getResource("gameoverok.png"));
		
		
		panelTitle = new JLabel(panelTitleIcon);
		panelTitle.setBounds(210, 60, panelTitleIcon.getIconWidth(), panelTitleIcon.getIconHeight());
		
		ok = new JLabel(okIcon);
		ok.setBounds(340, 470, okIcon.getIconWidth(), okIcon.getIconHeight());
		ok.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				ok.setIcon(new ImageIcon(getClass().getResource("gameoverok2.png")));
			}
			
			public void mouseExited(MouseEvent e){
				ok.setIcon(okIcon);
			}						
		});
				
		add(panelTitle);
		add(ok);
	}
	
	public void setWinner(Icon winnerIcon, String wName, int wMoves){
				
		winnerLabel = new JLabel(winnerIcon);
		winnerLabel.setBounds(310, 200, winnerIcon.getIconWidth(), winnerIcon.getIconHeight());		
		
		winnerName = new JLabel();
		winnerName.setFont(new Font("Cambria", Font.BOLD, 20));		
		
		int plusCoor = 0;
		
		if(wName.length() < 13){			
			plusCoor = 12 - wName.length();
		
		}else{
						
			wName = wName.substring(0, 10) + "...";
		}				
		
		winnerName.setText(wName + " wins!");
		winnerName.setBounds(300 + plusCoor*5, 110, 550, 100);
		
		winnerMoves = new JLabel();
		winnerMoves.setFont(winnerName.getFont());
		winnerMoves.setText("in " + wMoves + " moves");
		winnerMoves.setBounds(345, 370, 500, 100);
		
		add(this.winnerName);
		add(winnerLabel);		
		add(winnerMoves);
	}
	
}