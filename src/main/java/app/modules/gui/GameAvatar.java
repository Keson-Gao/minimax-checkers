package app.modules.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameAvatar extends JPanel{
	
	private String[] imageNames = {"/serialkiller1.png", "/baymax1.png", "/xCon1.png", "/robot1.png", "/alien1.png",
			 "/chick1.png", "/starwars1.png", "/painter1.png", "/thor1.png", "/gunman11.png", "/gunman21.png",
			 "/hulk1.png", "/hero1.png", "/luigi1.png", "/greenman1.png", "/death1.png", "/ranger1.png", "/wolverine1.png",
			 "/viking1.png", "/smoker1.png"};
	
	private String[] imageNamesHover = {"/serialkiller1_2.png", "/baymax1_2.png", "/xCon1_2.png", "/robot1_2.png", "/alien1_2.png",
			 "/chick1_2.png", "/starwars1_2.png", "/painter1_2.png", "/thor1_2.png", "/gunman11_2.png", "/gunman21_2.png",
			 "/hulk1_2.png", "/hero1_2.png", "/luigi1_2.png", "/greenman1_2.png", "/death1_2.png", "/ranger1_2.png", "/wolverine1_2.png",
			 "/viking1_2.png", "/smoker1_2.png"};
	
	private Icon[] icons = new ImageIcon[20];
	public JLabel[] labels = new JLabel[20];
	
	private Icon titleIcon;
	private JLabel title;
	
	public GameAvatar(){
		
		setOpaque(false);
		setLayout(null);
		
		init();
	}
	
	public void init(){
		
		Handler h = new Handler();
		
		for(int i = 0; i < 20; i++){
			icons[i] = new ImageIcon(getClass().getResource(imageNames[i]));
			labels[i] = new JLabel(icons[i]);
			labels[i].addMouseListener(h);
			add(labels[i]);
		}
		
		titleIcon = new ImageIcon(getClass().getResource("/title.png"));
		title = new JLabel(titleIcon);
		title.setBounds(170, 60, titleIcon.getIconWidth(), titleIcon.getIconHeight());
		
		labels[0].setBounds(100, 150, 79, 79);
		labels[1].setBounds(200, 150, 79, 79);
		labels[2].setBounds(300, 150, 79, 79);
		labels[3].setBounds(400, 150, 79, 79);
		labels[4].setBounds(500, 150, 79, 79);
		labels[5].setBounds(600, 150, 79, 79);
		labels[6].setBounds(100, 250, 79, 79);
		labels[7].setBounds(200, 250, 79, 79);
		labels[8].setBounds(300, 250, 79, 79);
		labels[9].setBounds(400, 250, 79, 79);
		labels[10].setBounds(500, 250, 79, 79);
		labels[11].setBounds(600, 250, 79, 79);
		labels[12].setBounds(100, 350, 79, 79);
		labels[13].setBounds(200, 350, 79, 79);
		labels[14].setBounds(300, 350, 79, 79);
		labels[15].setBounds(400, 350, 79, 79);
		labels[16].setBounds(500, 350, 79, 79);
		labels[17].setBounds(600, 350, 79, 79);
		labels[18].setBounds(100, 450, 79, 79);
		labels[19].setBounds(200, 450, 79, 79);
		
		add(title);
		
	}
	
	public class Handler extends MouseAdapter{
		
		public void mouseEntered(MouseEvent e){
			Object source = e.getSource();
		
			for(int i = 0; i < 20; i++){
				
				if(source == labels[i]){
					labels[i].setIcon(new ImageIcon(getClass().getResource(imageNamesHover[i])));
				}
			}
			
			
		}
		
		public void mouseExited(MouseEvent e){
			Object source = e.getSource();
			
			for(int i = 0; i < 20; i++){
				
				if(source == labels[i]){
					labels[i].setIcon(new ImageIcon(getClass().getResource(imageNames[i])));
				}
			}
		}			
		
	}
}