
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*; 

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.URL;

import javax.sound.sampled.*;

// ai make a focused effort to figure out a path to destroy base if possible
// ai employ more fighters of level 1 and 2 strength, not just upgrade every fighter as much as possible
// ai employ more fighters period

// clean up graph replay

// bonus hexes ai
// end graph wonky
// create a game within territory that just sits there owned
// maybe try to make sideboard one seperate frame
// 1, 2, 3 shortcuts to select level fighters available for move

public class hexVenture {
		
	static int fx = 0;
	static int freq = 0;
	static int hexx = 330;
	static int xcomp = 0;
	static int h = 0;
	static int stoneFreq;
	static int stone = 0;
	static int createMapCounter = 0;
	static int selectedFighterHighlight;
	static int regColor;
	static int mapSize = 100;
	static int hexHighlight = 0;
	
	static int screenSizeH;
	
	static GameStatus gameStatus = null;
	
	static boolean resume = false;
		
	static boolean isEditor=true;
	static boolean isBonusTool=false;
		
		// grid dimensions
	final static int ROWSIZE = 16; // 20, 24
	final static int COLSIZE = 22; // 33
	final static int BSIZE=ROWSIZE*COLSIZE; // BSIZE = total number of hexes
	final static int HEXSIZE = 60;	// 48 hex size in pixels // 35, 32

		// $ amounts
	public final static int FIGHTER_RANK_ONE_EXPENSE=2;
	final static int FIGHTER_RANK_TWO_EXPENSE=6;
	final static int FIGHTER_RANK_THREE_EXPENSE=18;
	final static int FIGHTER_RANK_FOUR_EXPENSE=54;
	final static int BASE_LEVEL2_HEXES=10;
	final static int BASE_LEVEL3_HEXES=30;
	
	public static int numPlayers=6;//+1
	static int numBonus=16; // number of bonus hexes
		
	static boolean sound=false;
	static boolean captureNew;
		
	public static int playerTurn=0;
	static int selectedHex;
	static int selectedPlayer;
	public static int selectedBase = -1;
	static int selectedFighter = -1;
	static boolean isPlacingFighter=false;
	static boolean isPlacingOutpost = false;
		
	static boolean oneHuman;
	static int humanPlayer;
	static int turn = -1;
		
	public static Random rand=new Random();
		
	public static Hex[] hexes = new Hex[BSIZE];
	public static Player[] player = new Player[numPlayers+1];
	public static Bonus[] bonus = new Bonus[numBonus];
		
	static ArrayList<Integer> realHexes = new ArrayList<Integer>();
	
	static ArrayList<Particle> particle = new ArrayList<Particle>();
	static ArrayList<innerLoop> hexList = new ArrayList<innerLoop>();
		
	static int aiScoreBonus=0; static int aiScore=0;
	static int aiScoreHigh=0;
		
	static ButtonPanel buttonPanel = new ButtonPanel();
		
	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem menuItem;
	JCheckBoxMenuItem cbMenuItem;
		
	int interval = 1;     
	static int delay = 25;
	static int period = 1; 
	static int aiSpeed = 1;
	static int periodAnim = 30;
	    
	static Timer timerAI;
	static Timer timerCreateMap;
	static Timer timerAnim;
	static Timer timerReplayGame;
	
	public static void test(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		StdDraw.setCanvasSize(screenSize.width, screenSize.height);
		screenSizeH = screenSize.height;
		StdDraw.setXscale(0.0, 100);
		StdDraw.setYscale(0.0, 100);
		
		StdDraw.setPenRadius(.002);
		
		initGame();
		
		timerAI = new Timer();
		timerAnim = new Timer();
		
		animateTimer();
		
		startGame();
	}
	
	public static void startGame(){
		timerCreateMap = new Timer();
		resetMap();
		createMap();
		while (!resume){
			StdDraw.clear();
			for (int hh = 0; hh < BSIZE; hh++){
    			if ((hexes[hh].isReal()) && (hexes[hh].isVisible())){
    				if ((hexes[hh].getX() != hexes[hh].getOx()) || (hexes[hh].getY() != hexes[hh].getOy()))
    					hexes[hh].moveToTarget();
    				colorHexes(hh);
    				StdDraw.drawHex(hh);
    			}
    		}
    		StdDraw.show(0);
		}

		playerTurn = 0;
		while (resume)
			simulate();
	}
	
	public static void simulate(){
		StdDraw.clear();
		
		for (int i=0;i<(BSIZE);i++) {
			if (!hexes[i].isReal())
				continue;
			
			colorHexes(i);
			
			for (int b = 0; b < numBonus; b++){
				if (bonus[b].isControlled()){
					if (bonus[b].getThickness() > .022)
						bonus[b].setChangeThicknessDir(true);
					if (bonus[b].getThickness() < .008)
						bonus[b].setChangeThicknessDir(false);
					if (bonus[b].isChangeThicknessDir())
						bonus[b].setThickness(-.0000005);
					else
						bonus[b].setThickness(.0000008);
					
					StdDraw.setRegionColor((player[bonus[b].getPlayer()].getR()/2)+regColor, (player[bonus[b].getPlayer()].getG()/2)+regColor, (player[bonus[b].getPlayer()].getB()/2)+regColor);
				}
			}
			
			if (!hexes[i].isVisible){
				if (((hexes[i].occupation.getPlayer() != playerTurn) || (hexes[i].occupation.getBase() == -1)) || (!player[playerTurn].isAI())){
					if ((hexes[i].getX() != hexes[i].getOx()) || (hexes[i].getY() != hexes[i].getOy()))
						hexes[i].moveToTarget();
					StdDraw.drawOldHex(i);
				}
				if ((hexes[i].occupation.getPlayer() == playerTurn) && (player[playerTurn].isAI()))
					StdDraw.drawOldHex(i);
			}	
		}
		
		for (int i = 0; i < BSIZE; i++){
			if (!hexes[i].isReal())
				continue;
			
			if (hexes[i].isVisible){
				
				
			//if ((hexes[i].occupation.getPlayer() == playerTurn) && (hexes[i].occupation.getBase() > -1)){
				if (hexes[i].occupation.getOccupiedBy()=="outpost") // hex has an outpost on it
					StdDraw.drawOutpost(hexes[i].column,hexes[i].row,player[hexes[i].occupation.getPlayer()].base.get(hexes[i].occupation.getBase()).getRank(),hexes[i].occupation.getPlayer(),hexes[i].occupation.getBase());
			
				if ((hexes[i].getX() != hexes[i].getOx()) || (hexes[i].getY() != hexes[i].getOy()))
					hexes[i].moveToTarget();
				StdDraw.drawHex(i);
			//}
				if (hexes[i].isHasDeath()) // hex had an unpaid fighter on it
					StdDraw.drawDeath(hexes[i].getX()+23, hexes[i].getY()+22, hexes[i].occupation.getPlayerDeath());
			}
			
			if (hexes[i].getRegionBonus() > 0)
				StdDraw.regLine(hexes[i].getColumn(),hexes[i].getRow(), i, hexes[i].getRegionBonus(), hexes[i].occupation.getPlayer());
			if (playerTurn > -1)
				if (!player[playerTurn].isAI())
					if ((hexes[i].occupation.getPlayer() == playerTurn) && (hexes[i].occupation.getBase() == selectedBase))
						StdDraw.line(hexes[i].getColumn(),hexes[i].getRow(), i);
			//if ((selectedBase > -1) && (selectedBase < player[playerTurn].base.size()) && (!player[playerTurn].isAI()))
				//if (player[playerTurn].base.get(selectedBase).getHexNum() == i)
					//StdDraw.drawSelectHex(i);
			//if (((hexes[i].getRegionBonus() > 0) && (hexes[i].occupation.getPlayer() > 0)) || ((hexes[i].getRegionBonus() > 0) && (hexes[i].isHighlighted())))
				//StdDraw.drawHex(hexes[i].getColumn(),hexes[i].getRow(),i);
		}
		
		drawIcons();
		
		drawGraphicalEffects();
		buttonPanel();
		StdDraw.drawSideInfo(79, 60);
		
		if (gameStatus != GameStatus.GAME_ON){
			int highBaseSacked = 0, highKilled = 0, highStarved = 0, highBaseLinked = 0;
			for (int pl = 0; pl <= numPlayers; pl++){
				if (player[pl].getBasesSacked() > highBaseSacked)
					highBaseSacked = player[pl].getBasesSacked();
				if (player[pl].getBasesLinked() > highBaseLinked)
					highBaseLinked = player[pl].getBasesLinked();
				if (player[pl].getFightersKilled() > highKilled)
					highKilled = player[pl].getFightersKilled();
				if (player[pl].getFightersStarved() > highStarved)
					highStarved = player[pl].getFightersStarved();
			}
			for (int pl = 0; pl <= numPlayers; pl++)
				StdDraw.drawHighs(pl, highBaseSacked, highBaseLinked, highKilled, highStarved);
			StdDraw.drawGameOver();
		}
		
		StdDraw.show(0);
	}
	
	public hexVenture() {

	}
	
	static void initGame(){
		//new SoundClipTest();
		//SoundEffect.init();
	    //SoundEffect.volume = SoundEffect.Volume.HIGH;
		
	    numPlayers = 0;
	    
		buttonPanel.one.setupButton(Color.DARK_GRAY, 89, 87, 10, 3, "Purchase Fighter");
		buttonPanel.two.setupButton(Color.DARK_GRAY, 89, 80, 10, 3, "---");
		buttonPanel.endturn.setupButton(Color.DARK_GRAY, 89, 74, 10, 3, "End Turn");
	
		buttonPanel.lblSavings.setupLabel(Color.DARK_GRAY, 92, 97, "0");
		buttonPanel.lblIncome.setupLabel(Color.DARK_GRAY, 92, 94, "0");
		buttonPanel.lblWages.setupLabel(Color.DARK_GRAY, 92, 91, "0");
		buttonPanel.lblBank.setupLabel(Color.DARK_GRAY, 92, 86, "0");
		
		buttonPanel.lblBankNext.setupLabel(Color.DARK_GRAY, 97, 86, "0");
		
		gameStatus=GameStatus.GAME_ON;
    
		for (int i = 0; i <= (6); i++){ 
			player[i] = new Player();
			player[i].setR(255);
			player[i].setG(255);
			player[i].setB(255);
		}
		
		for (int i = 0; i < (numBonus); i++)
			bonus[i] = new Bonus();
		
		for (int i = 0; i < (BSIZE); i++){ 
			hexes[i] = new Hex();
			hexes[i].occupation = new hexOccupation();
			
			for (int p=0;p<=5;p++){
				hexes[i].oldOccupation[p] = new hexOccupation();
				hexes[i].oldOccupation[p].setOccupiedBy("empty");
				hexes[i].oldOccupation[p].setPlayer(-1);
				hexes[i].oldOccupation[p].setBase(-1);
				hexes[i].oldOccupation[p].setFighter(-1);
				hexes[i].oldOccupation[p].setFighterRank(1);
			}
			
			hexes[i].occupation.setOccupiedBy("empty");
			hexes[i].occupation.setPlayer(-1);
			hexes[i].occupation.setBase(-1);
			hexes[i].occupation.setFighter(-1);
		}
		
		for (int p=0;p<=6;p++){
			player[p].setShadeColor(25, 25, 25);
			player[p].setActualColor(25, 25, 25);
			player[p].setName("Player "+(p+1));
			player[p].setAI(false);
			player[p].setHexesLinked(0);
		}
		
		StdDraw.setHexHeight(HEXSIZE);
		int num=-1;
		for (int i=0;i<COLSIZE;i++) {
			for (int j=0;j<ROWSIZE;j++) {
				num++;
				hexes[num].setColumn(i);
				hexes[num].setRow(j);
				hexes[num].setVisible(false);
				StdDraw.setHexOriginXY(i, j, num);
			}
		}
		
		// set hex neighbor values
		for (int i=0;i<(BSIZE);i++) {
			for (int j=0;j<6;j++) {
				hexes[i].line[j] = -1;
				hexes[i].regLine[j] = -1;
				hexes[i].neighbor[j]=-1;
				if ((i-1)>=0){
				if ((j==0)&&(hexes[i].getColumn()==hexes[i-1].getColumn()))
					hexes[i].neighbor[j]=(i-1);
				}
				if ((i+1)<(BSIZE)){
				if ((j==1)&&(hexes[i].getColumn()==hexes[i+1].getColumn()))
					hexes[i].neighbor[j]=(i+1);
				}
				if (hexes[i].getColumn()%2==0){
					if ((j==2)&&((i+(ROWSIZE-1))<(BSIZE))&&((hexes[i].getRow()-1)>=0))
						hexes[i].neighbor[j]=(i+(ROWSIZE-1));
					else if ((j==3)&&((i+ROWSIZE)<(BSIZE)))
						hexes[i].neighbor[j]=(i+ROWSIZE);
					else if ((j==4)&&((i-ROWSIZE)>0))
						hexes[i].neighbor[j]=(i-ROWSIZE);
					else if ((j==5)&&((i-(ROWSIZE+1))>0)&&((hexes[i].getRow()-1)>=0))
						hexes[i].neighbor[j]=(i-(ROWSIZE+1));
				} else {
					if ((j==2)&&((i+ROWSIZE)<(BSIZE)))
						hexes[i].neighbor[j]=(i+ROWSIZE);
					else if ((j==3)&&((i+(ROWSIZE+1))<(BSIZE))&&((hexes[i].getRow()+1)<ROWSIZE))
						hexes[i].neighbor[j]=(i+(ROWSIZE+1));
					else if ((j==4)&&((i-(ROWSIZE-1))>0)&&((hexes[i].getRow()+1)<ROWSIZE))
						hexes[i].neighbor[j]=(i-(ROWSIZE-1));
					else if ((j==5)&&((i-ROWSIZE)>0))
						hexes[i].neighbor[j]=(i-ROWSIZE);
				}
			}
		}
	}
	
	static void drawIcons(){
		for (int p = 0; p <= numPlayers; p++){
			for (int b = 0; b < player[p].base.size(); b++){
				if (hexes[player[p].base.get(b).getHexNum()].isVisible()){
					double x = hexes[player[p].base.get(b).getHexNum()].getX() + 26; //StdDraw.getHexX(hexes[player[p].base.get(b).getHexNum()].getColumn(), hexes[player[p].base.get(b).getHexNum()].getRow(), player[p].base.get(b).getHexNum());
					double y = hexes[player[p].base.get(b).getHexNum()].getY() + 23; //StdDraw.getHexY(hexes[player[p].base.get(b).getHexNum()].getColumn(), hexes[player[p].base.get(b).getHexNum()].getRow(), player[p].base.get(b).getHexNum());
					StdDraw.drawBase(x, y, 2.1, player[p].base.get(b).getRank(), p, b);
				}
				
				for (int f = 0; b < player[p].base.size() && f < player[p].base.get(b).fighter.size(); f++){
					if ((player[p].base.get(b).fighter.get(f).isSelected()) && (!player[p].isAI())){
						double xx = StdDraw.mouseXX();
						double yy = StdDraw.mouseYY();
						StdDraw.drawFighterShadow(xx, yy, 1.4, player[p].base.get(b).fighter.get(f).getRank(), p, b, f);
						StdDraw.drawFighter(xx, yy, 1.6, player[p].base.get(b).fighter.get(f).getRank(), p, b, f);
					}else{
						if (hexes[player[p].base.get(b).fighter.get(f).getHexNum()].isVisible()){
							double xx = hexes[player[p].base.get(b).fighter.get(f).getHexNum()].getX() + 26; //StdDraw.getHexX(hexes[player[p].base.get(b).fighter.get(f).getHexNum()].getColumn(), hexes[player[p].base.get(b).fighter.get(f).getHexNum()].getRow(), player[p].base.get(b).fighter.get(f).getHexNum());
							double yy = hexes[player[p].base.get(b).fighter.get(f).getHexNum()].getY() + 23; //StdDraw.getHexY(hexes[player[p].base.get(b).fighter.get(f).getHexNum()].getColumn(), hexes[player[p].base.get(b).fighter.get(f).getHexNum()].getRow(), player[p].base.get(b).fighter.get(f).getHexNum());
							StdDraw.drawFighter(xx, yy, 1.1, player[p].base.get(b).fighter.get(f).getRank(), p, b, f);
						}
					}
				}
			}
		}	
	}
	
	static void buttonPanel(){
		if (!buttonPanel.one.isHighlighted()){
			if (buttonPanel.one.getR() < 240)
				buttonPanel.one.setR(5);
			if (buttonPanel.one.getG() < 240)
				buttonPanel.one.setG(5);
			if (buttonPanel.one.getB() < 240)
				buttonPanel.one.setB(5);
		}
		if (!buttonPanel.two.isHighlighted()){
			if (buttonPanel.two.getR() < 240)
				buttonPanel.two.setR(5);
			if (buttonPanel.two.getG() < 240)
				buttonPanel.two.setG(5);
			if (buttonPanel.two.getB() < 240)
				buttonPanel.two.setB(5);
		}
		if (!buttonPanel.endturn.isHighlighted()){
			if (buttonPanel.endturn.getR() < 240)
				buttonPanel.endturn.setR(5);
			if (buttonPanel.endturn.getG() < 240)
				buttonPanel.endturn.setG(5);
			if (buttonPanel.endturn.getB() < 240)
				buttonPanel.endturn.setB(5);
		}

		buttonPanel.one.setFillColor();
		buttonPanel.two.setFillColor();
		buttonPanel.endturn.setFillColor();
		
		if (!buttonPanel.one.isEnabled())
			buttonPanel.one.setTextColor(Color.LIGHT_GRAY);
		if (!buttonPanel.two.isEnabled())
			buttonPanel.two.setTextColor(Color.LIGHT_GRAY);
		
		buttonPanel.draw();
	}
	
	static void resetGame(){
		
	}

	static void drawGraphicalEffects(){
		for (int i = 0; i < particle.size(); i++){
			particle.get(i).moveToTarget();
			particle.get(i).draw();
			if ((!particle.get(i).isAlive()) && (!particle.get(i).isExplodeGraphic()))
				particle.remove(i);
		}
		
		
		
		if (player[playerTurn].isAI())
			return;
		
		for (int b = 0; b < player[playerTurn].base.size(); b++){
			if (player[playerTurn].base.get(b).isExplodeGraphic()){
				double perT = .06 * 1.0;
				double perP = .06 * .01;
				int hex = player[playerTurn].base.get(b).getHexNum();
				double x = StdDraw.getHexX(hexes[hex].getColumn(), hexes[hex].getRow(), hex);
				double y = StdDraw.getHexY(hexes[hex].getColumn(), hexes[hex].getRow(), hex);
				player[playerTurn].base.get(b).drawHighlightGraphic(playerTurn, x, y, perT, perP);
			}
			if (b >= player[playerTurn].base.size())
				break;
			for (int f = 0; f < player[playerTurn].base.get(b).fighter.size(); f++){
				if (player[playerTurn].base.get(b).fighter.get(f).isExplodeGraphic()){
					double perT = .06 * 1.0;
					double perP = .06 * .01;
					int hex = player[playerTurn].base.get(b).fighter.get(f).getHexNum();
					double x = StdDraw.getHexX(hexes[hex].getColumn(), hexes[hex].getRow(), hex);
					double y = StdDraw.getHexY(hexes[hex].getColumn(), hexes[hex].getRow(), hex);
					player[playerTurn].base.get(b).fighter.get(f).drawHighlightGraphic(playerTurn, x, y, perT, perP);
				}
				if (b >= player[playerTurn].base.size())
					break;
			}
		}
	}
	
	static void colorHexes(int i){
		if (((hexes[i].getR() + 5) > hexes[i].getSetR()) && ((hexes[i].getR() - 5) < hexes[i].getSetR())){
			hexes[i].setR(-hexes[i].getR());
			hexes[i].setR(hexes[i].getSetR());
		}
		if ((hexes[i].getR() > hexes[i].getSetR()) && ((hexes[i].getR() - 5) >= 0))
			hexes[i].setR(-5);
		else if ((hexes[i].getR() < hexes[i].getSetR()) && ((hexes[i].getR() + 5) <= 255))
			hexes[i].setR(5);
	
		if (((hexes[i].getG() + 5) > hexes[i].getSetG()) && ((hexes[i].getG() - 5) < hexes[i].getSetG())){
			hexes[i].setG(-hexes[i].getG());
			hexes[i].setG(hexes[i].getSetG());
		}
		if ((hexes[i].getG() > hexes[i].getSetG()) && ((hexes[i].getG() - 5) > 0))
			hexes[i].setG(-5);
		else if ((hexes[i].getG() < hexes[i].getSetG()) && ((hexes[i].getG() + 5) < 255))
			hexes[i].setG(5);
	
		if (((hexes[i].getB() + 5) > hexes[i].getSetB()) && ((hexes[i].getB() - 5) < hexes[i].getSetB())){
			hexes[i].setB(-hexes[i].getB());
			hexes[i].setB(hexes[i].getSetB());
		}
		if ((hexes[i].getB() > hexes[i].getSetB()) && ((hexes[i].getB() - 5) > 0))
			hexes[i].setB(-5);
		else if ((hexes[i].getB() < hexes[i].getSetB()) && ((hexes[i].getB() - 5) < 255))
			hexes[i].setB(5);
		
		hexes[i].setColor(hexes[i].getR(), hexes[i].getG(), hexes[i].getB());
	}
	
	public static void createMap(){
		while (createMapCounter < mapSize){
			createMapCounter++;
			
			freq=rand.nextInt(20)+1; 
			if (createMapCounter < 5)
				freq = 1;
			xcomp = hexx;
					for (int ii = 0; ii < hexes[1].neighbor.length; ii++){
							stoneFreq=rand.nextInt(freq);
						if (stoneFreq == 1){
							if (hexes[hexx].neighbor[ii] > -1){
								if (!hexes[hexes[hexx].neighbor[ii]].isReal()){
									hexes[hexes[hexx].neighbor[ii]].setReal(true);
									hexx = hexes[hexx].neighbor[ii];
								}
							}
						}
					}

				if (xcomp == hexx){
					int inc = 0;
					do{ h = rand.nextInt(BSIZE);
					inc++;
					}while ((!hexes[h].isReal()) && (inc < 2000));
					hexx = h;
				}
				
				if (hexes[hexx].isReal()){
    				hexes[hexx].setStartX(-30);
    				hexes[hexx].setStartY(-30);
    				hexes[hexx].reset();
				}
		}
		//for (int h = 0; h < BSIZE; h++){
		//	hexes[h].setReal(true);
		//	hexes[h].setStartX(hexes[h].getOx()+1);
		//	hexes[h].setStartY(-50);
		//	hexes[h].reset();
		//}
		realHexes.clear();
		for (int h = BSIZE-1; h >= 0; h--)
			if (hexes[h].isReal())
				realHexes.add(h);
		createMapCounter = -1;
    	timerCreateMap = new Timer();
    	createMapTimer();
		
	}
	
	public static void resetMap(){
		if (createMapCounter < realHexes.size())
			return;
		
		for (int h = 0; h < BSIZE; h++){
			hexes[h].setReal(false);
			hexes[h].setVisible(false);
			hexes[h].setR(-hexes[h].getR());
			hexes[h].setG(-hexes[h].getG());
			hexes[h].setB(-hexes[h].getB());
			hexes[h].setR(240);
			hexes[h].setG(240);
			hexes[h].setB(240);
			hexes[h].setSetR(60);
			hexes[h].setSetG(60);
			hexes[h].setSetB(60);
			hexes[h].setStartX(0);
			hexes[h].setStartY(0);
			hexes[h].reset();
		}
		createMapCounter = -1;
		timerCreateMap.purge();
		timerCreateMap.cancel();
		hexx = 400;
		createMap();
	}
	
	public static void createMapTimer(){
        timerCreateMap.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	createMapCounter++;
        		
            	if (createMapCounter >= realHexes.size()){ 
        			timerCreateMap.cancel();
        			return;
            	}
            	hexes[realHexes.get(createMapCounter)].setVisible(true);
        		
            }
        }, delay, 10);
    }
	
	public static void setRegions(){
			for (int i = 0; i < (BSIZE); i++){ 
				for (int ii = 0; ii < hexes[1].neighbor.length; ii++){
					if (hexes[i].neighbor[ii] > -1){
						if (!hexes[hexes[i].neighbor[ii]].isReal())
							hexes[i].neighbor[ii] = -1;
					}
				}
			}
			boolean check = true;
		for (int counter = 1; counter < numBonus; counter++){
			bonus[counter].setRegNum(counter);
			do {
			do
			hexx = rand.nextInt(BSIZE);
			while ((!hexes[hexx].isReal()) || ((hexes[hexx].getRegionBonus() != counter) && (hexes[hexx].getRegionBonus() > 0)));
			check = true;  
			int size = rand.nextInt(10)+10;
				for (int ii = 0; ii < size; ii++){
					stoneFreq=rand.nextInt(6);
					if ((hexes[hexx].neighbor[stoneFreq] > 0) && (hexes[hexes[hexx].neighbor[stoneFreq]].getRegionBonus() == 0) && (hexes[hexes[hexx].neighbor[stoneFreq]].isReal())){
						hexes[hexes[hexx].neighbor[stoneFreq]].setRegionBonus(counter);
						hexx = hexes[hexx].neighbor[stoneFreq];
						bonus[counter-1].setHexAmt(1);
					}
				}
		for (int i = 0; i < (BSIZE); i++){ 
			if (hexes[i].getRegionBonus() == counter){
			for (int ii = 0; ii < hexes[1].neighbor.length; ii++){
				if (hexes[i].neighbor[ii] > -1){
				if (hexes[hexes[i].neighbor[ii]].getRegionBonus() > 0){
					if (hexes[hexes[i].neighbor[ii]].getRegionBonus() != counter){
						for (int iii = 0; iii < BSIZE; iii++){
							if (hexes[iii].getRegionBonus() == counter){
								hexes[iii].setRegionBonus(0);
								bonus[counter-1].setHexAmt(-1);	
							}
						}
						check = false;
					}
				}
				}
			}
			}
		}
		}while (!check);
		}
		
		for (int r = 1; r < numBonus; r++){
		for (int i = 0; i < (BSIZE); i++){
			if (hexes[i].getRegionBonus() == r){
				bonus[r].setHexNum(i);
			for (int ii = 0; ii < hexes[1].neighbor.length; ii++){
				if (hexes[i].neighbor[ii] > -1){
					if (hexes[hexes[i].neighbor[ii]].getRegionBonus() != r)
						hexes[i].regLine[ii] = r;
				}else
					hexes[i].regLine[ii] = r;
			}
			}
		}
		}
	}
	
	public static void animateTimer(){
        timerAnim.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	int random;
            	if (hexList.size() > 0){
            		for (int ii = 0; ii < hexList.get(0).innerHexList.size(); ii++){
            			if (hexList.get(0).getP() > -1){
            			random = (rand.nextInt(10)+5);
            			while (random > 0){
            				Particle par = new Particle();
            				particle.add(par);
            				if (hexList.get(0).getPp() == -1){
            					particle.get(particle.size()-1).setColor(Color.DARK_GRAY);
            					particle.get(particle.size()-1).setColor(130, 130, 130);
            				}else{
            					particle.get(particle.size()-1).setColor(player[hexList.get(0).getPp()].getColorActual());
            					particle.get(particle.size()-1).setColor(player[hexList.get(0).getPp()].getR()*1.2, player[hexList.get(0).getPp()].getG()*1.2, player[hexList.get(0).getPp()].getB()*1.2);
            				}
            				particle.get(particle.size()-1).reset(hexList.get(0).innerHexList.get(ii), hexList.get(0).getTarHex());
            				particle.get(particle.size()-1).setAlive(true);
            				random--;
            			}
            			
            			if (!player[playerTurn].isAI()){
            				//hexes[hexList.get(0).innerHexList.get(ii)].setOx(hexes[hexList.get(0).innerHexList.get(ii)].getOox() - 8);
            				//hexes[hexList.get(0).innerHexList.get(ii)].setOy(hexes[hexList.get(0).innerHexList.get(ii)].getOoy() - 8);
            			}
                		hexes[hexList.get(0).innerHexList.get(ii)].setR(-hexes[hexList.get(0).innerHexList.get(ii)].getR());
                		hexes[hexList.get(0).innerHexList.get(ii)].setR(250);
                		hexes[hexList.get(0).innerHexList.get(ii)].setG(-hexes[hexList.get(0).innerHexList.get(ii)].getG());
                		hexes[hexList.get(0).innerHexList.get(ii)].setG(250);
                		hexes[hexList.get(0).innerHexList.get(ii)].setB(-hexes[hexList.get(0).innerHexList.get(ii)].getB());
                		hexes[hexList.get(0).innerHexList.get(ii)].setB(250);
                		hexes[hexList.get(0).innerHexList.get(ii)].setSetColor(player[hexList.get(0).getP()].getR(), player[hexList.get(0).getP()].getG(), player[hexList.get(0).getP()].getB());
                		//hexes[hexList.get(0).innerHexList.get(ii)].setStartX(hexes[hexList.get(0).innerHexList.get(ii)].getX()+rand.nextInt(5));
                		//hexes[hexList.get(0).innerHexList.get(ii)].setStartY(hexes[hexList.get(0).innerHexList.get(ii)].getY()+rand.nextInt(5));
                		//hexes[hexList.get(0).innerHexList.get(ii)].reset();
                		//if ((sound) && (!player[playerTurn].isAI())){
                		//	stoneFreq=rand.nextInt(2);
                		//	if (stoneFreq == 0)
                		//		SoundEffect.particleOne.play();
                		//	else if (stoneFreq == 1)
                		//		SoundEffect.particleTwo.play();
                		//	else if (stoneFreq == 2)
                		//		SoundEffect.particleThree.play();
                		//}
            			}else{
            				if (!player[playerTurn].isAI()){
                				//hexes[hexList.get(0).innerHexList.get(ii)].setColor(255, 255, 255);
                			}
            			}
                	}  
            		hexList.remove(0);
            	}
            	
            	if (selectedFighter >= 0){
            		if (selectedFighterHighlight > 150)
            			selectedFighterHighlight -= 5;
            		if (selectedFighterHighlight <= 150)
            			selectedFighterHighlight = 255;
            	}

    			for (int b = 0; b < numBonus; b++){
    				if (bonus[b].isControlled()){
    					if (bonus[b].getThickness() > .022)
    						regColor = 100;
    					if (bonus[b].getThickness() < .008)
    						regColor = 0;
    					if (bonus[b].isChangeThicknessDir())
    						regColor-=2;
    					else
    						regColor+=2;
    				}
    			}

    			
            }
        }, delay, periodAnim);
    }
	
	public static void randomizeMap(){
		for (int h=0;h<BSIZE;h++){
			hexes[h].occupation.setBase(-1);
			hexes[h].occupation.setOccupiedBy("empty");
		}
	}
	
	public static void createMap(int selection){
		if (selection==1){
			for (int h=0;h<BSIZE;h++){
				hexes[h].occupation.setBase(-1);
				hexes[h].occupation.setOccupiedBy("empty");
			}
		}
	}
	
	public static void endTurn(){
		hexLinkColor();
		timerAI.purge();
		timerAI.cancel();
		
		for (int i = 0; i < BSIZE; i++){
			if (!hexes[i].isReal())
				continue;
		
			//hexes[i].setOx(hexes[i].getOox());
			//hexes[i].setOy(hexes[i].getOoy());
			hexes[i].setVisible(false);
			hexes[i].setBlank(true);
			
			if ((hexes[i].occupation.getPlayer() == playerTurn) && (hexes[i].hasDeath)){
				hexes[i].setHasDeath(false);
				//hexes[i].setHasFalseIncome(true);
				//hexes[i].setSetColor(player[playerTurn].getDr(), player[playerTurn].getDg(), player[playerTurn].getDb());
			}
			
			HexSaveState tempState = new HexSaveState();

			if (hexes[i].occupation.getOccupiedBy() == "base"){
				tempState.setBase(true);
				//tempState.setBaseRank(hexes[i].occupation.getBaseRank());
			}
			if (hexes[i].occupation.getFighter() > -1){
				tempState.setFighter(true);
				tempState.setFighterRank(hexes[i].occupation.getFighterRank());
			}
			if (hexes[i].occupation.getPlayer() > -1){
				tempState.setColor(player[hexes[i].occupation.getPlayer()].getColorActual());
				tempState.setPlayer(hexes[i].occupation.getPlayer());
			}else
				tempState.setColor(Color.DARK_GRAY);
			
			hexes[i].hexSaveState.add(tempState);
			
			if (!oneHuman)
				hexes[i].setVisible(true);
		}
		
		if (playerTurn > -1){
			for (int b = 0; b < player[playerTurn].base.size(); b++){
				player[playerTurn].base.get(b).setSavings(player[playerTurn].base.get(b).getMoney());
				player[playerTurn].base.get(b).setTempExpense(0);
				player[playerTurn].base.get(b).setBonus(-player[playerTurn].base.get(b).getBonus());
			}
		}
		
		do{ playerTurn++;
			if (playerTurn > numPlayers){
				playerTurn = 0;
				turn++;

				for (int p = 0; p <= numPlayers; p++){
					int numFighters = 0, wages = 0;
					player[p].hexHistory.add((double) player[p].getHexesLinked());
					for (int b = 0; b < player[p].base.size(); b++){
						numFighters += player[p].base.get(b).fighter.size();
						wages += player[p].base.get(b).getExpense();
					}
					player[p].menHistory.add((double) numFighters);
					player[p].wageHistory.add((double) wages);
				}
				
				StdDraw.checkGraphState();
			}
		}while ((player[playerTurn].base.size() == 0) && (gameStatus == GameStatus.GAME_ON));
		
		if (gameStatus != GameStatus.GAME_ON){
			clearGridHighlight();
			setVisibleGrid();
			return;
		}
		
		for (int b = 0; b < player[playerTurn].base.size(); b++)
			for (int f = 0; f < player[playerTurn].base.get(b).fighter.size(); f++)
				player[playerTurn].base.get(b).fighter.get(f).setMoved(false);	

		isPlacingFighter=false;
		hexLink();
		selectedPlayer = playerTurn;
		selectedBase = 0;
		//bonusLink();
		calculateBaseBudget();
		clearGridHighlight();
		setPlayerBaseHighestRankedFighter();
		selectedBase = -1;
		setDashboardLabels(false); // (isPlayer)
		buttonPanel.reset();
		
		if (!player[playerTurn].isAI()){
			//if (sound)
				//SoundEffect.EndTurn.play();
			StdDraw.popup();
			
			for (int i = 0; i < BSIZE; i++){
				if (!hexes[i].isReal())
					continue;
				
				hexes[i].setBlank(false);
				
				if ((hexes[i].occupation.getPlayer() == playerTurn) && (hexes[i].occupation.getBase() > -1) && (!player[playerTurn].isAI())){
					//hexes[i].setOx(hexes[i].getOox() - 8);
	    			//hexes[i].setOy(hexes[i].getOoy() - 8);
				}
			}
			
			selectedBase = 0;
			setDashboardLabels(true);
			setVisibleGrid();
			buttonPanel.endturn.setEnabled(true);
			StdDraw.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}else{
			selectedBase = 0;
			StdDraw.frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			testPlayerAiTurns(playerTurn);
		}
	}
	
	public static void hexLinkColor() {
		for (int p = 0; p <= numPlayers; p++){
			for (int hex = 0; hex < BSIZE; hex++){
				if (!hexes[hex].isReal())
						continue;
				if (hexes[hex].occupation.getPlayer() == p)// && (hexes[hex].occupation.getBase() > -1)
					hexes[hex].setSetColor(player[p].getR(), player[p].getG(), player[p].getB());	
			}
		}				
	}
	
	public static void hexLink() { // determine linked hexes
		for (int hex=0;hex<(BSIZE);hex++){
			//if (hexes[hex].occupation.getOccupiedBy() == "base")
				//continue;
			hexes[hex].setLinked(false); 
		}
			
		for (int p=0;p<=numPlayers;p++){
		for (int b = 0; b < player[p].base.size(); b++){

			player[p].base.get(b).setHexesLinked(-player[p].base.get(b).getHexesLinked());
			
		for (int hex=0;hex<(BSIZE);hex++){
			hexes[hex].setTallyLinked(false);
		}
					hexes[player[p].base.get(b).getHexNum()].setTallyLinked(true);
					hexes[player[p].base.get(b).getHexNum()].setLinked(true);
					
				int iWork=0;
				do{iWork++;
					int hex=0;
					int row=-1;
					int col=0;
					do{ row++;
						hex=row;
						do{ col++;
						if (!hexes[hex].isReal())
							continue;
						if (hexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
									if (hexes[hex].neighbor[iii]==-1){
										hexes[hex].line[iii] = b;
										continue;
									}
									if (hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p){
										hexes[hexes[hex].neighbor[iii]].setTallyLinked(true);
										hexes[hexes[hex].neighbor[iii]].setLinked(true);
										
										if (hexes[hexes[hex].neighbor[iii]].occupation.getBase() == -1)
											hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
										hexes[hex].line[iii] = -1;
									}else{
										hexes[hex].line[iii] = b;
									}
								}
							}while(iii<5);
						}
						hex+=ROWSIZE;
						} while (col<COLSIZE);
					} while (row<(ROWSIZE-1));
				
					row=ROWSIZE;
					col=0;
					do{ row--;
						hex=row+((COLSIZE-1)*ROWSIZE);
						do{ col++;
						if (!hexes[hex].isReal())
							continue;
						if (hexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
									if (hexes[hex].neighbor[iii]==-1){
										hexes[hex].line[iii] = b;
										continue;
									}
									if (hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p){
										hexes[hexes[hex].neighbor[iii]].setTallyLinked(true);
										hexes[hexes[hex].neighbor[iii]].setLinked(true);
										
										if (hexes[hexes[hex].neighbor[iii]].occupation.getBase() == -1)
											hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
										hexes[hex].line[iii] = -1;
									}else{
										hexes[hex].line[iii] = b;
									}
									if (!hexes[hexes[hex].neighbor[iii]].isReal())
										hexes[hex].line[iii] = b;
								}
							}while(iii<5);
						}
						hex-=ROWSIZE;
						} while (col<COLSIZE);
					} while (row>0);
					
					//-------------------------------
					
					hex=-1;
					do{ hex++;
					if (!hexes[hex].isReal())
						continue;
				    	if (hexes[hex].occupation.getPlayer() == p){
				    		int iii=-1;
				    		do{iii++;
				    		if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
				    			if (hexes[hex].neighbor[iii]==-1){
									hexes[hex].line[iii] = b;
									continue;
								}
				    			if (hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p){
				    				hexes[hexes[hex].neighbor[iii]].setTallyLinked(true);
				    				hexes[hexes[hex].neighbor[iii]].setLinked(true);
				    				
				    				if (hexes[hexes[hex].neighbor[iii]].occupation.getBase() == -1)
				    					hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
				    				hexes[hex].line[iii] = -1;
				    			}else{
									hexes[hex].line[iii] = b;
								}
				    			if (!hexes[hexes[hex].neighbor[iii]].isReal())
									hexes[hex].line[iii] = b;
				    		}
				    		}while(iii<5);

				    	}
					} while (hex<(BSIZE)-1);
				
					hex=BSIZE;
					
					do{ hex--;
					if (!hexes[hex].isReal())
						continue;
					if (hexes[hex].occupation.getPlayer() == p){
			    		int iii=-1;
			    		do{iii++;
			    		if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
			    			if (hexes[hex].neighbor[iii]==-1){
								hexes[hex].line[iii] = b;
								continue;
							}
			    			if (hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p){
			    				hexes[hexes[hex].neighbor[iii]].setTallyLinked(true);
			    				hexes[hexes[hex].neighbor[iii]].setLinked(true);
			    				
			    				if (hexes[hexes[hex].neighbor[iii]].occupation.getBase() == -1)
			    					hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
			    				hexes[hex].line[iii] = -1;
			    			}else{
								hexes[hex].line[iii] = b;
							}
			    			if (!hexes[hexes[hex].neighbor[iii]].isReal())
								hexes[hex].line[iii] = b;
			    		}
			    		}while(iii<5);

			    	}
					
					if (iWork==5){
		    			if (hexes[hex].occupation.getPlayer() == p){
		    				if (hexes[hex].isTallyLinked()){
		    					if (hexes[hex].occupation.getBase()==b)
		    						player[p].base.get(b).setHexesLinked(1);
		    				}
		    			}
		    		}
					
					} while (hex>0);
					
				}while(iWork<6);
				
				if (player[p].base.get(b).getHexesLinked() > 0)
					player[p].base.get(b).setRank(1);
				if (player[p].base.get(b).getHexesLinked() > BASE_LEVEL2_HEXES)
					player[p].base.get(b).setRank(2);
				if (player[p].base.get(b).getHexesLinked() > BASE_LEVEL3_HEXES)
					player[p].base.get(b).setRank(3);
				
		}
		}
		playerHexesLinked();
	}
	
	public static void hexLinkNewBase(int p, int b) {
			hexes[player[p].base.get(b).getHexNum()].setLinked(true);
					
				int iWork=0;
				do{iWork++;
					int hex=0;
					int row=-1;
					int col=0;
					do{ row++;
						hex=row;
						do{ col++;
						if (!hexes[hex].isReal())
							continue;
						if (hexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
									if (hexes[hex].neighbor[iii]==-1){
										hexes[hex].line[iii] = b;
										continue;
									}
									if (hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p){
										hexes[hexes[hex].neighbor[iii]].setTallyLinked(true);
										hexes[hexes[hex].neighbor[iii]].setLinked(true);
										hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
										hexes[hex].line[iii] = -1;
									}else{
										hexes[hex].line[iii] = b;
									}
								}
							}while(iii<5);
						}
						hex+=ROWSIZE;
						} while (col<COLSIZE);
					} while (row<(ROWSIZE-1));
				
					row=ROWSIZE;
					col=0;
					do{ row--;
						hex=row+((COLSIZE-1)*ROWSIZE);
						do{ col++;
						if (!hexes[hex].isReal())
							continue;
						if (hexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
									if (hexes[hex].neighbor[iii]==-1){
										hexes[hex].line[iii] = b;
										continue;
									}
									if (hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p){
										hexes[hexes[hex].neighbor[iii]].setTallyLinked(true);
										hexes[hexes[hex].neighbor[iii]].setLinked(true);
										hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
										hexes[hex].line[iii] = -1;
									}else{
										hexes[hex].line[iii] = b;
									}
									if (!hexes[hexes[hex].neighbor[iii]].isReal())
										hexes[hex].line[iii] = b;
								}
							}while(iii<5);
						}
						hex-=ROWSIZE;
						} while (col<COLSIZE);
					} while (row>0);
					
					//-------------------------------
					
					hex=-1;
					do{ hex++;
					if (!hexes[hex].isReal())
						continue;
				    	if (hexes[hex].occupation.getPlayer() == p){
				    		int iii=-1;
				    		do{iii++;
				    		if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
				    			if (hexes[hex].neighbor[iii]==-1){
									hexes[hex].line[iii] = b;
									continue;
								}
				    			if (hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p){
				    				hexes[hexes[hex].neighbor[iii]].setTallyLinked(true);
				    				hexes[hexes[hex].neighbor[iii]].setLinked(true);
				    				hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
				    				hexes[hex].line[iii] = -1;
				    			}else{
									hexes[hex].line[iii] = b;
								}
				    			if (!hexes[hexes[hex].neighbor[iii]].isReal())
									hexes[hex].line[iii] = b;
				    		}
				    		}while(iii<5);

				    	}
					} while (hex<(BSIZE)-1);
				
					hex=BSIZE;
					
					do{ hex--;
					if (!hexes[hex].isReal())
						continue;
					if (hexes[hex].occupation.getPlayer() == p){
			    		int iii=-1;
			    		do{iii++;
			    		if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
			    			if (hexes[hex].neighbor[iii]==-1){
								hexes[hex].line[iii] = b;
								continue;
							}
			    			if (hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p){
			    				hexes[hexes[hex].neighbor[iii]].setTallyLinked(true);
			    				hexes[hexes[hex].neighbor[iii]].setLinked(true);
			    				hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
			    				hexes[hex].line[iii] = -1;
			    			}else{
								hexes[hex].line[iii] = b;
							}
			    			if (!hexes[hexes[hex].neighbor[iii]].isReal())
								hexes[hex].line[iii] = b;
			    		}
			    		}while(iii<5);

			    	}
					
					} while (hex>0);
					
				}while(iWork<6);
	}
	
	public static void bonusLink() { // set bonuses for each base connected
		int hh = BSIZE, p = 0, b = 0, tally;
		for (int bb = 0; bb < player[p].base.size(); bb++)
			player[playerTurn].base.get(bb).setBonus(-player[playerTurn].base.get(bb).getBonus());
		for (int bo=0;bo<numBonus;bo++){ tally = 0; hh = BSIZE;
		bonus[bo].setControlled(false);
		bonus[bo].setPlayer(0);
		bonus[bo].setBase(0);
			for (int h = 0; h < BSIZE; h++){
				if (hexes[h].getRegionBonus() == (bo+1)){
					if (hexes[h].occupation.getPlayer() > -1){
						p = hexes[h].occupation.getPlayer();
						b = hexes[h].occupation.getBase();
						hh = (h+1);
						tally++;
						break;
					}
				}
			}
			for (int h = hh; h < BSIZE; h++){
				if (hexes[h].getRegionBonus() == (bo+1)){
					if ((hexes[h].occupation.getPlayer() == p) && (!hexes[h].hasFalseIncome))
						tally++;
					else{
						bonus[bo].setControlled(false);
						break;
					}
				}
			}
			
			if ((tally == bonus[bo].getHexAmt()) && (tally > 0)){
				//player[p].base[b].setBonus(-player[p].base[b].getBonus());
				bonus[bo].setControlled(true);
				bonus[bo].setPlayer(p);
				bonus[bo].setBase(b);
				player[p].base.get(b).setBonus(tally);
			}
		}
	}
	
	public static void setDashboardLabels(boolean isPlayer){
		int thisIncomeTurn=0, nextIncomeTurn=0;
		int thisExpenseTurn=0, nextExpenseTurn=0;
		int thisBalanceTurn=0, nextBalanceTurn=0;
		
		buttonPanel.lblSavings.setMessage("");
		buttonPanel.lblIncome.setMessage("");
		buttonPanel.lblWages.setMessage("");
		buttonPanel.lblBank.setMessage("");
		buttonPanel.lblBankNext.setMessage("");
		
		if (selectedBase == -1)
			return;
		
		if (selectedBase > player[selectedPlayer].base.size())
			return;
		
		if (!isPlayer){
			buttonPanel.lblSavings.setMessage(""+player[selectedPlayer].base.get(selectedBase).getSavings());
			
			thisIncomeTurn=player[selectedPlayer].base.get(selectedBase).getIncome();

			//bonusLink();
			
			int income = 0;
			for (int h = 0; h < BSIZE; h++){
				if ((hexes[h].occupation.getPlayer() == selectedPlayer) && (hexes[h].occupation.getBase() == selectedBase)){
					if (!hexes[h].hasFalseIncome){
						income++;
					}
				}
			}
			
			nextIncomeTurn=income+player[playerTurn].base.get(selectedBase).getBonus();
			buttonPanel.lblIncome.setSetValue(nextIncomeTurn);
			buttonPanel.lblIncome.setMessage(""+thisIncomeTurn);

			for (int f = 0; f < player[playerTurn].base.get(selectedBase).fighter.size(); f++)
				nextExpenseTurn+=player[playerTurn].base.get(selectedBase).fighter.get(f).getCost();
			
			thisExpenseTurn=player[selectedPlayer].base.get(selectedBase).getExpense();
			buttonPanel.lblWages.setSetValue(nextExpenseTurn);
			buttonPanel.lblWages.setMessage(""+(thisExpenseTurn+player[selectedPlayer].base.get(selectedBase).getTempExpense()));

			thisBalanceTurn=player[selectedPlayer].base.get(selectedBase).getBalance();
			nextBalanceTurn=(nextIncomeTurn-nextExpenseTurn);
			
			buttonPanel.lblBank.setMessage(""+player[selectedPlayer].base.get(selectedBase).getMoney());
			
			buttonPanel.lblBankNext.setMessage("("+(player[selectedPlayer].base.get(selectedBase).getMoney()+nextIncomeTurn-nextExpenseTurn)+")");
		}
	}
	
	public static void setBaseColRow() {
		for (int p=0;p<=numPlayers;p++){
			for (int b = 0; b < player[p].base.size(); b++){
				player[p].base.get(b).setCol(hexes[player[p].base.get(b).getHexNum()].getColumn());
				player[p].base.get(b).setRow(hexes[player[p].base.get(b).getHexNum()].getRow());
			}
		}
	}
	
	public static void setVisibleGrid(){
		int p = playerTurn;
		//if (oneHuman)
			//p = humanPlayer;
		
		for (int h=0;h<BSIZE;h++)
			hexes[h].setVisible(false);
		for (int h=0;h<BSIZE;h++){
			if ((hexes[h].occupation.getPlayer() == p) && (hexes[h].occupation.getBase() > -1)){
				//hexes[h].setVisible(true);
				setOldOccupation(h);
				
				if (hexes[h].isLinked()){
					for (int ii=0;ii<6;ii++){
						if (hexes[h].neighbor[ii]>=0){
							hexes[hexes[h].neighbor[ii]].setVisible(true);
							setOldOccupation(hexes[h].neighbor[ii]);
							
							//for (int iii=0;iii<6;iii++){
								//if (hexes[hexes[h].neighbor[ii]].neighbor[iii]>=0){
									//hexes[hexes[hexes[h].neighbor[ii]].neighbor[iii]].setVisible(true);
									//setOldOccupation(hexes[hexes[h].neighbor[ii]].neighbor[iii]);
								//}
							//}
						}
					}
				}
			}
		}
		
		//for (int h=0;h<BSIZE;h++){
			//if (hexes[h].isVisible()){
			//	hexes[h].setOx(hexes[h].getOox() - 15);
			//	hexes[h].setOy(hexes[h].getOoy() - 15);
			//}else{
			//	hexes[h].setOx(hexes[h].getOox());
			//	hexes[h].setOy(hexes[h].getOoy());
			//}
		//}
		//****************************************************************************************************************
		//for (int h=0;h<BSIZE;h++)
			//hexes[h].setVisible(true); // make entire grid visible
		//****************************************************************************************************************
	}
	
	
	public static void setOldOccupation(int h){
		hexes[h].oldOccupation[playerTurn].setOccupiedBy(hexes[h].occupation.getOccupiedBy());
		hexes[h].oldOccupation[playerTurn].setPlayer(hexes[h].occupation.getPlayer());
		hexes[h].oldOccupation[playerTurn].setBase(hexes[h].occupation.getBase());
		hexes[h].oldOccupation[playerTurn].setFighter(hexes[h].occupation.getFighter());
		hexes[h].oldOccupation[playerTurn].setFighterRank(hexes[h].occupation.getFighterRank());
		//hexes[h].oldState[playerTurn]=hexes[h].getState();
	}
	
	public static void selectBase(int p, int b){
		for (int h=0;h<BSIZE;h++)
			hexes[h].setHighlighted(false);	
		
		if (b == -1)
			return;

		if (player[p].base.get(b).getMoney() >= FIGHTER_RANK_ONE_EXPENSE*5)
			buttonPanel.one.setEnabled(true);
		else
			buttonPanel.one.setEnabled(false);
		
		for (int f = 0; f < player[p].base.get(b).fighter.size(); f++)
			player[p].base.get(b).fighter.get(f).setSelected(false);
		
		player[p].base.get(b).setExplodeGraphic(true);
		player[p].base.get(b).setExplodeRadius(1.75);
		player[p].base.get(b).setExplodeTran(1.0f);
		player[p].base.get(b).setExplodePenR(.01f);
		
		setDashboardLabels(false);
		
		selectedBase = b;
		
		//if ((!player[playerTurn].isAI())&&(sound))
			//SoundEffect.Click.play();
	}
	
	public static void selectFighter(int p, int b, int f){
		for (int h=0;h<BSIZE;h++)
			hexes[h].setHighlighted(false);

		hexes[player[p].base.get(b).fighter.get(f).getHexNum()].setHighlighted(true);
		
		if (!player[p].isAI()){
			if (!player[p].base.get(b).fighter.get(f).isMoved()){
				moveSelectedFighter(p,b,f);
				isPlacingFighter=true;
			}
		}
		
		buttonPanel.one.setEnabled(false);
		
		selectedPlayer=p;
		selectedBase=b;
		selectedFighter=f;
		player[p].base.get(b).fighter.get(f).setSelected(true);
		selectedFighterHighlight = 255;
		player[p].base.get(b).fighter.get(f).setExplodeGraphic(true);
		player[p].base.get(b).fighter.get(f).setExplodeRadius(.8);
		player[p].base.get(b).fighter.get(f).setExplodeTran(1.0f);
		player[p].base.get(b).fighter.get(f).setExplodePenR(.01f);
		
		setDashboardLabels(false);
		
		//if ((!player[playerTurn].isAI())&&(sound))
			//SoundEffect.Click.play();
	}
	
	public static void moveSelectedFighter(int p, int b, int f){
		for (int h=0;h<BSIZE;h++){
			if (!hexes[h].isReal())
				continue;
			
			if ((hexes[h].occupation.getOccupiedBy()!="bonus")){ 
				if ((hexes[h].occupation.getPlayer() == p) && (hexes[h].isLinked())){
					if (hexes[h].occupation.getBase() == b){
						hexes[h].setHighlighted(true);
						int n=0;
						for (int i=0;i<6;i++){ 
							n=hexes[h].neighbor[i];
							if (n>=0){
								if ((hexes[h].isHighlighted()) && (hexes[n].occupation.getOccupiedBy()!="bonus"))
									hexes[n].setHighlighted(true);
							}
						}
					}
				}
			}
		}
		
		for (int h=0;h<BSIZE;h++)
			checkHexOccupation(p,b,f,h);
		
		for (int h=0;h<BSIZE;h++)
			checkHexOccupationExtra(p,b,f,h);
		
	}
	
	public static void checkHexOccupation(int p, int b, int f, int n){
		int pp,bb,ff;
		if ((hexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) || (hexes[n].occupation.getFighter() > -1)){
			pp=hexes[n].occupation.getPlayer();
			bb=hexes[n].occupation.getBase();
			ff=hexes[n].occupation.getFighter();
			
			if ((hexes[n].occupation.getPlayer() != p) && (hexes[n].isHighlighted())){ // occupied by enemy
				if (hexes[n].occupation.getFighter() > -1){ // occupied by a fighter
					if (player[p].base.get(b).fighter.get(f).getRank() > player[pp].base.get(bb).fighter.get(ff).getRank())
						hexes[n].setHighlighted(true);
					else
						hexes[n].setHighlighted(false);
				}
				if ((hexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) && (hexes[n].isHighlighted())){ // occupied by a base
					if (player[p].base.get(b).fighter.get(f).getRank() > player[pp].base.get(bb).getRank())
						hexes[n].setHighlighted(true);
					else
						hexes[n].setHighlighted(false);
				}
			}
			if (hexes[n].occupation.getPlayer() == p) // occupied by own
				if (hexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) // occupied by a base
					hexes[n].setHighlighted(false);
		}
	}
	
	public static void checkHexOccupationExtra(int p, int b, int f, int n){
		int pp,bb,ff;
		if ((hexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) || (hexes[n].occupation.getFighter() > -1)){
			pp=hexes[n].occupation.getPlayer();
			bb=hexes[n].occupation.getBase();
			ff=hexes[n].occupation.getFighter();
			
			if (hexes[n].occupation.getPlayer() != p){ // occupied by enemy
				if (hexes[n].occupation.getFighter() > -1) // occupied by a fighter
					if (player[p].base.get(b).fighter.get(f).getRank() <= player[pp].base.get(bb).fighter.get(ff).getRank())
						for (int nn = 0; nn < 6; nn++)
							if (hexes[n].neighbor[nn]>-1)
								if (hexes[hexes[n].neighbor[nn]].occupation.getPlayer() == pp)
									hexes[hexes[n].neighbor[nn]].setHighlighted(false);

				if (hexes[n].occupation.getOccupiedBy().equalsIgnoreCase("base")) // occupied by a base
					if (player[p].base.get(b).fighter.get(f).getRank()<=player[pp].base.get(bb).getRank())
						for (int nn=0;nn<6;nn++)
							if (hexes[n].neighbor[nn]>-1)
								if (hexes[hexes[n].neighbor[nn]].occupation.getPlayer() == pp)
									hexes[hexes[n].neighbor[nn]].setHighlighted(false);
			}
		}
	}
	
	public static void purchaseFighter(int p, int b){
		buttonPanel.endturn.setEnabled(false);
		
		//if ((!player[playerTurn].isAI())&&(sound))
			//SoundEffect.PurchaseFighter.play();
		
		Fighter tempFighter = new Fighter();
		player[p].base.get(b).fighter.add(tempFighter);
		int f = player[p].base.get(b).fighter.size()-1;

		player[p].base.get(b).fighter.get(f).setHexNum(player[p].base.get(b).getHexNum());
		player[p].base.get(b).fighter.get(f).setMoved(false);
		player[p].base.get(b).fighter.get(f).setX(player[p].base.get(b).getCol());
		player[p].base.get(b).fighter.get(f).setY(player[p].base.get(b).getRow());
		player[p].base.get(b).fighter.get(f).setRank(1);
		player[p].base.get(b).fighter.get(f).setBaseNum(b);
		player[p].base.get(b).setMoney(-FIGHTER_RANK_ONE_EXPENSE*5);
		player[p].base.get(b).fighter.get(f).setCost(FIGHTER_RANK_ONE_EXPENSE);
		player[p].base.get(b).setTempExpense(player[p].base.get(b).getTempExpense()+10);
		selectFighter(p,b,f);
		buttonPanel.lblBank.setMessage(""+player[selectedPlayer].base.get(selectedBase).getMoney());
	}
	
	public static void purchaseOutpost(int p, int b){
		isPlacingOutpost = true;
		for (int h = 0; h < BSIZE; h++){
			if ((hexes[h].occupation.getPlayer() == p) && (hexes[h].occupation.getBase() == b) && (hexes[h].occupation.getOccupiedBy() == "empty"))
				hexes[h].setHighlighted(true);
		}
	}
	
	public static void checkBaseDuplicateDrawings(){
		for (int p=0;p<=numPlayers;p++){
			for (int b = 0; b <= player[p].base.size(); b++){
				int tally=0;
				for (int h=0;h<BSIZE;h++){
					if (hexes[h].occupation.getPlayer() == p){
						if (hexes[h].occupation.getBase()==b){
							if (hexes[h].occupation.getOccupiedBy()=="base"){
								tally++;
							}
							if (tally>1){
								hexes[h].occupation.setOccupiedBy("empty");
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public static void clearGridHighlight(){
		for (int h=0;h<BSIZE;h++)
			hexes[h].setHighlighted(false);
	}
	
	public static void calculateBaseBudget(){
		int income = 0;
		
		if (selectedBase == -1)
			return;
		
		for (int b = 0; b < player[playerTurn].base.size(); b++){
			income = 0;
			for (int h = 0; h < BSIZE; h++){
				if ((hexes[h].occupation.getPlayer() == playerTurn) && (hexes[h].occupation.getBase() == b)){
					if (!hexes[h].hasFalseIncome){
						income++;
					}
				}
			}
			player[playerTurn].base.get(b).setIncome(income+player[playerTurn].base.get(b).getBonus());
			player[playerTurn].base.get(b).setMoney(player[playerTurn].base.get(b).getIncome());
		}
		payFighters();
		for (int b = 0; b < player[playerTurn].base.size(); b++){
			player[playerTurn].base.get(b).setExpense(-player[playerTurn].base.get(b).getExpense());
			for (int f = 0; f < player[playerTurn].base.get(b).fighter.size(); f++)
				player[playerTurn].base.get(b).setExpense(player[playerTurn].base.get(b).fighter.get(f).getCost());
			
			player[playerTurn].base.get(b).setBalance(player[playerTurn].base.get(b).getIncome()-player[playerTurn].base.get(b).getExpense());
			player[playerTurn].base.get(b).setMoney(-player[playerTurn].base.get(b).getExpense());
			if (player[playerTurn].base.get(b).getMoney()<0)
				player[playerTurn].base.get(b).setMoney(-player[playerTurn].base.get(b).getMoney());
		}
	}
	
	public static void payFighters(){
		int tempMoney=0;
				
		for (int b = 0; b < player[playerTurn].base.size(); b++){
			tempMoney=player[playerTurn].base.get(b).getMoney();
			for (int f = 0; f < player[playerTurn].base.get(b).fighter.size(); f++){
				if ((tempMoney-player[playerTurn].base.get(b).fighter.get(f).getCost())>=0){
					tempMoney-=player[playerTurn].base.get(b).fighter.get(f).getCost();
				}else{
					//while (player[playerTurn].base.get(b).fighter.size() > 0)
						killFighter(playerTurn, b, f);
					break;
				}
			}
		}
	}
	
	public static void killSackedBaseFighters(int pp, int bb){
		player[playerTurn].setFightersKilled(player[playerTurn].getFightersKilled()+player[pp].base.get(bb).fighter.size());

		while (player[pp].base.get(bb).fighter.size() > 0)
			killFighter(pp, bb, 0);
	}
	
	public static void killFighter(int p,int b,int f){
		int h = player[p].base.get(b).fighter.get(f).getHexNum();
		
		if (h != player[p].base.get(b).getHexNum())
			hexes[h].setHasDeath(true);
		hexes[h].occupation.setFighter(-1);
		hexes[h].occupation.setFighterRank(1);
		hexes[h].occupation.setPlayerDeath(p);
		
		player[p].base.get(b).fighter.remove(f);
		
		for (int i = 0; i < player[p].base.get(b).fighter.size(); i++)
			hexes[player[p].base.get(b).fighter.get(i).getHexNum()].occupation.setFighter(i);
		
		if (sound)
			SoundEffect.KillFighter.play();
	}
	
	public static void combineFighters(int p, int b, int f1, int f2){
		player[p].base.get(b).fighter.get(f1).setRank(player[p].base.get(b).fighter.get(f1).getRank()+player[p].base.get(b).fighter.get(f2).getRank());
		
		if (player[p].base.get(b).fighter.get(f1).getRank()>4)
			player[p].base.get(b).fighter.get(f1).setRank(4);
		
		if (player[p].base.get(b).fighter.get(f1).getRank()==2)
			player[p].base.get(b).fighter.get(f1).setCost(FIGHTER_RANK_TWO_EXPENSE);
		else if (player[p].base.get(b).fighter.get(f1).getRank()==3)
			player[p].base.get(b).fighter.get(f1).setCost(FIGHTER_RANK_THREE_EXPENSE);
		else if (player[p].base.get(b).fighter.get(f1).getRank()==4)
			player[p].base.get(b).fighter.get(f1).setCost(FIGHTER_RANK_FOUR_EXPENSE);
		
		if (player[p].base.get(b).fighter.get(f2).isMoved())
			player[p].base.get(b).fighter.get(f1).setMoved(true);
		hexes[player[p].base.get(b).fighter.get(f2).getHexNum()].occupation.setFighter(f1);
		hexes[player[p].base.get(b).fighter.get(f2).getHexNum()].occupation.setFighterRank(player[p].base.get(b).fighter.get(f1).getRank());
		
		hexes[player[p].base.get(b).fighter.get(f1).getHexNum()].occupation.setFighter(-1);
		
		player[p].base.get(b).fighter.get(f1).setHexNum(player[p].base.get(b).fighter.get(f2).getHexNum());
		
		player[p].base.get(b).fighter.remove(f2);
		
		for (int i = 0; i < player[p].base.get(b).fighter.size(); i++)
			hexes[player[p].base.get(b).fighter.get(i).getHexNum()].occupation.setFighter(i);
		
		for (int fff = 0; fff < player[p].base.get(b).fighter.size(); fff++){
			if (player[p].base.get(b).fighter.get(fff).isSelected()){
				selectedFighter = fff;
				player[p].base.get(b).fighter.get(fff).setSelected(false);
				break;
			}
		}
		
		buttonPanel.endturn.setEnabled(true);
		
		selectedFighter = -1;
		
		if ((!player[playerTurn].isAI())&&(sound))
			SoundEffect.Combine.play();
	}
	
	public static void playerHexesLinked(){
		for (int p=0;p<=numPlayers;p++){
			player[p].setHexesLinked(-player[p].getHexesLinked());
			for (int b = 0; b < player[p].base.size(); b++)
				player[p].setHexesLinked(player[p].base.get(b).getHexesLinked());
		}
	}
	
	public static void sackBaseVerOld(int p, int b, int pp, int bb){
		int baseBig=0,base=0;
		
		//if (sound)
			//SoundEffect.SackBase.play();

		for (int h=0;h<BSIZE;h++){
			if ((hexes[h].occupation.getPlayer() == pp)&&(hexes[h].occupation.getBase()==bb)){
				hexes[h].occupation.setPlayer(p);
				hexes[h].occupation.setBase(b);
			}
		}

		killSackedBaseFighters(pp,bb);
		hexes[player[pp].base.get(bb).getHexNum()].occupation.setOccupiedBy("empty");
		
		player[pp].base.remove(bb);
		
		for (int i = 0; i < BSIZE; i++)
			if (hexes[i].occupation.getPlayer() == pp)
				hexes[i].occupation.setBase(-1);
		
		for (int i = 0; i < player[pp].base.size(); i++){
			hexes[player[pp].base.get(i).getHexNum()].occupation.setBase(i);
			setBaseHexes(pp, i);
		}
		
		checkBaseLink(p);
		
		int tallyBases=0;
		for (int enemy=0;enemy<=numPlayers;enemy++){
			if (enemy!=p)
				tallyBases+=(player[enemy].base.size());
		}
		
		if (tallyBases>0)
			return;

		if (p==0)
			gameStatus=GameStatus.PLAYER1WON;
		else if (p==1)
			gameStatus=GameStatus.PLAYER2WON;
		else if (p==2)
			gameStatus=GameStatus.PLAYER3WON;
		else if (p==3)
			gameStatus=GameStatus.PLAYER4WON;
		else if (p==4)
			gameStatus=GameStatus.PLAYER5WON;
		else if (p==5)
			gameStatus=GameStatus.PLAYER6WON;
		
		StdDraw.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (sound)
			SoundEffect.GameOver.play();
		turn++;
		hexLink();
		hexLinkColor();
		playerHexesLinked();
		StdDraw.drawSideInfo(79, 60);
		setVisibleGrid();
		
		for (int pl = 0; pl <= numPlayers; pl++){
			int numFighters = 0, wages = 0;
			if (pl == p)
				player[pl].hexHistory.add((double) player[pl].getHexesLinked());
			else 
				player[pl].hexHistory.add((double) 0);
			for (int bbb = 0; bbb < player[pl].base.size(); bbb++){
				numFighters += player[pl].base.get(bbb).fighter.size();
				wages += player[pl].base.get(bbb).getExpense();
			}
			player[pl].menHistory.add((double) numFighters);
			player[pl].wageHistory.add((double) wages);
		}
		
		timerAI.cancel();
		timerAI.purge();
	}
	
	public static void sackBaseVerNew(int p, int b, int pp, int bb){
		int baseBig=0,base=0;
		
		player[p].setBasesSacked(player[p].getBasesSacked()+1);
		player[p].base.get(b).setIncome(player[p].base.get(b).getIncome()+player[pp].base.get(bb).getSavings());
		player[p].base.get(b).setMoney(player[pp].base.get(bb).getSavings());
		setDashboardLabels(false);
		
		if (!player[playerTurn].isAI()){
			Particle par = new Particle();
			particle.add(par);
			particle.get(0).reset(player[pp].base.get(bb).getHexNum(), player[p].base.get(b).getHexNum());
			particle.get(0).setAlive(true);
			particle.get(0).setMessage("$"+player[pp].base.get(bb).getSavings());
		}
		
		//if (sound)
			//SoundEffect.SackBase.play();
		if (player[pp].base.get(bb).getHexesLinked() <= 1000){
			for (int h=0;h<BSIZE;h++){
				if ((hexes[h].occupation.getPlayer() == pp)&&(hexes[h].occupation.getBase()==bb)){
					hexes[h].occupation.setPlayer(p);
					hexes[h].occupation.setBase(b);
				}
			}

			killSackedBaseFighters(pp,bb);
			checkBaseLink(p);
			hexes[player[pp].base.get(bb).getHexNum()].occupation.setOccupiedBy("empty");
			
			player[pp].base.remove(bb);
			
			for (int i = 0; i < BSIZE; i++)
				if (hexes[i].occupation.getPlayer() == pp)
					hexes[i].occupation.setBase(-1);
			
			for (int i = 0; i < player[pp].base.size(); i++){
				hexes[player[pp].base.get(i).getHexNum()].occupation.setBase(i);
				setBaseHexes(pp, i);
			}
		}else{
			for (int n = 0; n < 6; n++)
				if (hexes[player[pp].base.get(bb).getHexNum()].neighbor[n] > -1)
					if (hexes[hexes[player[pp].base.get(bb).getHexNum()].neighbor[n]].occupation.getPlayer() == pp)
						if (hexes[hexes[player[pp].base.get(bb).getHexNum()].neighbor[n]].occupation.getBase() == bb){
							player[pp].base.get(bb).setHexNum(hexes[player[pp].base.get(bb).getHexNum()].neighbor[n]);
							break;
						}
			killSackedBaseFighters(pp,bb);
			hexes[player[pp].base.get(bb).getHexNum()].occupation.setOccupiedBy("base");
			player[pp].base.get(bb).setSavings(0);
			player[pp].base.get(bb).setMoney(-player[pp].base.get(bb).getMoney());
		}
	
		
		int tallyBases=0;
		for (int enemy=0;enemy<=numPlayers;enemy++){
			if (enemy!=p)
				tallyBases+=(player[enemy].base.size());
		}
		
		if (tallyBases>0)
			return;

		hexList.clear();
		
		if (p==0)
			gameStatus=GameStatus.PLAYER1WON;
		else if (p==1)
			gameStatus=GameStatus.PLAYER2WON;
		else if (p==2)
			gameStatus=GameStatus.PLAYER3WON;
		else if (p==3)
			gameStatus=GameStatus.PLAYER4WON;
		else if (p==4)
			gameStatus=GameStatus.PLAYER5WON;
		else if (p==5)
			gameStatus=GameStatus.PLAYER6WON;
		
		StdDraw.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (sound)
			SoundEffect.GameOver.play();
		turn++;
		hexLink();
		hexLinkColor();
		playerHexesLinked();
		StdDraw.drawSideInfo(79, 60);
		setVisibleGrid();
		
		for (int pl = 0; pl <= numPlayers; pl++){
			int numFighters = 0, wages = 0;
			if (pl == p)
				player[pl].hexHistory.add((double) player[pl].getHexesLinked());
			else 
				player[pl].hexHistory.add((double) 0);
			for (int bbb = 0; bbb < player[pl].base.size(); bbb++){
				numFighters += player[pl].base.get(bbb).fighter.size();
				wages += player[pl].base.get(bbb).getExpense();
			}
			player[pl].menHistory.add((double) numFighters);
			player[pl].wageHistory.add((double) wages);
		}
		
		timerAI.cancel();
		timerAI.purge();
	}
	
	public static void placeFighterOnHex(int h, int p, int b, int f, int x, int y, String type){
		int pp;
		if ((!player[playerTurn].isAI())&&(sound))
			SoundEffect.Place.play();
		
		Hex[] tempHexes = new Hex[BSIZE];
		
		for (int i = 0; i < BSIZE; i++){
			tempHexes[i] = new Hex();
			tempHexes[i].occupation = new hexOccupation();
			hexes[i].setNew(false);
			tempHexes[i].occupation.setPlayer(hexes[i].occupation.getPlayer());
			tempHexes[i].occupation.setBase(hexes[i].occupation.getBase());
			hexes[i].setFighterCover(false);
		}
		
		hexes[player[p].base.get(b).fighter.get(f).getHexNum()].occupation.setFighter(-1);
		hexes[h].occupation.setOccupiedBy("empty");
		
		pp = hexes[h].occupation.getPlayer();
		
		if (type.equalsIgnoreCase("sack"))
			sackBaseVerNew(playerTurn, b, hexes[selectedHex].occupation.getPlayer(), hexes[selectedHex].occupation.getBase());
		
		hexHighlight = -1;
		player[p].base.get(selectedBase).fighter.get(f).setHexNum(h);
		player[p].base.get(selectedBase).fighter.get(f).setX(x);
		player[p].base.get(selectedBase).fighter.get(f).setY(y);
		hexes[h].occupation.setPlayer(p);
		hexes[h].occupation.setFighter(f);
		hexes[h].occupation.setFighterRank(player[p].base.get(selectedBase).fighter.get(f).getRank());
		hexes[h].occupation.setBase(selectedBase);
		player[p].base.get(selectedBase).fighter.get(f).setMoved(true);
		hexes[h].setHasDeath(false);
		hexes[h].setHasFalseIncome(false);
		hexes[h].setLinked(true);

		checkBaseLink(p);
		selectedFighter = -1;
		hexLink();
		checkDivision();
		buttonPanel.endturn.setEnabled(true);

		if (player[playerTurn].isAI())
			return;
		
		for (int i = 0; i < BSIZE; i++){
			if (tempHexes[i].occupation.getPlayer() != hexes[i].occupation.getPlayer())
				hexes[i].setNew(true);
			if ((tempHexes[i].occupation.getPlayer() == playerTurn) && (tempHexes[i].occupation.getPlayer() == hexes[i].occupation.getPlayer()) && (tempHexes[i].occupation.getBase() == -1) && (hexes[i].occupation.getBase() > -1)){
				hexes[i].setNew(true);
				hexes[i].setFighterCover(true);
			}
			//if ((tempHexes[i].occupation.getPlayer() == playerTurn) && (tempHexes[i].occupation.getPlayer() == hexes[i].occupation.getPlayer()) && (hexes[i].occupation.getBase() == selectedBase) && (tempHexes[i].occupation.getBase() > -1) && (tempHexes[i].occupation.getBase() != selectedBase))
				//hexes[i].setNew(true);
		}
		
		if (hexes[h].isNew()){
			innerLoop slist = new innerLoop();
			slist.setTarHex(player[p].base.get(selectedBase).getHexNum());
			slist.innerHexList.add(h);
			slist.setP(p);
			slist.setPp(pp);
			hexList.add(slist);
			
			if (type.equalsIgnoreCase("sack"))
				for (int i = 0; i < 3; i ++)
					hexList.add(slist);
		}
		
		for (int i = 0; i < hexList.size(); i++){
			for (int ii = 0; ii < hexList.get(i).innerHexList.size(); ii++){
				for (int n = 0; n < 6; n++){
					if (hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n] == -1)
						continue;
					if (hexes[hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n]].isNew()){
						innerLoop list = new innerLoop();
						list.innerHexList.add(hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n]);
						list.setTarHex(player[p].base.get(selectedBase).getHexNum());
						list.setP(p);
						if (hexes[hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n]].isFighterCover())
							list.setPp(p);
						else
							list.setPp(pp);
						hexList.add(list);
						if (type.equalsIgnoreCase("sack"))
							for (int iii = 0; iii < 3; iii ++)
								hexList.add(list);
						hexes[hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n]].setNew(false);
					}
				}
			}
		}
	}
	
	public static void checkBaseLink(int p){
		ArrayList<Integer> intBaseLinkList = new ArrayList<Integer>();
		boolean dup = false;
		for (int h = 0; h < BSIZE; h++){
			if (hexes[h].occupation.getPlayer() != p)
				continue;
			if (hexes[h].occupation.getBase() != selectedBase)
				continue;
			if (!hexes[h].isLinked())
				continue;
			for (int n=0;n<6;n++)
				if (hexes[h].neighbor[n]>=0)
					if (hexes[hexes[h].neighbor[n]].isLinked())
						if (hexes[hexes[h].neighbor[n]].occupation.getPlayer()==p)
							if ((hexes[hexes[h].neighbor[n]].occupation.getBase() != selectedBase) && (hexes[hexes[h].neighbor[n]].occupation.getBase() >= 0)){
								for (int i = 0; i < intBaseLinkList.size(); i++){
									if (intBaseLinkList.get(i) == hexes[hexes[h].neighbor[n]].occupation.getBase()){
										dup = true;
										break;
									}
								}
								if (!dup)
									intBaseLinkList.add(hexes[hexes[h].neighbor[n]].occupation.getBase());
								dup = false;
							}
			}
		
		if (intBaseLinkList.size() > 0)
			linkBases(p, intBaseLinkList, selectedBase);
	}
	
	public static void linkBases(int p, ArrayList<Integer> baseList, int bigb){
		int mainBase = bigb, inc;
		for (int b = 0; b < baseList.size(); b++){
			player[p].base.get(baseList.get(b)).setAlive(true);
			for (int h = 0; h < BSIZE; h++)
				if (hexes[h].occupation.getPlayer() == p)
					if (hexes[h].occupation.getBase() == baseList.get(b))
						hexes[h].occupation.setBase(bigb);
		}
		
		player[p].base.get(bigb).setSelected(true);
		
		while (baseList.size() > 0){
			baseList.clear();
			for (int i = 0; i < player[p].base.size(); i++){
				if (player[p].base.get(i).isSelected())
					mainBase = i;
				if (player[p].base.get(i).isAlive())
					baseList.add(i);
			}
		
			inc = baseList.get(0);

			player[p].base.get(mainBase).fighter.addAll(player[p].base.get(inc).fighter);
			player[p].base.get(inc).fighter.clear();
			hexes[player[p].base.get(inc).getHexNum()].occupation.setOccupiedBy("empty");
			if (player[p].base.get(inc).getRank()>player[p].base.get(mainBase).getRank())
				player[p].base.get(mainBase).setRank(player[p].base.get(inc).getRank());
			player[p].base.get(mainBase).setMoney(player[p].base.get(inc).getMoney());
			player[p].setBasesLinked(player[p].getBasesLinked()+1);
			player[p].base.get(inc).setAlive(false);
			player[p].base.remove(inc);
			baseList.remove(0);
		}
		
		for (int i = 0; i < player[p].base.size(); i++)
			if (player[p].base.get(i).isSelected())
				mainBase = i;
		
		for (int i = 0; i < BSIZE; i++)
			if (hexes[i].occupation.getPlayer() == p)
				hexes[i].occupation.setBase(-1);
		
		for (int i = 0; i < player[p].base.size(); i++){
			hexes[player[p].base.get(i).getHexNum()].occupation.setBase(i);
			setBaseHexes(p, i);
			for (int f = 0; f < player[p].base.get(i).fighter.size(); f++)
				hexes[player[p].base.get(i).fighter.get(f).getHexNum()].occupation.setFighter(f);
		}
		
		selectedBase = mainBase;
		player[p].base.get(mainBase).setSelected(false);
		
		if ((!player[playerTurn].isAI())&&(sound))
			SoundEffect.Link.play();
	}
	
	public static void setBaseHexes(int p, int b){
				int iWork=0;
				do{iWork++;
					int hex=0;
					int row=-1;
					int col=0;
					do{ row++;
						hex=row;
						do{ col++;
						if (!hexes[hex].isReal())
							continue;
						if (hexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
								if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
									if (hexes[hex].neighbor[iii]==-1){
										hexes[hex].line[iii] = b;
										continue;
									}
									if ((hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p) && (hexes[hexes[hex].neighbor[iii]].isLinked())){
										hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
										hexes[hex].line[iii] = -1;
									}else{
										hexes[hex].line[iii] = b;
									}
								}
							}while(iii<5);
						}
						hex+=ROWSIZE;
						} while (col<COLSIZE);
					} while (row<(ROWSIZE-1));
				
					row=ROWSIZE;
					col=0;
					do{ row--;
						hex=row+((COLSIZE-1)*ROWSIZE);
						do{ col++;
						if (!hexes[hex].isReal())
							continue;
						if (hexes[hex].occupation.getPlayer() == p){
							int iii=-1;
							do{iii++;
							if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
								if (hexes[hex].neighbor[iii]==-1){
									hexes[hex].line[iii] = b;
									continue;
								}
								if ((hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p) && (hexes[hexes[hex].neighbor[iii]].isLinked())){
									hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
									hexes[hex].line[iii] = -1;
								}else{
									hexes[hex].line[iii] = b;
								}
							}
							}while(iii<5);
						}
						hex-=ROWSIZE;
						} while (col<COLSIZE);
					} while (row>0);
					
					//-------------------------------
					
					hex=-1;
					do{ hex++;
					if (!hexes[hex].isReal())
						continue;
				    	if (hexes[hex].occupation.getPlayer() == p){
				    		int iii=-1;
				    		do{iii++;
				    		if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
								if (hexes[hex].neighbor[iii]==-1){
									hexes[hex].line[iii] = b;
									continue;
								}
								if ((hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p) && (hexes[hexes[hex].neighbor[iii]].isLinked())){
									hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
									hexes[hex].line[iii] = -1;
								}else{
									hexes[hex].line[iii] = b;
								}
							}
				    		}while(iii<5);

				    	}
					} while (hex<(BSIZE)-1);
				
					hex=BSIZE;
					
					do{ hex--;
					if (!hexes[hex].isReal())
						continue;
					if (hexes[hex].occupation.getPlayer() == p){
			    		int iii=-1;
			    		do{iii++;
			    		if ((hexes[hex].isLinked())&&(hexes[hex].occupation.getBase()==b)){
							if (hexes[hex].neighbor[iii]==-1){
								hexes[hex].line[iii] = b;
								continue;
							}
							if ((hexes[hexes[hex].neighbor[iii]].occupation.getPlayer() == p) && (hexes[hexes[hex].neighbor[iii]].isLinked())){
								hexes[hexes[hex].neighbor[iii]].occupation.setBase(b);
								hexes[hex].line[iii] = -1;
							}else{
								hexes[hex].line[iii] = b;
							}
						}
			    		}while(iii<5);

			    	}
					
					} while (hex>0);
					
				}while(iWork<6);
	}
	
	static ArrayList<Integer> getNewBaseHexes(){
		ArrayList<Integer> intNewBaseHexList = new ArrayList<Integer>();
		ArrayList<Integer> intUnlinkedList = new ArrayList<Integer>();
		int player = -1, originHex = -1;
				
		Hex[] tempHexes = new Hex[hexVenture.BSIZE];
		for (int i = 0; i < hexes.length; i++){
			tempHexes[i] = new Hex();
			tempHexes[i].setReal(hexes[i].isReal());
			tempHexes[i].setLinked(hexes[i].isLinked());
			tempHexes[i].occupation = new hexOccupation();
			tempHexes[i].occupation.setPlayer(hexes[i].occupation.getPlayer());
			tempHexes[i].occupation.setBase(hexes[i].occupation.getBase());
			tempHexes[i].occupation.setFighter(hexes[i].occupation.getFighter());
			for (int n = 0; n < 6; n++)
				tempHexes[i].neighbor[n] = hexes[i].neighbor[n];
		}
		
		for (int i = 0; i < hexes.length; i++){
			if (!tempHexes[i].isReal)
				continue;
			
			if ((tempHexes[i].occupation.getBase() >= 0) && (!tempHexes[i].isLinked())){
				intUnlinkedList.add(i);
				player = hexes[i].occupation.getPlayer();
				originHex = i;
			}
		}

		if (!intUnlinkedList.isEmpty())
			intNewBaseHexList.addAll(getNewBaseHexes(player, originHex, tempHexes, intUnlinkedList));
		else
			return intUnlinkedList;
		
		return intNewBaseHexList;
	}

	public static ArrayList<Integer> getNewBaseHexes(int player, int originHex, Hex[] temphexes, ArrayList<Integer> intUnlinkedList ) {
		ArrayList<Integer> intNewBaseHexList = new ArrayList<Integer>();
		boolean newHex;
	
		temphexes[originHex].setNew(true);
		
		do{ newHex = false;
		for (int i = 0; i < intUnlinkedList.size(); i++){
			if (temphexes[intUnlinkedList.get(i)].isNew()){
				for (int n = 0; n < 6; n++){
					if (temphexes[intUnlinkedList.get(i)].neighbor[n] > -1){
						if ((temphexes[temphexes[intUnlinkedList.get(i)].neighbor[n]].occupation.getPlayer() == player) && (!temphexes[temphexes[intUnlinkedList.get(i)].neighbor[n]].isNew())){
							temphexes[temphexes[intUnlinkedList.get(i)].neighbor[n]].setNew(true);
							newHex = true;
						}
					}
				}
			}
		}
		}while (newHex);
		
		for (int i = 0; i < intUnlinkedList.size(); i++)
			if (temphexes[intUnlinkedList.get(i)].isNew())
				intNewBaseHexList.add(intUnlinkedList.get(i));
		
		return intNewBaseHexList;
	}
	
	static void checkDivision(){
		ArrayList<Integer> intMoveList = new ArrayList<Integer>();
		int pl = -1;
		
		do{
			intMoveList.clear();
			intMoveList.addAll(getNewBaseHexes());
		
			if (intMoveList.isEmpty())
				return;
			
			pl = hexes[intMoveList.get(0)].occupation.getPlayer();

			if (intMoveList.size() <= 0){
				for (int i = 0 ; i < intMoveList.size(); i++){
					hexes[intMoveList.get(i)].setSetColor(player[pl].getR(), player[pl].getG(), player[pl].getB());
					if (hexes[intMoveList.get(i)].occupation.getFighter() > -1)
						killFighter(pl, hexes[intMoveList.get(i)].occupation.getBase(), hexes[intMoveList.get(i)].occupation.getFighter());
				
					hexes[intMoveList.get(i)].occupation.setBase(-1);
					hexes[intMoveList.get(i)].setR(-hexes[intMoveList.get(i)].getR());
            		hexes[intMoveList.get(i)].setR(0);
            		hexes[intMoveList.get(i)].setG(-hexes[intMoveList.get(i)].getG());
            		hexes[intMoveList.get(i)].setG(0);
            		hexes[intMoveList.get(i)].setB(-hexes[intMoveList.get(i)].getB());
            		hexes[intMoveList.get(i)].setB(0);
				}
				continue;
			}
			
			for (int i = 0 ; i < intMoveList.size(); i++){
				hexes[intMoveList.get(i)].setR(-hexes[intMoveList.get(i)].getR()/2);
    			//hexes[intMoveList.get(i)].setR(0);
    			hexes[intMoveList.get(i)].setG(-hexes[intMoveList.get(i)].getG()/2);
    			//hexes[intMoveList.get(i)].setG(0);
    			hexes[intMoveList.get(i)].setB(-hexes[intMoveList.get(i)].getB()/2);
    			//hexes[intMoveList.get(i)].setB(0);
    			//hexes[intMoveList.get(i)].setX(hexes[intMoveList.get(i)].getX()+rand.nextInt(35));
    			//hexes[intMoveList.get(i)].setY(hexes[intMoveList.get(i)].getY()+20);
    			//hexes[intMoveList.get(i)].reset();
			}
		
			createNewBase(pl, intMoveList);
		
		}while (!intMoveList.isEmpty());
	}
	
	static void createNewBase(int pl, ArrayList<Integer> intBaseHexList){
		int b = -1, oldb = -1;
	
		Base tempBase = new Base();
		player[pl].base.add(tempBase);
		b = player[pl].base.size()-1;
		oldb=hexes[intBaseHexList.get(0)].occupation.getBase();                                                                                   

		player[pl].base.get(b).setHexNum(intBaseHexList.get(0));
					
		if (hexes[player[pl].base.get(b).getHexNum()].occupation.getFighter() > -1)
			killFighter(pl, oldb, hexes[player[pl].base.get(b).getHexNum()].occupation.getFighter());
					
		player[pl].base.get(b).setRank(1);
		hexes[player[pl].base.get(b).getHexNum()].occupation.setBase(b);
		hexes[player[pl].base.get(b).getHexNum()].occupation.setOccupiedBy("base");
		player[pl].base.get(b).setMoney(0);
		player[pl].base.get(b).setCol(hexes[player[pl].base.get(b).getHexNum()].getColumn());
		player[pl].base.get(b).setRow(hexes[player[pl].base.get(b).getHexNum()].getRow());
		hexLinkNewBase(pl, b, intBaseHexList);
		
		splitFighters(pl, b, oldb, intBaseHexList);
	}

	static void hexLinkNewBase(int p, int b, ArrayList<Integer> intNewBaseHexList) {
		for (int i = 0; i < intNewBaseHexList.size(); i++){
			hexes[intNewBaseHexList.get(i)].setLinked(true);
			hexes[intNewBaseHexList.get(i)].occupation.setBase(b);
		}
	}
	
	static void splitFighters(int pl, int b, int oldb, ArrayList<Integer> intBaseHexList){
		for (int i = 0; i < intBaseHexList.size(); i++){
			if (hexes[intBaseHexList.get(i)].occupation.getFighter() > -1){
				Fighter tempFighter = new Fighter();
				player[pl].base.get(b).fighter.add(tempFighter);
				int f = player[pl].base.get(b).fighter.size()-1;
				int oldf=hexes[intBaseHexList.get(i)].occupation.getFighter();

				player[pl].base.get(b).fighter.get(f).setBaseNum(b);
				player[pl].base.get(b).fighter.get(f).setHexNum(intBaseHexList.get(i));
				player[pl].base.get(b).fighter.get(f).setRank(player[pl].base.get(oldb).fighter.get(oldf).getRank());
				player[pl].base.get(b).fighter.get(f).setCost(player[pl].base.get(oldb).fighter.get(oldf).getCost());
				player[pl].base.get(b).fighter.get(f).setX(player[pl].base.get(oldb).fighter.get(oldf).getX());
				player[pl].base.get(b).fighter.get(f).setY(player[pl].base.get(oldb).fighter.get(oldf).getY());
				player[pl].base.get(b).fighter.get(f).setMoved(player[pl].base.get(oldb).fighter.get(oldf).isMoved());
					
				killFighter(pl, oldb, oldf);
				hexes[intBaseHexList.get(i)].setHasDeath(false);
				hexes[intBaseHexList.get(i)].occupation.setFighterRank(player[pl].base.get(b).fighter.get(f).getRank());
				hexes[intBaseHexList.get(i)].occupation.setFighter(f);
			}
		}
	}

	public static void setPlayerBaseHighestRankedFighter(){
		for (int p=0;p<=numPlayers;p++){
			for (int b = 0; b < player[p].base.size(); b++){
				player[p].base.get(b).setHighestRankedFighter(0);
				for (int f = 0;f < player[p].base.get(b).fighter.size(); f++){
					if (player[p].base.get(b).fighter.get(f).getRank() > player[p].base.get(b).getHighestRankedFighter())
						player[p].base.get(b).setHighestRankedFighter(player[p].base.get(b).fighter.get(f).getRank());
				}
			}
		}
	}
	
	public static void setAiSpeed(int speed){
		aiSpeed = speed;
	}
	
	public static void setBases(){ // place each players initial bases
		boolean isPlaceable=false;
		
		for (int p=0;p<=numPlayers;p++){
		for (int b=0;b<1;b++){
			Base tempBase = new Base();
			player[p].base.add(tempBase);
			player[p].base.get(player[p].base.size()-1).setRank(1);
			player[p].base.get(player[p].base.size()-1).setHexesLinked(0);
			player[p].base.get(player[p].base.size()-1).setMoney(-player[p].base.get(player[p].base.size()-1).getMoney());

			do{ isPlaceable=true;
				player[p].base.get(player[p].base.size()-1).setHexNum(rand.nextInt(BSIZE));

				if (!hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].isReal())
					isPlaceable=false;
				
				for (int n=0;n<6;n++){
					if (hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].neighbor[n] >= 0){
						if (hexes[hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].neighbor[n]].occupation.getOccupiedBy() == "base")
							isPlaceable=false;
						
						for (int nn=0;nn<6;nn++){
							if (hexes[hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].neighbor[n]].neighbor[nn] >= 0){
								if (hexes[hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].neighbor[n]].occupation.getOccupiedBy() == "base")
									if (hexes[hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].neighbor[n]].occupation.getPlayer() == p)
										isPlaceable=false;
							}
						}
					}
				}
			}while (!isPlaceable);
			
			hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].setSetColor(player[p].getR(), player[p].getG(), player[p].getB());
			hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].occupation.setPlayer(p);
			hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].occupation.setBase(b);
			hexes[player[p].base.get(player[p].base.size()-1).getHexNum()].occupation.setOccupiedBy("base");
			player[p].base.get(player[p].base.size()-1).setMoney(8);
		}
		
		}
	}
	
	public static void replayGameTimer(){
		
		timerReplayGame.scheduleAtFixedRate(new TimerTask() {
			
            public void run() {
            	fx++;

        		for (int i = 0; i < (BSIZE); i++) {
        			if (!hexes[i].isReal())
        				continue;
        			hexx = i;
        			
        			hexes[i].setColor(hexes[i].hexSaveState.get(fx).getColor());
        			StdDraw.drawHex(i);
        			
        			if (hexes[i].hexSaveState.get(fx).isBase()){
        				double x = hexes[i].getX() + 26;
        				double y = hexes[i].getY() + 23;
        				StdDraw.drawBase(x, y, 2.1, hexes[i].hexSaveState.get(fx).getBaseRank(), hexes[i].hexSaveState.get(fx).getPlayer(), 0);
        			}else if (hexes[i].hexSaveState.get(fx).isFighter()){
        				double xx = hexes[i].getX() + 26;
						double yy = hexes[i].getY() + 23;
        				StdDraw.drawFighter(xx, yy, 1.1, hexes[i].hexSaveState.get(fx).getFighterRank(), hexes[i].hexSaveState.get(fx).getPlayer(), 0, 0);
        			}
        		}
        		
        		if ((hexes[hexx].hexSaveState.size()-1) == fx){
        			StdDraw.showStats();
        			resume = true;
        			simulate();
        			timerReplayGame.cancel();
        		}
        		StdDraw.show();
        		
            }
        }, delay, 100);
		
	}

	public static ArrayList<Integer> testTwo(int p, int b, ArrayList<Integer>surroundings){
		List<Integer> newSurroundings = new ArrayList<Integer>();
		boolean goodPlace  = false;
		for (int temp = 0; temp < surroundings.size(); temp++){
			if (!hexes[surroundings.get(temp)].isReal())
				continue;
				for (int n=0;n<6;n++){
					if (hexes[surroundings.get(temp)].neighbor[n]>=0){
						if (!hexes[hexes[surroundings.get(temp)].neighbor[n]].isReal())
							continue;
							goodPlace  = true;
							for (int nn=0;nn<6;nn++){
								if (hexes[hexes[surroundings.get(temp)].neighbor[n]].neighbor[nn]>=0){
									if ((hexes[hexes[hexes[surroundings.get(temp)].neighbor[n]].neighbor[nn]].occupation.getPlayer() == p) && (hexes[hexes[hexes[surroundings.get(temp)].neighbor[n]].neighbor[nn]].occupation.getBase() != b)){
										goodPlace = false;
									}
								}
							}
					}
					if ((goodPlace) && (hexes[surroundings.get(temp)].neighbor[n] >= 0)){
						newSurroundings.add(hexes[surroundings.get(temp)].neighbor[n]);
					}
				}
		}
		
		for (int temp = 0; temp < newSurroundings.size(); temp++){
			if (!hexes[newSurroundings.get(temp)].isReal())
				continue;

			hexes[newSurroundings.get(temp)].occupation.setPlayer(p);
			hexes[newSurroundings.get(temp)].occupation.setBase(b);
			hexes[newSurroundings.get(temp)].setLinked(true);
			hexes[newSurroundings.get(temp)].setVisible(true);
			hexes[newSurroundings.get(temp)].setSetColor(player[p].getR(), player[p].getG(), player[p].getB());
			
			break;
		}
			
		return (ArrayList<Integer>) newSurroundings;
	}

	public static int getLinkedHexAmt(int p, int h){
		ArrayList<innerLoop> tempList = new ArrayList<innerLoop>();
	
		Hex[] tempHexes = new Hex[hexVenture.BSIZE];
		for (int i = 0; i < hexes.length; i++){
			tempHexes[i] = new Hex();
			tempHexes[i].setReal(hexes[i].isReal());
			tempHexes[i].setLinked(hexes[i].isLinked());
			tempHexes[i].occupation = new hexOccupation();
			tempHexes[i].occupation.setPlayer(hexes[i].occupation.getPlayer());
			tempHexes[i].occupation.setBase(hexes[i].occupation.getBase());

			for (int n = 0; n < 6; n++)
				tempHexes[i].neighbor[n] = hexes[i].neighbor[n];
		}                                                                          
		
		tempHexes[h].occupation.setBase(player[p].base.size());

		innerLoop slist = new innerLoop();
		slist.innerHexList.add(h);
		tempList.add(slist);
		
		for (int i = 0; i < tempList.size(); i++){
			for (int ii = 0; ii < tempList.get(i).innerHexList.size(); ii++){
				for (int n = 0; n < 6; n++){
					if (hexes[tempList.get(i).innerHexList.get(ii)].neighbor[n] == -1)
						continue;
					if ((tempHexes[tempHexes[tempList.get(i).innerHexList.get(ii)].neighbor[n]].occupation.getPlayer() == p) && (tempHexes[tempHexes[tempList.get(i).innerHexList.get(ii)].neighbor[n]].occupation.getBase() == -1)){
						innerLoop list = new innerLoop();
						list.innerHexList.add(tempHexes[tempList.get(i).innerHexList.get(ii)].neighbor[n]);
						tempList.add(list);
						tempHexes[tempHexes[tempList.get(i).innerHexList.get(ii)].neighbor[n]].occupation.setBase(player[p].base.size());
					}
				}
			}
		}
		
		return tempList.size();
	}
	
	public static void setupGame(){
		int pl = 0, tempPl, base;
		
		for (int h = 0; h < BSIZE; h++){
			if (!hexes[h].isReal())
				continue;
			
			pl = rand.nextInt(numPlayers+1);
			hexes[h].occupation.setPlayer(pl);
			hexes[h].setVisible(true);
			hexes[h].setSetColor(player[pl].getR(), player[pl].getG(), player[pl].getB());
			
			for (int p = 0; p <= 5; p++)
				hexes[h].oldOccupation[p].setPlayer(pl);
		}
		
		for (int h = 0; h < BSIZE; h++){
			if (!hexes[h].isReal())
				continue;
			if (hexes[h].occupation.getBase() > -1)
				continue;
			
			pl = hexes[h].occupation.getPlayer();
			
			if (getLinkedHexAmt(pl, h) > 1){
			
				Base tempBase = new Base();
				player[pl].base.add(tempBase);
				base = player[pl].base.size()-1;                                                                             

				player[pl].base.get(base).setHexNum(h);
							
				player[pl].base.get(base).setRank(1);
				hexes[h].occupation.setBase(base);
				hexes[h].setLinked(true);
				hexes[h].occupation.setOccupiedBy("base");
				player[pl].base.get(base).setMoney(0);
				player[pl].base.get(base).setCol(hexes[h].getColumn());
				player[pl].base.get(base).setRow(hexes[h].getRow());
				player[pl].base.get(base).setMoney(5);
				
				innerLoop slist = new innerLoop();
				slist.innerHexList.add(h);
				hexList.add(slist);
			
				for (int i = 0; i < hexList.size(); i++){
					for (int ii = 0; ii < hexList.get(i).innerHexList.size(); ii++){
						for (int n = 0; n < 6; n++){
							if (hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n] == -1)
								continue;
							if ((hexes[hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n]].occupation.getPlayer() == pl) && (hexes[hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n]].occupation.getBase() == -1)){
								innerLoop list = new innerLoop();
								list.innerHexList.add(hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n]);
								hexList.add(list);
								hexes[hexes[hexList.get(i).innerHexList.get(ii)].neighbor[n]].occupation.setBase(base);
							}
						}
					}
				}
				hexList.clear();
				slist.innerHexList.clear();
			}
		}
		
		resume = true;
		playerTurn = -1;
		isEditor=false;
		StdDraw.menu.setVisible(false);
		numBonus+=1;
		//setRegions();
		hexLink();
		
		endTurn();
		buttonPanel.one.setupButton(player[playerTurn].getColorActual(), 89, 80, 10, 2, "fighter $10");
		buttonPanel.two.setupButton(player[playerTurn].getColorActual(), 89, 76, 10, 2, "---");
		buttonPanel.endturn.setupButton(player[playerTurn].getColorActual(), 89, 71, 10, 3, "end turn");
		buttonPanel.one.setVisible(true);
		buttonPanel.two.setVisible(true);
		buttonPanel.endturn.setVisible(true);
		buttonPanel.lblSavings.setVisible(true);
		buttonPanel.lblIncome.setVisible(true);
		buttonPanel.lblWages.setVisible(true);
		buttonPanel.lblBank.setVisible(true);
		buttonPanel.lblBankNext.setVisible(true);

		for (int p = 0; p <= numPlayers; p++){
			if (p == 0){
				StdDraw.p1.setForeground(player[p].getColorActual());
				StdDraw.p1.setText(player[p].getName());
				StdDraw.p1.setVisible(true);
				StdDraw.p1bs.setVisible(true);
				StdDraw.p1fk.setVisible(true);
				StdDraw.p1fs.setVisible(true);
			}
			if (p == 1){
				StdDraw.p2.setForeground(player[p].getColorActual());
				StdDraw.p2.setText(player[p].getName());
				StdDraw.p2.setVisible(true);
				StdDraw.p2bs.setVisible(true);
				StdDraw.p2fk.setVisible(true);
				StdDraw.p2fs.setVisible(true);
			}
			if (p == 2){
				StdDraw.p3.setForeground(player[p].getColorActual());
				StdDraw.p3.setText(player[p].getName());
				StdDraw.p3.setVisible(true);
				StdDraw.p3bs.setVisible(true);
				StdDraw.p3fk.setVisible(true);
				StdDraw.p3fs.setVisible(true);
			}
			if (p == 3){
				StdDraw.p4.setForeground(player[p].getColorActual());
				StdDraw.p4.setText(player[p].getName());
				StdDraw.p4.setVisible(true);
				StdDraw.p4bs.setVisible(true);
				StdDraw.p4fk.setVisible(true);
				StdDraw.p4fs.setVisible(true);
			}
			if (p == 4){
				StdDraw.p5.setForeground(player[p].getColorActual());
				StdDraw.p5.setText(player[p].getName());
				StdDraw.p5.setVisible(true);
				StdDraw.p5bs.setVisible(true);
				StdDraw.p5fk.setVisible(true);
				StdDraw.p5fs.setVisible(true);
			}
			if (p == 5){
				StdDraw.p6.setForeground(player[p].getColorActual());
				StdDraw.p6.setText(player[p].getName());
				StdDraw.p6.setVisible(true);
				StdDraw.p6bs.setVisible(true);
				StdDraw.p6fk.setVisible(true);
				StdDraw.p6fs.setVisible(true);
			}
		}
		StdDraw.endGameShadow.setVisible(false);
		StdDraw.endGameShadow.getContentPane().setBackground(Color.GRAY);
		StdDraw.endGameShadow.setBounds(StdDraw.endGame.getX()-10, StdDraw.endGame.getY()-10, StdDraw.endGame.getWidth()+20, StdDraw.endGame.getHeight()+20);
		
		int tally = 0;
		for (int i = 0; i <= numPlayers; i++){
			if (!player[i].isAI()){
				tally++;
				humanPlayer = i;
			}
		}
		
		//for (int h=0;h<BSIZE;h++)
			//hexes[h].setVisible(false);
		
		if (tally >= 1)
			oneHuman = true;
		
		//if (sound)
			//SoundEffect.EndTurn.play();
	}
	
	public static boolean isHexDivisible(int p, int h){
		int n = 0, flipTally = 0;
		boolean flip = false, prevFlip = false;
		
		if (hexes[h].neighbor[n] < 0)
			flip = false;
		if (hexes[h].neighbor[n] > -1){
			if (hexes[hexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 2;
		prevFlip = flip;
			
		if (hexes[h].neighbor[n] < 0)
			flip = false;
		if (hexes[h].neighbor[n] > -1){
			if (hexes[hexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 3;
		if (prevFlip != flip)
			flipTally++;
		prevFlip = flip;
			
		if (hexes[h].neighbor[n] < 0)
			flip = false;
		if (hexes[h].neighbor[n] > -1){
			if (hexes[hexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 1;
		if (prevFlip != flip)
			flipTally++;
		prevFlip = flip;
		
		if (hexes[h].neighbor[n] < 0)
			flip = false;
		if (hexes[h].neighbor[n] > -1){
			if (hexes[hexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 4;
		if (prevFlip != flip)
			flipTally++;
		prevFlip = flip;
		
		if (hexes[h].neighbor[n] < 0)
			flip = false;
		if (hexes[h].neighbor[n] > -1){
			if (hexes[hexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		n = 5;
		if (prevFlip != flip)
			flipTally++;
		prevFlip = flip;
		
		if (hexes[h].neighbor[n] < 0)
			flip = false;
		if (hexes[h].neighbor[n] > -1){
			if (hexes[hexes[h].neighbor[n]].occupation.getPlayer() != p){
				flip = false;
			}else{
				flip = true;
			}
		}
		if (prevFlip != flip)
			flipTally++;
		
		if (flipTally >= 3)
			return true;
		
		return false;
	}
	
	public static void playAiTimer(final int p){
		
		timerAI.scheduleAtFixedRate(new TimerTask() {
			
            public void run() {

            	fx++;
	
            	if (player[p].moveList.get(fx).isFighterPurchased()){
            		purchaseFighter(p, player[p].moveList.get(fx).getBase());
            	}else{
            		setMove(p, player[p].moveList.get(fx).getBase(), player[p].moveList.get(fx).getFighter(), player[p].moveList.get(fx).getSelectedHex());
    				hexLink();
            	}
            	
            	if (fx == player[p].moveList.size()-1)
            		endTurn();

            }
        }, delay, 100);
		
	}
	
	public static void setMove(int p, int b, int f, int h){
		int ff;

		selectedPlayer=p;
		selectedBase=b;
		selectedFighter=f;

		for (int i = 0; i < player[p].base.get(b).fighter.size(); i++)
			player[p].base.get(b).fighter.get(i).setSelected(false);
		
		hexes[player[p].base.get(b).fighter.get(f).getHexNum()].occupation.setFighter(-1);
		player[p].base.get(b).fighter.get(f).setSelected(true);
		
		if (hexes[h].occupation.getPlayer() != p){
			if (hexes[h].occupation.getFighter() > -1)
				killFighter(hexes[h].occupation.getPlayer(), hexes[h].occupation.getBase(), hexes[h].occupation.getFighter());
			else if (hexes[h].occupation.getOccupiedBy().equalsIgnoreCase("base"))
				sackBaseVerNew(p,b,hexes[h].occupation.getPlayer(), hexes[h].occupation.getBase());
		}
		else if (hexes[h].occupation.getPlayer() == p){
			if (hexes[h].occupation.getFighter() > -1){
				if (hexes[h].occupation.getFighter() != f){
					ff=hexes[h].occupation.getFighter();
					combineFighters(p,b,f,ff);
					return;
				}
			}
		}

		for (int fff = 0; fff < player[p].base.get(selectedBase).fighter.size(); fff++){
			if (player[p].base.get(selectedBase).fighter.get(fff).isSelected()){
				selectedFighter = fff;
				break;
			}
		}
		
		player[p].base.get(selectedBase).fighter.get(selectedFighter).setSelected(false);
		
		placeFighterOnHex(h,p,selectedBase,selectedFighter,hexes[h].getColumn(),hexes[h].getRow(), "null");
	}
	
	public static void testPlayerAiTurns(int p){
		AI ai = new AI();
		ai = new AI();
		player[p].moveList.clear();
		player[p].moveList.addAll(ai.runTests(p));
		fx = -1;

		for (int i = 0; i < BSIZE; i++){
			hexes[i].setSumRank(ai.testHexes[i].getSumRank());
			hexes[i].setDefendRank(ai.testHexes[i].getDefendRank());
			hexes[i].setThreatRank(ai.testHexes[i].getThreatRank());
			hexes[i].setFighterCover(ai.testHexes[i].isFighterCover());
		}
		//for (int b = 0; b < player[p].base.size(); b++)
			//for (int f = 0; f < player[p].base.get(b).fighter.size(); f++)
				//player[p].base.get(b).fighter.get(f).setMove(ai.testPlayer[p].base.get(b).fighter.get(f).getMove());
		
		if (player[p].moveList.size() > 0){
			timerAI = new Timer();
			playAiTimer(p);
		}else{
			endTurn();
		}
	}
}