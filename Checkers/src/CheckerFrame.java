import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CheckerFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private Icon[] icon;	
	private Icon humanBlackIcon, humanWhiteIcon, icon1, icon2, humanBlackIcon_2, humanWhiteIcon_2;
	private Icon aiWins = new ImageIcon(getClass().getResource("robotwins.png"));
	private static JLabel aiWinsLabel = new JLabel();
	
	
	private String[] iconImage = {"play.png", "pLay2.png", "settings.png", "help1.png", "dama.png", "selectMode.png", "1p.png", "1p_2.png",
								"2p.png", "2p_2.png", "back1.png", "back2.png", "menu.png", "bot.png", "blacktext.png", "whitetext.png",
								"go.png", "human.png", "versus.png"};
	
	private String[] imageNames = {"serialKiller2.png", "baymax2.png", "xCon2.png", "robot2.png", "alien2.png", "chick2.png", "starwars2.png", 
								"painter2.png", "thor2.png", "gunman12.png", "gunman22.png", "hulk2.png", "hero2.png", "luigi2.png", "greenman2.png", 
								"death2.png", "ranger2.png", "wolverine2.png", "viking2.png", "smoker2.png"};
	
	private String[] imageNamesHover = {"serialKiller2_2.png", "baymax2_2.png", "xCon2_2.png", "robot2_2.png", "alien2_2.png", "chick2_2.png", 
								"starwars2_2.png", "painter2_2.png", "thor2_2.png", "gunman12_2.png", "gunman22_2.png", "hulk2_2.png", "hero2_2.png", 
								"luigi2_2.png", "greenman2_2.png", "death2_2.png", "ranger2_2.png", "wolverine2_2.png", "viking2_2.png", "smoker2_2.png"};
	
	private String human1Icon, human1Icon_2, human2Icon, human2Icon_2, humanBlack, humanBlack_2, 
	   				humanWhite, humanWhite_2, humanIcon;
	private String whiteTurn = "whiteturn.png", turnImage = whiteTurn;	
	
	private static JPanel gamePanel, menu, mode, character;	
	private static JLabel gameTitle, gameMenu, selectMode, boardBorder, playGame, humanPlayer, aiPlayer,
				   infoButton, onePlayer, twoPlayer, playerOne, playerTwo,
				   versusLabel, backToMain, blackText, whiteText, gameBackOnePlayer, gamePlayOnePlayer, 
				   gameBackTwoPlayer, gamePlayTwoPlayer;
	private static JLabel playerOneAvatar, playerTwoAvatar, pieceTextColor;

	static JLabel playerTurn;		
	private static JTextField human1Name, human2Name;	
	private static Container container;	
	
	static FixedGlassPane glass;
	CheckerGamePanel game; 		
	GamePausedPanel gamePaused;	
	GameAvatar avatar = new GameAvatar();
	GameAvatar avatar1 = new GameAvatar();
	GameAvatar avatar2 = new GameAvatar();
	private static int t = 0;
	
	private void init(){
		
		icon = new Icon[iconImage.length];
		for(int i = 0; i < iconImage.length; i++){
			icon[i] = new ImageIcon(getClass().getResource(iconImage[i]));
		}
		aiWinsLabel.setIcon(aiWins);
	}
	
	CheckerFrame()
	{
		super("DAMA");
		getContentPane().setBackground(Color.ORANGE);				
		setLayout(null);		
		setBounds(300, 100, 800, 600);		
		
		setIconImage(new ImageIcon(getClass().getResource("Logo.png")).getImage());
		init();				
		
		menu = new JPanel();
		menu.setBounds(0, 0, 1000, 600);
		menu.setOpaque(false);
		menu.setLayout(null);
		
		gameTitle = new JLabel(icon[4]);
		gameTitle.setBounds(215, 160, icon[4].getIconWidth(), icon[4].getIconHeight());		
		
		playGame = new JLabel(icon[0]);
		playGame.setBounds(250, 280, icon[0].getIconWidth(), icon[0].getIconHeight());
		
		playGame.addMouseListener(
			new MouseListener(){
				public void mousePressed(MouseEvent e){
					playGame.setIcon(icon[1]);
				}				
				public void mouseEntered(MouseEvent e){
					playGame.setIcon(icon[1]);
				}
				public void mouseExited(MouseEvent e)
				{
					playGame.setIcon(icon[0]);
				}				
				public void mouseReleased(MouseEvent e)
				{
					playGame.setIcon(icon[0]);
				}
				public void mouseClicked(MouseEvent e)
				{														
					
					getContentPane().removeAll();
					
					mode = new JPanel();
					mode.setBounds(0, 0, 1000, 600);
					mode.setOpaque(false);
					mode.setLayout(null);
					
					selectMode = new JLabel(icon[5]);
					selectMode.setBounds(180, 40, icon[5].getIconWidth(), icon[5].getIconHeight());
					
					onePlayer = new JLabel(icon[6]);
					onePlayer.setBounds(150, 160, icon[6].getIconWidth(), icon[6].getIconHeight());
					onePlayer.addMouseListener(new MouseAdapter(){
						public void mouseEntered(MouseEvent e){
							onePlayer.setIcon(icon[7]);
							onePlayer.setBounds(46, 160, icon[7].getIconWidth(), icon[7].getIconHeight());
						}
						
						public void mouseExited(MouseEvent e){
							onePlayer.setIcon(icon[6]);
							onePlayer.setBounds(150, 160, icon[6].getIconWidth(), icon[6].getIconHeight());
						}
						
						public void mouseReleased(MouseEvent e){
							onePlayer.setIcon(icon[6]);
							onePlayer.setBounds(150, 160, icon[6].getIconWidth(), icon[6].getIconHeight());
						}
						
						public void mouseClicked(MouseEvent e){
							getContentPane().removeAll();
							
							humanBlack = "human12.png"; humanBlack_2 = "human12_2.png"; 
							humanWhite = "human22.png"; humanWhite_2 = "human22_2.png";
							humanIcon = "human21.png";
							
							humanBlackIcon = new ImageIcon(getClass().getResource(humanBlack));
							humanBlackIcon_2 = new ImageIcon(getClass().getResource(humanBlack_2));
							humanWhiteIcon = new ImageIcon(getClass().getResource(humanWhite));
							humanWhiteIcon_2 = new ImageIcon(getClass().getResource(humanWhite_2));
							
							icon1 = humanWhiteIcon; icon2 = humanWhiteIcon_2;
							
							character = new JPanel();
							character.setBounds(0, 0, 1000, 600);
							character.setOpaque(false);
							character.setLayout(null);
							
							humanPlayer = new JLabel(icon1);
							humanPlayer.setBounds(300, 190, 177, 177);
							humanPlayer.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									humanPlayer.setIcon(icon2);
								}
								
								public void mouseExited(MouseEvent e){
									humanPlayer.setIcon(icon1);
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();
									getContentPane().setBackground(Color.ORANGE);
																		
									avatar.setBounds(0, 0, 1000, 600);
									AvatarHandler h = new AvatarHandler();

									for(int i = 0; i < 20; i++){										
										avatar.labels[i].addMouseListener(h);										
									}
									
									add(avatar);
									repaint();
									revalidate();
								}
							});					
							
							pieceTextColor = new JLabel(icon[15]);
							pieceTextColor.setBounds(290, 140, icon[15].getIconWidth(), icon[15].getIconHeight());	
							pieceTextColor.setToolTipText("Click to play as Black.");
							pieceTextColor.addMouseListener(new MouseAdapter(){
								public void mouseClicked(MouseEvent e){
									
									if(pieceTextColor.getIcon() == icon[15]){
										
										pieceTextColor.setIcon(icon[14]);
										pieceTextColor.setToolTipText("Click to play as White.");
										
										if(humanPlayer.getIcon() == humanWhiteIcon){
											
											humanPlayer.setIcon(humanBlackIcon);
											icon1 = humanBlackIcon; 
											icon2 = humanBlackIcon_2;
											humanIcon = "human11.png";
											
										}
										
									}else if(pieceTextColor.getIcon() == icon[14]){
										
										pieceTextColor.setIcon(icon[15]);
										pieceTextColor.setToolTipText("Click to play as Black.");
										
										if(humanPlayer.getIcon() == humanBlackIcon){
											
											humanPlayer.setIcon(humanWhiteIcon);
											icon1 = humanWhiteIcon; 
											icon2 = humanWhiteIcon_2;
											humanIcon = "human21.png";
										}																
										
									}
									
								}
							});			
							
							gameBackOnePlayer = new JLabel(icon[10]);
							gameBackOnePlayer.setBounds(40, 490, icon[10].getIconWidth(), icon[10].getIconHeight());
							gameBackOnePlayer.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									gameBackOnePlayer.setIcon(icon[11]);
								}                                                                                      
								
								public void mouseExited(MouseEvent e){
									gameBackOnePlayer.setIcon(icon[10]);
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();									
									
									add(mode);
									repaint();
									revalidate();
								}
							});
							
							gamePlayOnePlayer = new JLabel(icon[16]);
							gamePlayOnePlayer.setBounds(645, 480, icon[16].getIconWidth(), icon[16].getIconHeight());
							gamePlayOnePlayer.addMouseListener(new MouseAdapter(){
								

								public void mouseEntered(MouseEvent e){
									gamePlayOnePlayer.setIcon(new ImageIcon(getClass().getResource("go_2.png")));
								}
								
								public void mouseExited(MouseEvent e){
									gamePlayOnePlayer.setIcon(icon[16]);
								}
								
								public void mouseReleased(MouseEvent e){
									gamePlayOnePlayer.setIcon(icon[16]);
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();
									
									gamePanel = new JPanel();
									gamePanel.setBounds(0, 0, 1000, 600);
									gamePanel.setOpaque(false);
									gamePanel.setLayout(null);
									
									playerTurn = new JLabel(new ImageIcon(getClass().getResource(turnImage)));
									playerTurn.setBounds(685, 30, 59, 69);							
									
									game = new CheckerGamePanel(
											(pieceTextColor.getIcon().toString().equals(icon[15].toString()))? PieceColor.WHITE : PieceColor.BLACK);
									
									Icon bgIcon = new ImageIcon(getClass().getResource("greenbg.png"));
									JLabel bg = new JLabel(bgIcon);
									bg.setBounds(0, 0, bgIcon.getIconWidth(), bgIcon.getIconHeight());
									
									boardBorder = new JLabel(new ImageIcon(getClass().getResource("border.png")));
									boardBorder.setBounds(147, 33, 497, 497);																											
																														
									aiPlayer = new JLabel(icon[13]);									
									playerOne = new JLabel(new ImageIcon(getClass().getResource(humanIcon)));
																																	
									aiPlayer.setBounds(30, 20, icon[13].getIconWidth(), icon[13].getIconHeight());
									playerOne.setBounds(675, 440, 80, 80);
									
									gameMenu = new JLabel(icon[12]);
									gameMenu.setBounds(40, 470, icon[12].getIconWidth(), icon[12].getIconHeight());	
									gameMenu.addMouseListener(new MouseAdapter(){
										public void mouseEntered(MouseEvent e){
											gameMenu.setIcon(new ImageIcon(getClass().getResource("menu_2.png")));
										}
										
										public void mouseExited(MouseEvent e){
											gameMenu.setIcon(icon[12]);
										}
										
										public void mouseClicked(MouseEvent e){
											glass.setVisible(true);
										}
									});
																		
									
									gamePanel.add(playerTurn);									
									gamePanel.add(aiPlayer);
									gamePanel.add(playerOne);							
									gamePanel.add(game);
									gamePanel.add(boardBorder);
									gamePanel.add(gameMenu);														
									gamePanel.add(bg);
									
									add(gamePanel);
									
									repaint();
									revalidate();
								}
							});
														
							character.add(humanPlayer);				
							character.add(pieceTextColor);					
							character.add(gameBackOnePlayer);
							character.add(gamePlayOnePlayer);
							
							add(character);
							
							repaint();
							revalidate();
						}
						
					});
					
					twoPlayer = new JLabel(icon[8]);
					twoPlayer.setBounds(160, 320, icon[8].getIconWidth(), icon[8].getIconHeight());
					
					twoPlayer.addMouseListener(new MouseAdapter(){
						public void mouseEntered(MouseEvent e){
							twoPlayer.setIcon(icon[9]);
							twoPlayer.setBounds(160, 320, icon[9].getIconWidth(), icon[9].getIconHeight());
						}
						
						public void mouseExited(MouseEvent e){
							twoPlayer.setIcon(icon[8]);
							twoPlayer.setBounds(160, 320, icon[8].getIconWidth(), icon[8].getIconHeight());
						}
						
						public void mouseReleased(MouseEvent e){
							twoPlayer.setIcon(icon[8]);
							twoPlayer.setBounds(160, 320, icon[8].getIconWidth(), icon[8].getIconHeight());
						}
						
						public void mouseClicked(MouseEvent e){
							getContentPane().removeAll();
							
							human1Icon = "human12.png"; human1Icon_2 = "human12_2.png"; 
							human2Icon = "human22.png"; human2Icon_2 = "human22_2.png";
							
							character = new JPanel();
							character.setBounds(0, 0, 1000, 600);
							character.setOpaque(false);
							character.setLayout(null);
							
							playerOneAvatar = new JLabel(new ImageIcon(getClass().getResource(human1Icon)));
							playerOneAvatar.setBounds(170, 170, 177, 177);
							playerOneAvatar.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									playerOneAvatar.setIcon(new ImageIcon(getClass().getResource(human1Icon_2)));
								}
								
								public void mouseExited(MouseEvent e){
									playerOneAvatar.setIcon(new ImageIcon(getClass().getResource(human1Icon)));
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
							
							playerTwoAvatar = new JLabel(new ImageIcon(getClass().getResource(human2Icon)));
							playerTwoAvatar.setBounds(450, 170, 177, 177);
							playerTwoAvatar.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									playerTwoAvatar.setIcon(new ImageIcon(getClass().getResource(human2Icon_2)));
								}
								
								public void mouseExited(MouseEvent e){
									playerTwoAvatar.setIcon(new ImageIcon(getClass().getResource(human2Icon)));
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
							
							versusLabel = new JLabel(icon[18]);
							versusLabel.setBounds(365, 270, icon[18].getIconWidth(), icon[18].getIconHeight());		
							
							blackText = new JLabel(icon[14]);
							blackText.setBounds(160, 120, icon[14].getIconWidth(), icon[14].getIconHeight());
							
							whiteText = new JLabel(icon[15]);
							whiteText.setBounds(430, 120, icon[15].getIconWidth(), icon[15].getIconHeight());							
							
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
							
							gameBackTwoPlayer = new JLabel(icon[10]);
							gameBackTwoPlayer.setBounds(40, 490, icon[10].getIconWidth(), icon[10].getIconHeight());
							gameBackTwoPlayer.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									gameBackTwoPlayer.setIcon(icon[11]);
								}
								
								public void mouseExited(MouseEvent e){
									gameBackTwoPlayer.setIcon(icon[10]);
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();									
									add(mode);
									repaint();
									revalidate();
								}
							});
							
							gamePlayTwoPlayer = new JLabel(icon[16]);
							gamePlayTwoPlayer.setBounds(645, 480, icon[16].getIconWidth(), icon[16].getIconHeight());
							gamePlayTwoPlayer.addMouseListener(new MouseAdapter(){
								public void mouseEntered(MouseEvent e){
									gamePlayTwoPlayer.setIcon(new ImageIcon(getClass().getResource("go_2.png")));
								}
								
								public void mouseExited(MouseEvent e){
									gamePlayTwoPlayer.setIcon(icon[16]);
								}
								
								public void mouseClicked(MouseEvent e){
									getContentPane().removeAll();
									
									gamePanel = new JPanel();
									gamePanel.setBounds(0, 0, 1000, 600);
									gamePanel.setOpaque(false);
									gamePanel.setLayout(null);
									
									Icon bgIcon = new ImageIcon(getClass().getResource("greenbg.png"));
									JLabel bg = new JLabel(bgIcon);
									bg.setBounds(0, 0, bgIcon.getIconWidth(), bgIcon.getIconHeight());
									
									game = new CheckerGamePanel();
									
									boardBorder = new JLabel(new ImageIcon(getClass().getResource("border.png")));
									boardBorder.setBounds(147, 33, 497, 497);																											
																							
									
									String hum1 =  human1Icon.substring(0, human1Icon.length()-5) + "1.png";									
									String hum2 = human2Icon.substring(0, human2Icon.length()-5) + "1.png";
										
									playerOne = new JLabel(new ImageIcon(getClass().getResource(hum1)));	
									playerTwo = new JLabel(new ImageIcon(getClass().getResource(hum2)));
																																	
									playerOne.setBounds(30, 20, 80, 80);
									playerTwo.setBounds(675, 440, 80, 80);
									
									gameMenu = new JLabel(icon[12]);
									gameMenu.setBounds(40, 470, icon[12].getIconWidth(), icon[12].getIconHeight());	
									gameMenu.addMouseListener(new MouseAdapter(){
										public void mouseEntered(MouseEvent e){
											gameMenu.setIcon(new ImageIcon(getClass().getResource("menu_2.png")));
										}
										
										public void mouseExited(MouseEvent e){
											gameMenu.setIcon(icon[12]);
										}
										
										public void mouseClicked(MouseEvent e){
											glass.setVisible(true);
										}
									});
									
									playerTurn = new JLabel(new ImageIcon(getClass().getResource(turnImage)));
									playerTurn.setBounds(695, 30, 59, 69);							
									
									gamePanel.add(playerTurn);									
									gamePanel.add(playerOne);
									gamePanel.add(playerTwo);							
									gamePanel.add(game);
									gamePanel.add(boardBorder);
									gamePanel.add(gameMenu);														
									gamePanel.add(bg);
									
									add(gamePanel);
									
									repaint();
									revalidate();
								}
							});
														
							character.add(playerOneAvatar);
							character.add(playerTwoAvatar);
							character.add(versusLabel);
							character.add(blackText);
							character.add(whiteText);
							character.add(human1Name);
							character.add(human2Name);
							character.add(gameBackTwoPlayer);
							character.add(gamePlayTwoPlayer);
							
							add(character);
							
							repaint();
							revalidate();
							
						}
						
					});
					
					backToMain =  new JLabel(icon[10]);
					backToMain.setBounds(40, 490, icon[10].getIconWidth(), icon[10].getIconHeight());
					backToMain.addMouseListener(new MouseAdapter(){
						public void mouseEntered(MouseEvent e){							
							backToMain.setIcon(icon[11]);
						}
						
						public void mouseExited(MouseEvent e){							
							backToMain.setIcon(icon[10]);
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
					revalidate();
				}				
			
			}
			
		);
		
		/*optionsButton = new JLabel(icon[2]);
		optionsButton.setBounds(200, 310, icon[2].getIconWidth(),icon[2].getIconHeight());
		optionsButton.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				optionsButton.setIcon(new ImageIcon(getClass().getResource("settings2.png")));
			}
			
			public void mouseExited(MouseEvent e){
				optionsButton.setIcon(icon[2]);
			}
			
			public void mouseReleased(MouseEvent e){
				optionsButton.setIcon(icon[2]);
			}
			
			public void mouseClicked(MouseEvent e){
				//code here.
			}
		});*/
		
		infoButton = new JLabel(icon[3]);
		infoButton.setBounds(430, 310, icon[3].getIconWidth(), icon[3].getIconHeight());
		infoButton.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				infoButton.setIcon(new ImageIcon(getClass().getResource("help2.png")));
			}
			
			public void mouseExited(MouseEvent e){
				infoButton.setIcon(icon[3]);
			}
			
			public void mouseReleased(MouseEvent e){
				infoButton.setIcon(icon[3]);
			}
			
			public void mouseClicked(MouseEvent e){
				getContentPane().removeAll();
				getContentPane().setBackground(Color.ORANGE);
				
				InstructionPanel ins = new InstructionPanel();								
				ins.nextLabel.addMouseListener(new MouseAdapter(){
															
					public void mouseEntered(MouseEvent e){
						ins.nextLabel.setIcon(new ImageIcon(getClass().getResource("next2.png")));
					}
					
					public void mouseExited(MouseEvent e){
						ins.nextLabel.setIcon(new ImageIcon(getClass().getResource("next.png")));
					}
					
					public void mouseClicked(MouseEvent e){
						ins.next(++t);
					}
										
				});
				
				
				ins.backLabel.addMouseListener(new MouseAdapter(){
					
					public void mouseEntered(MouseEvent e){
						ins.backLabel.setIcon(new ImageIcon(getClass().getResource("back_2.png")));
					}
					
					public void mouseExited(MouseEvent e){
						ins.backLabel.setIcon(new ImageIcon(getClass().getResource("back_1.png")));
					}
					
					public void mouseClicked(MouseEvent e){
						ins.back(--t);
					}
										
				});
				
				ins.exitLabel.addMouseListener(new MouseAdapter(){
					
					public void mouseEntered(MouseEvent e){
						ins.exitLabel.setIcon(new ImageIcon(getClass().getResource("close2.png")));
					}
					
					public void mouseExited(MouseEvent e){
						ins.exitLabel.setIcon(new ImageIcon(getClass().getResource("close.png")));
					}
					
					public void mouseClicked(MouseEvent e){
						remove(ins);
						add(menu);
						repaint();
						revalidate();
					}
				});
				
				add(ins);
				
				
				repaint();
				revalidate();
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
				
				add(character);
				getContentPane().repaint();
				revalidate();
			}
		});
		
		gamePaused.info.addMouseListener(new MouseAdapter(){
						
			public void mouseEntered(MouseEvent e){
				gamePaused.info.setIcon(new ImageIcon(getClass().getResource("gamehelp2.png")));
			}
			
			public void mouseExited(MouseEvent e){
				gamePaused.info.setIcon(new ImageIcon(getClass().getResource("gamehelp.png")));
			}
			
			public void mouseClicked(MouseEvent e){
				
				t = 0;
				
				glass.setVisible(false);
				getContentPane().removeAll();
				getContentPane().setBackground(Color.ORANGE);
				
				InstructionPanel ins = new InstructionPanel();								
				ins.nextLabel.addMouseListener(new MouseAdapter(){
															
					public void mouseEntered(MouseEvent e){
						ins.nextLabel.setIcon(new ImageIcon(getClass().getResource("next2.png")));
					}
					
					public void mouseExited(MouseEvent e){
						ins.nextLabel.setIcon(new ImageIcon(getClass().getResource("next.png")));
					}
					
					public void mouseClicked(MouseEvent e){
						ins.next(++t);
					}
										
				});
				
				
				ins.backLabel.addMouseListener(new MouseAdapter(){
					
					public void mouseEntered(MouseEvent e){
						ins.backLabel.setIcon(new ImageIcon(getClass().getResource("back_2.png")));
					}
					
					public void mouseExited(MouseEvent e){
						ins.backLabel.setIcon(new ImageIcon(getClass().getResource("back_1.png")));
					}
					
					public void mouseClicked(MouseEvent e){
						ins.back(--t);
					}
										
				});
				
				ins.exitLabel.addMouseListener(new MouseAdapter(){
					
					public void mouseEntered(MouseEvent e){
						ins.exitLabel.setIcon(new ImageIcon(getClass().getResource("close2.png")));
					}
					
					public void mouseExited(MouseEvent e){
						ins.exitLabel.setIcon(new ImageIcon(getClass().getResource("close.png")));
					}
					
					public void mouseClicked(MouseEvent e){
						remove(ins);
						add(gamePanel);
						repaint();
						revalidate();
					}
				});
				
				add(ins);
				
				
				repaint();
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
		//menu.add(optionsButton);
		menu.add(infoButton);
		
		add(menu);	
		
		container = getContentPane();
	}
	
	public class AvatarHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			Object source = e.getSource();
			getContentPane().removeAll();
			
			int i;
			for(i = 0; i < 20; i++){
				if(source == avatar.labels[i]){
										
					icon1 = new ImageIcon(getClass().getResource(imageNames[i]));
					icon2 = new ImageIcon(getClass().getResource(imageNamesHover[i]));					
					humanPlayer.setIcon(icon2);
					
					humanIcon = imageNames[i].substring(0, (imageNames[i].length()-5)) + "1.png";
					break;
				}
			}		
			
			
			if(i == 20){
				
				for(i = 0; i < 20; i++){
					if(source == avatar1.labels[i]){
											
						human1Icon = imageNames[i];
						human1Icon_2 = imageNamesHover[i];					
						
						playerOneAvatar.setIcon(new ImageIcon(getClass().getResource(human1Icon_2)));												
						break;
					}
				}
				
				for(i = 0; i < 20; i++){
					if(source == avatar2.labels[i]){
											
						human2Icon = imageNames[i];
						human2Icon_2 = imageNamesHover[i];					
						
						playerTwoAvatar.setIcon(new ImageIcon(getClass().getResource(human2Icon_2)));												
						break;
					}
				}
				
			}
						
			add(character);
			repaint();
			revalidate();
			
		}
	}
	
	public static void gameOver(GameOver game, int blacksScore, int whitesScore, int blacksMoves, int whitesMoves, boolean twoP){
				
		container.removeAll();		
		
		if(blacksScore == 0){						
			if(twoP)
				game.setWinner(playerTwoAvatar.getIcon(), human2Name.getText(), whitesMoves);
			else
				game.setWinner((CheckerBoard.aiColor == PieceColor.BLACK)? humanPlayer.getIcon(): aiWinsLabel.getIcon() ,
						(CheckerBoard.aiColor == PieceColor.BLACK)? "Human" : "Computer", whitesMoves);
			
		}else if(whitesScore == 0){						
			if(twoP)
				game.setWinner(playerOneAvatar.getIcon(), human1Name.getText(), blacksMoves);
			else
				game.setWinner((CheckerBoard.aiColor == PieceColor.WHITE)? humanPlayer.getIcon() : aiWinsLabel.getIcon(), 
						(CheckerBoard.aiColor == PieceColor.WHITE)? "Human": "Computer", blacksMoves);
		}
		
		container.add(game);
		
		game.ok.addMouseListener(new MouseAdapter(){
			
			public void mouseClicked(MouseEvent e){
				container.removeAll();
				container.add(mode);
				container.repaint();
				container.revalidate();
			}
		});				
		
		container.repaint();
		container.revalidate();
		
	}
	
	public static void main(String[] args)
	{
		CheckerFrame frame = new CheckerFrame();		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setResizable(false);
	}
}
