import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePausedPanel extends JPanel{
	private static final long serialVersionUID = 1L;	
	
	private Icon titleIcon = new ImageIcon(getClass().getResource("gamePaused.png"));
	private Icon infoIcon = new ImageIcon(getClass().getResource("gamehelp.png"));
	private Icon optionsIcon = new ImageIcon(getClass().getResource("gameoptions.png"));
	private Icon resignIcon = new ImageIcon(getClass().getResource("resign.png")); 
	public Icon backToMainIcon = new ImageIcon(getClass().getResource("gameback.png"));
	private Icon closeIcon = new ImageIcon(getClass().getResource("close.png"));
	
	public JLabel title, info, options, resign, backToMain, close;
	int num = 0;
	
	public GamePausedPanel() {
		
		setOpaque(true);
		setLayout(null);
		setBackground(Color.ORANGE);
		
		title = new JLabel(titleIcon);
		title.setBounds(100, 50, titleIcon.getIconWidth(), titleIcon.getIconHeight());
		
		info = new JLabel(infoIcon);
		info.setBounds(90, 150, infoIcon.getIconWidth(), infoIcon.getIconHeight());
		
				
		
		options = new JLabel(optionsIcon);
		options.setBounds(180, 150, optionsIcon.getIconWidth(), optionsIcon.getIconHeight());
		options.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				options.setIcon(new ImageIcon(getClass().getResource("gameoptions2.png")));
			}
			
			public void mouseExited(MouseEvent e){
				options.setIcon(optionsIcon);
			}
		});
		
		resign = new JLabel(resignIcon);
		resign.setBounds(270, 150, resignIcon.getIconWidth(), resignIcon.getIconHeight());
		resign.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				resign.setIcon(new ImageIcon(getClass().getResource("resign2.png")));				
			}
			
			public void mouseExited(MouseEvent e){
				resign.setIcon(resignIcon);
			}
		});
		
		backToMain = new JLabel(backToMainIcon);
		backToMain.setBounds(360, 150, backToMainIcon.getIconWidth(), backToMainIcon.getIconHeight());
		
		
		close = new JLabel(closeIcon);
		close.setBounds(460, 15, closeIcon.getIconWidth(), closeIcon.getIconHeight());
		close.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				close.setIcon(new ImageIcon(getClass().getResource("close2.png")));
			}
			
			public void mouseExited(MouseEvent e){
				close.setIcon(closeIcon);
			}
			
			public void mouseClicked(MouseEvent e){
				CheckerFrame.glass.setVisible(false);
			}
		});
		
		add(title);
		add(info);   
		add(options);
		add(resign);
		add(backToMain);
		add(close);
	}
}
