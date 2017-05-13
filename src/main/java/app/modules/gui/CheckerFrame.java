import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class CheckerFrame extends JFrame{
		
	private Icon play_icon1 = new ImageIcon(getClass().getResource("ClickHereToPlay1.jpg"));	//	mouse out
	private Icon play_icon2 = new ImageIcon(getClass().getResource("ClickHereToPLay2.jpg"));	//	mouse hover
	private Icon play_icon3 = new ImageIcon(getClass().getResource("ClickHereToPLay3.jpg"));	//	mouse click/pressed		
	private Icon options1_icon1 = new ImageIcon(getClass().getResource("OPTIONS_1.jpg"));		
	private Icon quit1_icon1 = new ImageIcon(getClass().getResource("QUIT_1.jpg"));		
	private Icon backToMain_icon = new ImageIcon(getClass().getResource("BACKTOMENU1.jpg"));
	private Icon bg1 = new ImageIcon(getClass().getResource("backdrop00.jpg"));
	
	private JLabel playGame;		
	private JLabel backToMain;
	private JLabel options1;
	private JLabel options2;
	private JLabel instructions;
	private JLabel quit1;
	private JLabel quit2;
	private JLabel newGame;
	
	
	private JPanel menu;
	
	CheckerFrame()
	{
		super("Checker");		
		setContentPane(new JLabel(new ImageIcon("backdrop00.jpg")));				
		setLayout(null);		
		setBounds(300, 100, bg1.getIconWidth(), bg1.getIconHeight());
		
		//play background music
		/*
		try
		{
			URL url = this.getClass().getClassLoader().getResource("bgsound.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch(UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(LineUnavailableException e)
		{
			e.printStackTrace();
		}*/
		
		menu = new JPanel();
		menu.setBounds(0, 0, 1000, 600);
		menu.setOpaque(false);
		menu.setLayout(null);
		
		playGame = new JLabel(play_icon1);
		playGame.setBounds(220, 380, play_icon1.getIconWidth(), play_icon1.getIconHeight());
		
		playGame.addMouseListener(
			new MouseListener(){
				public void mousePressed(MouseEvent e){
					playGame.setIcon(play_icon3);
				}				
				public void mouseEntered(MouseEvent e){
					playGame.setIcon(play_icon2);
				}
				public void mouseExited(MouseEvent e)
				{
					playGame.setIcon(play_icon1);
				}				
				public void mouseReleased(MouseEvent e)
				{
					playGame.setIcon(play_icon1);
				}
				public void mouseClicked(MouseEvent e)
				{									
					getContentPane().removeAll();
					setContentPane(new JLabel(new ImageIcon("bkg4.jpg")));			
						
					//Add the game board to the frame.
					CheckerGamePanel game = new CheckerGamePanel();					
					add(game);
					
					repaint();
					
					backToMain = new JLabel(backToMain_icon);
					backToMain.setBounds(30, 45, 179, 29);
					backToMain.addMouseListener(
						new MouseListener(){
							public void mouseEntered(MouseEvent e){
								backToMain.setIcon(new ImageIcon(getClass().getResource("BACKTOMENU2.jpg")));
							}
							public void mousePressed(MouseEvent e){
								backToMain.setIcon(new ImageIcon(getClass().getResource("BACKTOMENU3.jpg")));								
							}
							public void mouseExited(MouseEvent e){
								backToMain.setIcon(new ImageIcon(getClass().getResource("BACKTOMENU1.jpg")));
							}
							public void mouseReleased(MouseEvent e){
								backToMain.setIcon(new ImageIcon(getClass().getResource("BACKTOMENU1.jpg")));
							}							
							public void mouseClicked(MouseEvent e){
								getContentPane().removeAll();
								setContentPane(new JLabel(new ImageIcon("backdrop00.jpg")));
								add(menu);
								repaint();
								validate();
							}
					});
					
					newGame = new JLabel(new ImageIcon("NEWGAME1.jpg"));
					newGame.setBounds(30, 90, 179, 29);
					newGame.addMouseListener(new MouseListener(){
						public void mouseEntered(MouseEvent e){
							newGame.setIcon(new ImageIcon("NEWGAME2.jpg"));
						}
						public void mouseExited(MouseEvent e){
							newGame.setIcon(new ImageIcon("NEWGAME1.jpg"));
						}
						public void mousePressed(MouseEvent e){
							newGame.setIcon(new ImageIcon("NEWGAME3.jpg"));
						}
						public void mouseReleased(MouseEvent e){
							newGame.setIcon(new ImageIcon("NEWGAME1.jpg"));
						}
						public void mouseClicked(MouseEvent	e){															
							getContentPane().removeAll();
							CheckerBoard newBoard = new CheckerBoard();
							add(newBoard);																
							newBoard.turn = true;
							
							add(backToMain);
							add(newGame);
							add(instructions);
							add(options2);
							add(quit2);		
							
							repaint();
							validate();
						}
					});
					
					instructions = new JLabel(new ImageIcon("INSTRUCTIONS1.jpg"));
					instructions.setBounds(30, 135, 179, 29);
					instructions.addMouseListener(new MouseListener(){
						public void mouseEntered(MouseEvent e){
							instructions.setIcon(new ImageIcon("INSTRUCTIONS2.jpg"));
						}
						public void mouseExited(MouseEvent e){
							instructions.setIcon(new ImageIcon("INSTRUCTIONS1.jpg"));
						}
						public void mousePressed(MouseEvent e){
							instructions.setIcon(new ImageIcon("INSTRUCTIONS3.jpg"));
						}
						public void mouseReleased(MouseEvent e){
							instructions.setIcon(new ImageIcon("INSTRUCTIONS1.jpg"));
						}
						public void mouseClicked(MouseEvent	e){
							//code here
						}
					});
					
					options2 = new JLabel(new ImageIcon("OPTIONS1.jpg"));
					options2.setBounds(30, 180, 179, 29);
					options2.addMouseListener(new MouseListener(){
						public void mouseEntered(MouseEvent e){
							options2.setIcon(new ImageIcon("OPTIONS2.jpg"));
						}
						public void mouseExited(MouseEvent e){
							options2.setIcon(new ImageIcon("OPTIONS1.jpg"));
						}
						public void mousePressed(MouseEvent e){
							options2.setIcon(new ImageIcon("OPTIONS3.jpg"));
						}
						public void mouseReleased(MouseEvent e){
							options2.setIcon(new ImageIcon("OPTIONS1.jpg"));
						}
						public void mouseClicked(MouseEvent	e){
							//code here
						}
					});
					
					quit2 = new JLabel(new ImageIcon("QUIT1.jpg"));
					quit2.setBounds(30, 230, 179, 29);
					quit2.addMouseListener(new MouseListener(){
						public void mouseEntered(MouseEvent e){
							quit2.setIcon(new ImageIcon("QUIT2.jpg"));
						}
						public void mouseExited(MouseEvent e){
							quit2.setIcon(new ImageIcon("QUIT1.jpg"));
						}
						public void mousePressed(MouseEvent e){
							quit2.setIcon(new ImageIcon("QUIT3.jpg"));
						}
						public void mouseReleased(MouseEvent e){
							quit2.setIcon(new ImageIcon("QUIT1.jpg"));
						}
						public void mouseClicked(MouseEvent	e){
							//code here
						}
					});
					
					
					game.add(backToMain);
					game.add(newGame);
					game.add(instructions);
					game.add(options2);
					game.add(quit2);					
					validate();
				}				
			}
		);
		
		options1 = new JLabel(options1_icon1);
		options1.setBounds(220, 433, options1_icon1.getIconWidth(), 48);
		options1.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){
				options1.setIcon(new ImageIcon("OPTIONS_2.jpg"));
			}
			public void mouseExited(MouseEvent e){
				options1.setIcon(options1_icon1);
			}
			public void mousePressed(MouseEvent e){
				options1.setIcon(new ImageIcon("OPTIONS_3.jpg"));
			}
			public void mouseReleased(MouseEvent e){
				options1.setIcon(options1_icon1);
			}
			public void mouseClicked(MouseEvent e){
				//code here
			}
		});
		
		quit1 = new JLabel(quit1_icon1);
		quit1.setBounds(462, 433, quit1_icon1.getIconWidth(), 48);
		quit1.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){
				quit1.setIcon(new ImageIcon("QUIT_2.jpg"));
			}
			public void mouseExited(MouseEvent e){
				quit1.setIcon(quit1_icon1);
			}
			public void mousePressed(MouseEvent e){
				quit1.setIcon(new ImageIcon("QUIT_3.jpg"));
			}
			public void mouseReleased(MouseEvent e){
				quit1.setIcon(quit1_icon1);
			}
			public void mouseClicked(MouseEvent e){
				//code here
			}
		});
		
		menu.add(playGame);
		menu.add(options1);
		menu.add(quit1);
		
		add(menu);		
	}
	
	public static void main(String[] args)
	{
		CheckerFrame frame = new CheckerFrame();		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setResizable(false);
	}
}
