import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class CheckerFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private Icon play_icon1 = new ImageIcon(getClass().getResource("play.png"));	
	private Icon play_icon2 = new ImageIcon(getClass().getResource("pLay2.png"));		
	private Icon options1_icon1 = new ImageIcon(getClass().getResource("settings.png"));		
	private Icon info_icon1 = new ImageIcon(getClass().getResource("help1.png"));				
	private Icon titleIcon = new ImageIcon(getClass().getResource("dama.png"));
	
	private Icon selectModeIcon = new ImageIcon(getClass().getResource("selectMode.png"));
	private Icon onePlayerIcon = new ImageIcon(getClass().getResource("1p.png"));
	private Icon onePlayerIcon2 = new ImageIcon(getClass().getResource("1p_2.png"));
	private Icon twoPlayerIcon = new ImageIcon(getClass().getResource("2p.png"));
	private Icon twoPlayerIcon2 = new ImageIcon(getClass().getResource("2p_2.png"));
	private Icon backToMainIcon = new ImageIcon(getClass().getResource("back1.png"));
	private Icon backToMainIcon2 = new ImageIcon(getClass().getResource("back2.png"));		
	private Icon gameMenuIcon = new ImageIcon(getClass().getResource("menu.png")); 
	private Icon aiPlayerIcon = new ImageIcon(getClass().getResource("bot.png"));
	private Icon humanPlayerIcon = new ImageIcon(getClass().getResource("human.png"));	
	private Icon vsIcon = new ImageIcon(getClass().getResource("versus.png"));
	private Icon blackTextIcon = new ImageIcon(getClass().getResource("blacktext.png"));
	private Icon whiteTextIcon = new ImageIcon(getClass().getResource("whitetext.png"));
	private Icon gamePlay2pIcon = new ImageIcon(getClass().getResource("go.png"));		
	
	private JLabel gameTitle, selectMode;
	private JLabel playGame, onePlayer, twoPlayer, boardBorder, gameMenu;
	private JLabel humanPlayer, aiPlayer, humanPlayer1, humanPlayer2, human1, human2, versus;
	private JLabel backToMain, blackText, whiteText;
	private JLabel gameBack2p, gamePlay2p, playerTurn;
	private JLabel options1;		
	private JLabel info;	
			
	GameAvatar avatar1 = new GameAvatar();
	GameAvatar avatar2 = new GameAvatar();
	
	private String[] imageNames = {"serialKiller2.png", "baymax2.png", "xCon2.png", "robot2.png", "alien2.png", 
			 "chick2.png", "starwars2.png", "painter2.png", "thor2.png", "gunman12.png", "gunman22.png",
			 "hulk2.png", "hero2.png", "luigi2.png", "greenman2.png", "death2.png", "ranger2.png", "wolverine2.png",
			 "viking2.png", "smoker2.png"};
	
	private String[] imageNamesHover = {"serialKiller2_2.png", "baymax2_2.png", "xCon2_2.png", "robot2_2.png", "alien2_2.png", 
			 "chick2_2.png", "starwars2_2.png", "painter2_2.png", "thor2_2.png", "gunman12_2.png", "gunman22_2.png",
			 "hulk2_2.png", "hero2_2.png", "luigi2_2.png", "greenman2_2.png", "death2_2.png", "ranger2_2.png", "wolverine2_2.png",
			 "viking2_2.png", "smoker2_2.png"};
	
	private String human1Icon, human1Icon_2, 
				   human2Icon, human2Icon_2;
	
	private String whiteTurn ="whiteturn.png", blackTurn = "blackturn.png";
	public String turnImage = whiteTurn;
	private JTextField human1Name, human2Name;
	
	public static FixedGlassPane glass;
	private GamePausedPanel gamePaused;
	
	private JPanel menu, mode, character;
	
	CheckerFrame()
	{
		super("Checker");
		getContentPane().setBackground(Color.ORANGE);				
		setLayout(null);		
		setBounds(300, 100, 800, 600);
		
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
		
		gameTitle = new JLabel(titleIcon);
		gameTitle.setBounds(215, 160, titleIcon.getIconWidth(), titleIcon.getIconHeight());		
		
		playGame = new JLabel(play_icon1);
		playGame.setBounds(325, 280, play_icon1.getIconWidth(), play_icon1.getIconHeight());
		
		playGame.addMouseListener(
			new MouseListener(){
				public void mousePressed(MouseEvent e){
					playGame.setIcon(play_icon2);
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
						
					mode = new JPanel();
					mode.setBounds(0, 0, 1000, 600);
					mode.setOpaque(false);
					mode.setLayout(null);
					
					selectMode = new JLabel(selectModeIcon);
					selectMode.setBounds(180, 40, selectModeIcon.getIconWidth(), selectModeIcon.getIconHeight());
					
					onePlayer = new JLabel(onePlayerIcon);
					onePlayer.setBounds(150, 160, onePlayerIcon.getIconWidth(), onePlayerIcon.getIconHeight());
					onePlayer.addMouseListener(new MouseAdapter(){
						public void mouseEntered(MouseEvent e){
							onePlayer.setIcon(onePlayerIcon2);
							onePlayer.setBounds(46, 160, onePlayerIcon2.getIconWidth(), onePlayerIcon2.getIconHeight());
						}
						
						public void mouseExited(MouseEvent e){
							onePlayer.setIcon(onePlayerIcon);
							onePlayer.setBounds(150, 160, onePlayerIcon.getIconWidth(), onePlayerIcon.getIconHeight());
						}
						
						public void mouseClicked(MouseEvent e){
							getContentPane().removeAll();
							
							Icon icon = new ImageIcon(getClass().getResource("greenbg.png"));
							JLabel bg = new JLabel(icon);
							bg.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
							
							CheckerGamePanel game = new CheckerGamePanel();
							
							boardBorder = new JLabel(new ImageIcon(getClass().getResource("border.png")));
							boardBorder.setBounds(147, 33, 497, 497);																											
														
							humanPlayer = new JLabel(humanPlayerIcon);							
							aiPlayer = new JLabel(aiPlayerIcon);		
							humanPlayer.setBounds(675, 440, humanPlayerIcon.getIconWidth(), humanPlayerIcon.getIconHeight());
							aiPlayer.setBounds(30, 20, aiPlayerIcon.getIconWidth(), aiPlayerIcon.getIconHeight());
							
							gameMenu = new JLabel(gameMenuIcon);
							gameMenu.setBounds(40, 470, gameMenuIcon.getIconWidth(), gameMenuIcon.getIconHeight());	
							gameMenu.addMouseListener(new MouseAdapter(){
								public void mouseClicked(MouseEvent e){
									glass.setVisible(true);
								}
							});
							
							
							add(humanPlayer);
							add(aiPlayer);							
							add(game);
							add(boardBorder);
							add(gameMenu);														
							add(bg);
							
							repaint();
							revalidate();
						}
						
					});
					
					twoPlayer = new JLabel(twoPlayerIcon);
					twoPlayer.setBounds(160, 320, twoPlayerIcon.getIconWidth(), twoPlayerIcon.getIconHeight());
					
					twoPlayer.addMouseListener(new MouseAdapter(){
						public void mouseEntered(MouseEvent e){
							twoPlayer.setIcon(twoPlayerIcon2);
							twoPlayer.setBounds(160, 320, twoPlayerIcon2.getIconWidth(), twoPlayerIcon2.getIconHeight());
						}
						
						public void mouseExited(MouseEvent e){
							twoPlayer.setIcon(twoPlayerIcon);
							twoPlayer.setBounds(160, 320, twoPlayerIcon.getIconWidth(), twoPlayerIcon.getIconHeight());
						}
						
						public void mouseClicked(MouseEvent e){
							getContentPane().removeAll();
							
							human1Icon = "human12.png"; human1Icon_2 = "human12_2.png"; 
							human2Icon = "human22.png"; human2Icon_2 = "human22_2.png";
							
							character = new JPanel();
							character.setBounds(0, 0, 1000, 600);
							character.setOpaque(false);
							character.setLayout(null);
							
							human1 = new JLabel(new ImageIcon(getClass().getResource(human1Icon)));
							human1.setBounds(170, 170, 177, 177);
							human1.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									human1.setIcon(new ImageIcon(getClass().getResource(human1Icon_2)));
								}
								
								public void mouseExited(MouseEvent e){
									human1.setIcon(new ImageIcon(getClass().getResource(human1Icon)));
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();
									getContentPane().setBackground(Color.ORANGE);
																		
									avatar1.setBounds(0, 0, 1000, 600);
									AvatarHandler h = new AvatarHandler();

									for(int i = 0; i < 20; i++){										
										avatar1.labels[i].addMouseListener(h);										
									}
									
									add(avatar1);
									repaint();
									revalidate();
								}
							});
							
							human2 = new JLabel(new ImageIcon(getClass().getResource(human2Icon)));
							human2.setBounds(450, 170, 177, 177);
							human2.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									human2.setIcon(new ImageIcon(getClass().getResource(human2Icon_2)));
								}
								
								public void mouseExited(MouseEvent e){
									human2.setIcon(new ImageIcon(getClass().getResource(human2Icon)));
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();
									getContentPane().setBackground(Color.ORANGE);
																		
									avatar2.setBounds(0, 0, 1000, 600);
									AvatarHandler h = new AvatarHandler();

									for(int i = 0; i < 20; i++){										
										avatar2.labels[i].addMouseListener(h);										
									}
									
									add(avatar2);
									repaint();
									revalidate();
								}
							});
							
							versus = new JLabel(vsIcon);
							versus.setBounds(350, 270, vsIcon.getIconWidth(), vsIcon.getIconHeight());		
							
							blackText = new JLabel(blackTextIcon);
							blackText.setBounds(160, 120, blackTextIcon.getIconWidth(), blackTextIcon.getIconHeight());
							
							whiteText = new JLabel(whiteTextIcon);
							whiteText.setBounds(430, 120, whiteTextIcon.getIconWidth(), whiteTextIcon.getIconHeight());							
							
							human1Name = new JTextField();
							human1Name.setBounds(160, 370, 180, 50);
							human1Name.setText(" Player Name");
							human1Name.setFont(new Font("Consolas", Font.PLAIN, 16));
							human1Name.addFocusListener(new FocusListener(){
								public void focusGained(FocusEvent e){
									if(human1Name.getText().trim().equals("Player Name"))
										human1Name.setText(" ");
								}
								
								public void focusLost(FocusEvent e){
									if(human1Name.getText().trim().equals("")){
										human1Name.setText(" Player Name");
									}
								}
							});
							
							human2Name = new JTextField();
							human2Name.setBounds(450, 370, 180, 50);
							human2Name.setText(" Player Name");
							human2Name.setFont(new Font("Consolas", Font.PLAIN, 16));
							human2Name.addFocusListener(new FocusListener(){
								public void focusGained(FocusEvent e){
									if(human2Name.getText().trim().equals("Player Name"))
										human2Name.setText(" ");
								}
								
								public void focusLost(FocusEvent e){
									if(human2Name.getText().trim().equals("")){
										human2Name.setText(" Player Name");
									}
								}
							});
							
							gameBack2p = new JLabel(backToMainIcon);
							gameBack2p.setBounds(40, 480, backToMainIcon.getIconWidth(), backToMainIcon.getIconHeight());
							gameBack2p.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									gameBack2p.setIcon(backToMainIcon2);
								}
								
								public void mouseExited(MouseEvent e){
									gameBack2p.setIcon(backToMainIcon);
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();									
									add(mode);
									repaint();
									revalidate();
								}
							});
							
							gamePlay2p = new JLabel(gamePlay2pIcon);
							gamePlay2p.setBounds(670, 480, gamePlay2pIcon.getIconWidth(), gamePlay2pIcon.getIconHeight());
							gamePlay2p.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									gamePlay2p.setIcon(new ImageIcon(getClass().getResource("go_2.png")));
								}
								
								public void mouseExited(MouseEvent e){
									gamePlay2p.setIcon(gamePlay2pIcon);
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();
									
									Icon icon = new ImageIcon(getClass().getResource("greenbg.png"));
									JLabel bg = new JLabel(icon);
									bg.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
									
									CheckerGamePanel game = new CheckerGamePanel();
									
									boardBorder = new JLabel(new ImageIcon(getClass().getResource("border.png")));
									boardBorder.setBounds(147, 33, 497, 497);																											
																							
									
									String hum1 =  human1Icon.substring(0, human1Icon.length()-5) + "1.png";									
									String hum2 = human2Icon.substring(0, human2Icon.length()-5) + "1.png";
										
									humanPlayer1 = new JLabel(new ImageIcon(getClass().getResource(hum1)));	
									humanPlayer2 = new JLabel(new ImageIcon(getClass().getResource(hum2)));
																																	
									humanPlayer1.setBounds(30, 20, 80, 80);
									humanPlayer2.setBounds(675, 440, 80, 80);
									
									gameMenu = new JLabel(gameMenuIcon);
									gameMenu.setBounds(40, 470, gameMenuIcon.getIconWidth(), gameMenuIcon.getIconHeight());	
									gameMenu.addMouseListener(new MouseAdapter(){
										public void mouseEntered(MouseEvent e){
											gameMenu.setIcon(new ImageIcon(getClass().getResource("menu_2.png")));
										}
										
										public void mouseExited(MouseEvent e){
											gameMenu.setIcon(gameMenuIcon);
										}
										
										public void mouseClicked(MouseEvent e){
											glass.setVisible(true);
										}
									});
									
									playerTurn = new JLabel(new ImageIcon(getClass().getResource(turnImage)));
									playerTurn.setBounds(695, 30, 59, 69);							
									
									add(playerTurn);									
									add(humanPlayer1);
									add(humanPlayer2);							
									add(game);
									add(boardBorder);
									add(gameMenu);														
									add(bg);
									
									repaint();
									revalidate();
								}
							});
														
							character.add(human1);
							character.add(human2);
							character.add(versus);
							character.add(blackText);
							character.add(whiteText);
							character.add(human1Name);
							character.add(human2Name);
							character.add(gameBack2p);
							character.add(gamePlay2p);
							
							add(character);
							
							repaint();
							revalidate();
							
						}
						
					});
					
					backToMain =  new JLabel(backToMainIcon);
					backToMain.setBounds(680, 480, backToMainIcon.getIconWidth(), backToMainIcon.getIconHeight());
					backToMain.addMouseListener(new MouseAdapter(){
						public void mouseEntered(MouseEvent e){							
							backToMain.setIcon(backToMainIcon2);
						}
						
						public void mouseExited(MouseEvent e){							
							backToMain.setIcon(backToMainIcon);
						}
						
						public void mouseClicked(MouseEvent e){
							getContentPane().removeAll();
							add(menu);
							repaint();
							revalidate();
						}
					});
								
					mode.add(selectMode);
					mode.add(onePlayer);
					mode.add(twoPlayer);
					mode.add(backToMain);
					add(mode);
					
					repaint();
				}				
			}
		);
		
		options1 = new JLabel(options1_icon1);
		options1.setBounds(200, 310, options1_icon1.getIconWidth(),options1_icon1.getIconHeight());
		options1.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				options1.setIcon(new ImageIcon(getClass().getResource("settings2.png")));
			}
			
			public void mouseExited(MouseEvent e){
				options1.setIcon(options1_icon1);
			}
			
			public void mouseClicked(MouseEvent e){
				//code here.
			}
		});
		
		info = new JLabel(info_icon1);
		info.setBounds(500, 310, info_icon1.getIconWidth(), info_icon1.getIconHeight());
		info.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				info.setIcon(new ImageIcon(getClass().getResource("help2.png")));
			}
			
			public void mouseExited(MouseEvent e){
				info.setIcon(info_icon1);
			}
			
			public void mouseClicked(MouseEvent e){
				//code here
			}
		});
				
		gamePaused = new GamePausedPanel();
		gamePaused.setBounds(145, 120, 500, 300);
		
		gamePaused.backToMain.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				gamePaused.backToMain.setIcon(new ImageIcon(getClass().getResource("gameback2.png")));				
			}
			
			public void mouseExited(MouseEvent e){
				gamePaused.backToMain.setIcon(gamePaused.backToMainIcon);
			}
			
			public void mouseClicked(MouseEvent e){
				getContentPane().removeAll();
				getContentPane().setBackground(Color.ORANGE);
				glass.setVisible(false);
				
				add(mode);
				getContentPane().repaint();
				revalidate();
			}
		});
		
		glass = new FixedGlassPane(getJMenuBar(), getContentPane());
	    glass.setLayout(null);
	    glass.setOpaque(false);
	    glass.setBackground(Color.GRAY);
	    glass.add(gamePaused);
	     
	    setGlassPane(glass);
		
		menu.add(gameTitle);
		menu.add(playGame);
		menu.add(options1);
		menu.add(info);
		
		add(menu);		
	}
	
	public class AvatarHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			Object source = e.getSource();
			getContentPane().removeAll();
			
			int i;
			for(i = 0; i < 20; i++){
				if(source == avatar2.labels[i]){
										
					human2Icon = imageNames[i];
					human2Icon_2 = imageNamesHover[i];					
					human2.setIcon(new ImageIcon(getClass().getResource(human2Icon)));
					break;
				}
			}
			
			if(i == 20){
				
				for(int j = 0; j < 20; j++){
					if(source == avatar1.labels[j]){
											
						human1Icon = imageNames[j];
						human1Icon_2 = imageNamesHover[j];					
						human1.setIcon(new ImageIcon(getClass().getResource(human1Icon)));
						break;
					}
				}
				
			}			
						
			add(character);
			repaint();
			revalidate();
			
		}
	}
	
	
	public static void main(String[] args)
	{
		CheckerFrame frame = new CheckerFrame();		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setResizable(false);
	}
}
