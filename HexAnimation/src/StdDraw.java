
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;

public final class StdDraw implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, ItemListener {

	private static int s=0;	// length of one side
	private static int t=0;	// short side of 30o triangle outside of each hex
	private static int r=0;	// radius of inscribed circle (center to middle of each side). r= h/2
	private static int h=0;	// height. Distance between centers of two adjacent hexes. Distance between two opposite sides in a hex.
	
	private static boolean mousePressed = false;
    private static double mouseX = 0;
    private static double mouseY = 0;
    private static double mouseXX = 0;
    private static double mouseYY = 0;
    private static Object mouseLock = new Object();
    private static Object keyLock = new Object();
    
 // queue of typed key characters
    private static LinkedList<Character> keysTyped = new LinkedList<Character>();

    // set of key codes currently pressed down
    private static TreeSet<Integer> keysDown = new TreeSet<Integer>();
    
    // pre-defined colors
    public static final Color BLACK      = Color.BLACK;
    public static final Color BLUE       = Color.BLUE;
    public static final Color BOOK_BLUE       = new Color(  90,  180, 250);
    private static final Color DEFAULT_PEN_COLOR   = Color.WHITE;
    private static final Color DEFAULT_CLEAR_COLOR = new Color(  240,  240, 240);
    private static Color VISIBLEGRID =  new Color(70,70,70);
    private static Color INVISIBLEGRID =  new Color(30,30,30);
    private static Color VISIBLEFILL =  new Color(20,20,20);
    private static Color INVISIBLEFILL =  new Color(6,6,6);
    private static Color CAN_MOVE =  new Color(255,255,255);
    private static Color CANNOT_MOVE =  new Color(130,130,130);
    private static Color REGION;
    
    // current pen color
    private static Color penColor;

    // default canvas size is DEFAULT_SIZE-by-DEFAULT_SIZE
    private static final int DEFAULT_SIZE = 512;
    private static int width  = DEFAULT_SIZE;
    private static int height = DEFAULT_SIZE;

    // default pen radius
    private static final double DEFAULT_PEN_RADIUS = 0.02;

    // current pen radius
    private static double penRadius;
    private static double penDashRadius;
    
    // show we draw immediately or wait until next show?
    private static boolean defer = false;

    // boundary of drawing canvas, 5% border
    private static final double BORDER = 0.01;
    private static final double DEFAULT_XMIN = 0.0;
    private static final double DEFAULT_XMAX = 1.0;
    private static final double DEFAULT_YMIN = 0.0;
    private static final double DEFAULT_YMAX = 1.0;
    private static double xmin, ymin, xmax, ymax;

    // default font
    private static final Font TINY_FONT = new Font("Square721 BT", Font.BOLD, 22);
    private static final Font DEFAULT_FONT = new Font("Square721 BT", Font.BOLD, 22);
    private static final Font HEADING_FONT = new Font("Square721 BT", Font.BOLD, 22);
    private static final Font INFO_BIG_FONT = new Font("Square721 BT", Font.BOLD, 22);
    private static final Font INFO_SMALL_FONT = new Font("Square721 BT", Font.BOLD, 22);
    private static final Font GAME_OVER_FONT = new Font("Square721 BT", Font.BOLD, 100);
    
    // current font
    private static Font font;

    //private static ZoomAndPanListener zoomAndPanListener;
    
    // double buffered graphics
    private static BufferedImage offscreenImage, onscreenImage;
    private static Graphics2D offscreen, onscreen;

    static JTextField tf = new JTextField("player 1", 20);
    static JSlider sliderR = new JSlider(JSlider.HORIZONTAL, 0, 255, 50);
    static JSlider sliderG = new JSlider(JSlider.HORIZONTAL, 0, 255, 50);
    static JSlider sliderB = new JSlider(JSlider.HORIZONTAL, 0, 255, 50);
    static JSlider bonuses = new JSlider(JSlider.HORIZONTAL, 0, 15, 3);
    static JCheckBox AI = new JCheckBox("ai");
    static JButton addPlayer = new JButton("+ player");
    static JButton done = new JButton("start game");
    static JButton replay = new JButton("watch replay");
    static JLabel red = new JLabel("red");
    static JLabel green = new JLabel("green");
    static JLabel blue = new JLabel("blue");
    static JLabel lblBonuses = new JLabel("# of regions");
    static JLabel numBonuses = new JLabel("3");
    static JLabel mapProgress = new JLabel("0%");
    static JLabel p1 = new JLabel("player 1");
    static JLabel p2 = new JLabel("player 2");
    static JLabel p3 = new JLabel("player 3");
    static JLabel p4 = new JLabel("player 4");
    static JLabel p5 = new JLabel("player 5");
    static JLabel p6 = new JLabel("player 6");
    static JLabel bs = new JLabel("base sacks");
    static JLabel fk = new JLabel("men killed");
    static JLabel fs = new JLabel("men unpaid");
    static JLabel ba = new JLabel("cur balance");
    static JLabel mo = new JLabel("cur money");
    static JLabel p1bs = new JLabel("0");
    static JLabel p2bs = new JLabel("0");
    static JLabel p3bs = new JLabel("0");
    static JLabel p4bs = new JLabel("0");
    static JLabel p5bs = new JLabel("0");
    static JLabel p6bs = new JLabel("0");
    static JLabel p1bl = new JLabel("0");
    static JLabel p2bl = new JLabel("0");
    static JLabel p3bl = new JLabel("0");
    static JLabel p4bl = new JLabel("0");
    static JLabel p5bl = new JLabel("0");
    static JLabel p6bl = new JLabel("0");
    static JLabel p1fk = new JLabel("0");
    static JLabel p2fk = new JLabel("0");
    static JLabel p3fk = new JLabel("0");
    static JLabel p4fk = new JLabel("0");
    static JLabel p5fk = new JLabel("0");
    static JLabel p6fk = new JLabel("0");
    static JLabel p1fs = new JLabel("0");
    static JLabel p2fs = new JLabel("0");
    static JLabel p3fs = new JLabel("0");
    static JLabel p4fs = new JLabel("0");
    static JLabel p5fs = new JLabel("0");
    static JLabel p6fs = new JLabel("0");
    
    // the frame for drawing to the screen
    public static JFrame frame;
    static JFrame menu;
    static JFrame endGame;
    static JFrame endGameShadow;
    private static GraphPanel graph;
    
    private static JMenuBar menuBar;
    private static JMenu Bmenu, submenu;
    private static JMenuItem menuItem;
    private static JCheckBoxMenuItem cbMenuItem1, cbMenuItem2;
    
    // singleton pattern: client can't instantiate
    StdDraw() { }

    public static void setCanvasSize() {
        setCanvasSize(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public static void setCanvasSize(int w, int h) {
        if (w < 1 || h < 1) throw new IllegalArgumentException("width and height must be positive");
        width = w;
        height = h;
        init();
    }

    private static void init() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (frame != null) frame.setVisible(false);
        frame = new JFrame();
        frame.setLayout(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize( (int)(screenSize.width), screenSize.height);
        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        onscreenImage  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        offscreen = offscreenImage.createGraphics();
        onscreen  = onscreenImage.createGraphics();
        //setXscale();
        //setYscale();
        offscreen.setColor(DEFAULT_CLEAR_COLOR);
        offscreen.fillRect(0, 0, width, height);
        setPenColor();
        setPenRadius();
        setFont();
        //clear();

        StdDraw std = new StdDraw();

        endGame = new JFrame();
        endGame.setLayout(null);
        endGame.setExtendedState(JFrame.NORMAL);
        endGame.setResizable(false);
        endGame.setUndecorated(true);
        //endGame.setOpacity((float) .85);
        endGame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        endGame.setSize( (int)(500), 410);
        endGame.setBounds((screenSize.width/3)-40, screenSize.height/3, 500, 410);
        endGame.setTitle("End Game");
        endGame.setBackground(Color.LIGHT_GRAY);
        endGame.setVisible(false);
        endGame.setAlwaysOnTop(true);
        
        p1.setVisible(false);
        p1.setBounds(15, 310, 50, 20);
        p1.setForeground(Color.BLACK);
        p1.setFont(TINY_FONT);
        p2.setVisible(false);
        p2.setBounds(p1.getX(), p1.getY()+18, 50, 20);
        p2.setForeground(Color.BLACK);
        p2.setFont(p1.getFont());
        p3.setVisible(false);
        p3.setBounds(p1.getX(), p2.getY()+18, 50, 20);
        p3.setForeground(Color.BLACK);
        p3.setFont(p1.getFont());
        p4.setVisible(false);
        p4.setBounds(p1.getX(), p3.getY()+18, 50, 20);
        p4.setForeground(Color.BLACK);
        p4.setFont(p1.getFont());
        p5.setVisible(false);
        p5.setBounds(p1.getX(), p4.getY()+18, 50, 20);
        p5.setForeground(Color.BLACK);
        p5.setFont(p1.getFont());
        p6.setVisible(false);
        p6.setBounds(p1.getX(), p5.getY()+18, 50, 20);
        p6.setForeground(Color.BLACK);
        p6.setFont(p1.getFont());
        
        bs.setVisible(true);
        bs.setBounds(90, 293, 80, 20);
        bs.setForeground(Color.BLACK);
        bs.setFont(p1.getFont());  
        fk.setVisible(true);
        fk.setBounds(170, bs.getY(), 80, 20);
        fk.setForeground(Color.BLACK);
        fk.setFont(p1.getFont());       
        fs.setVisible(true);
        fs.setBounds(250, bs.getY(), 80, 20);
        fs.setForeground(Color.BLACK);
        fs.setFont(p1.getFont());        
        ba.setVisible(true);
        ba.setBounds(330, bs.getY(), 80, 20);
        ba.setForeground(Color.BLACK);
        ba.setFont(p1.getFont());
        mo.setVisible(true);
        mo.setBounds(410, bs.getY(), 80, 20);
        mo.setForeground(Color.BLACK);
        mo.setFont(p1.getFont());
        
        p1bs.setVisible(false);
        p1bs.setBounds(bs.getX()+25, p1.getY(), 30, 20);
        p1bs.setForeground(Color.BLACK);
        p1bs.setFont(TINY_FONT);
        p1fk.setVisible(false);
        p1fk.setBounds(fk.getX()+25, p1.getY(), 30, 20);
        p1fk.setForeground(Color.BLACK);
        p1fk.setFont(TINY_FONT);
        p1fs.setVisible(false);
        p1fs.setBounds(fs.getX()+25, p1.getY(), 30, 20);
        p1fs.setForeground(Color.BLACK);
        p1fs.setFont(TINY_FONT);
        
        p2bs.setVisible(false);
        p2bs.setBounds(bs.getX()+25, p2.getY(), 30, 20);
        p2bs.setForeground(Color.BLACK);
        p2bs.setFont(TINY_FONT);
        p2fk.setVisible(false);
        p2fk.setBounds(fk.getX()+25, p2.getY(), 30, 20);
        p2fk.setForeground(Color.BLACK);
        p2fk.setFont(TINY_FONT);
        p2fs.setVisible(false);
        p2fs.setBounds(fs.getX()+25, p2.getY(), 30, 20);
        p2fs.setForeground(Color.BLACK);
        p2fs.setFont(TINY_FONT);
        
        p3bs.setVisible(false);
        p3bs.setBounds(bs.getX()+25, p3.getY(), 30, 20);
        p3bs.setForeground(Color.BLACK);
        p3bs.setFont(TINY_FONT);
        p3fk.setVisible(false);
        p3fk.setBounds(fk.getX()+25, p3.getY(), 30, 20);
        p3fk.setForeground(Color.BLACK);
        p3fk.setFont(TINY_FONT);
        p3fs.setVisible(false);
        p3fs.setBounds(fs.getX()+25, p3.getY(), 30, 20);
        p3fs.setForeground(Color.BLACK);
        p3fs.setFont(TINY_FONT);
        
        p4bs.setVisible(false);
        p4bs.setBounds(bs.getX()+25, p4.getY(), 30, 20);
        p4bs.setForeground(Color.BLACK);
        p4bs.setFont(TINY_FONT);
        p4fk.setVisible(false);
        p4fk.setBounds(fk.getX()+25, p4.getY(), 30, 20);
        p4fk.setForeground(Color.BLACK);
        p4fk.setFont(TINY_FONT);
        p4fs.setVisible(false);
        p4fs.setBounds(fs.getX()+25, p4.getY(), 30, 20);
        p4fs.setForeground(Color.BLACK);
        p4fs.setFont(TINY_FONT);
        
        p5bs.setVisible(false);
        p5bs.setBounds(bs.getX()+25, p5.getY(), 30, 20);
        p5bs.setForeground(Color.BLACK);
        p5bs.setFont(TINY_FONT);
        p5fk.setVisible(false);
        p5fk.setBounds(fk.getX()+25, p5.getY(), 30, 20);
        p5fk.setForeground(Color.BLACK);
        p5fk.setFont(TINY_FONT);
        p5fs.setVisible(false);
        p5fs.setBounds(fs.getX()+25, p5.getY(), 30, 20);
        p5fs.setForeground(Color.BLACK);
        p5fs.setFont(TINY_FONT);
        
        p6bs.setVisible(false);
        p6bs.setBounds(bs.getX()+25, p5.getY(), 30, 20);
        p6bs.setForeground(Color.BLACK);
        p6bs.setFont(TINY_FONT);
        p6fk.setVisible(false);
        p6fk.setBounds(fk.getX()+25, p5.getY(), 30, 20);
        p6fk.setForeground(Color.BLACK);
        p6fk.setFont(TINY_FONT);
        p6fs.setVisible(false);
        p6fs.setBounds(fs.getX()+25, p5.getY(), 30, 20);
        p6fs.setForeground(Color.BLACK);
        p6fs.setFont(TINY_FONT);
        
        endGame.add(p1);
        endGame.add(p2);
        endGame.add(p3);
        endGame.add(p4);
        endGame.add(p5);
        endGame.add(p6);
        endGame.add(bs);
        endGame.add(fk);
        endGame.add(fs);
        endGame.add(ba);
        endGame.add(mo);
        endGame.add(p1bs);
        endGame.add(p1fk);
        endGame.add(p1fs);
        endGame.add(p2bs);
        endGame.add(p2fk);
        endGame.add(p2fs);
        endGame.add(p3bs);
        endGame.add(p3fk);
        endGame.add(p3fs);
        endGame.add(p4bs);
        endGame.add(p4fk);
        endGame.add(p4fs);
        endGame.add(p5bs);
        endGame.add(p5fk);
        endGame.add(p5fs);
        endGame.add(p6bs);
        endGame.add(p6fk);
        endGame.add(p6fs);
        
        graph = new GraphPanel();
        graph.setBounds(0, 0, endGame.getWidth(), 290); // width = 480
        graph.setVisible(true);
        graph.setBackground(Color.WHITE);
        JRadioButton hexHistory = new JRadioButton("Land");
        hexHistory.setActionCommand("Hex History");
        hexHistory.setBounds(graph.getHeight()-30, 20, 50, 30);
        hexHistory.setOpaque(false);
        hexHistory.setSelected(true);
        hexHistory.setFont(new Font("Square721 BT", Font.BOLD, 12));
        JRadioButton menHistory = new JRadioButton("Fighters");
        menHistory.setActionCommand("Men History");
        menHistory.setBounds(graph.getHeight()-30, 80, 50, 30);
        menHistory.setOpaque(false);
        menHistory.setSelected(false);
        menHistory.setFont(new Font("Square721 BT", Font.BOLD, 12));
        JRadioButton wageHistory = new JRadioButton("Wages");
        wageHistory.setActionCommand("Wage History");
        wageHistory.setBounds(graph.getHeight()-30, 130, 50, 30);
        wageHistory.setOpaque(false);
        wageHistory.setSelected(false);
        wageHistory.setFont(new Font("Square721 BT", Font.BOLD, 12));
        
        replay.setVisible(true);
        replay.setBounds((graph.getWidth()/2)-(130/2), graph.getHeight()-200, 80, 30);
        replay.setActionCommand("watch replay");
        replay.addActionListener(std);
        
        ButtonGroup group = new ButtonGroup();
        group.add(hexHistory);
        group.add(menHistory);
        group.add(wageHistory);
        
        hexHistory.addActionListener(std);
        menHistory.addActionListener(std);
        wageHistory.addActionListener(std);
        graph.add(hexHistory);
        graph.add(menHistory);
        graph.add(wageHistory);
        graph.add(replay);
        endGame.add(graph);
        
        menu = new JFrame();
        menu.setLayout(null);
        menu.setExtendedState(JFrame.NORMAL);
        menu.setResizable(false);
        menu.setUndecorated(true);
        //menu.setOpacity((float) .85);
        menu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        menu.setSize( (int)(250), 400);
        menu.setAlwaysOnTop(true);
        menu.setBounds((screenSize.width/2)-(250/2), screenSize.height/3, 250, 400);
        menu.setTitle("Player Options");
        
        endGameShadow = new JFrame();
        endGameShadow.setUndecorated(true);
        endGameShadow.setBounds(menu.getX()-10, menu.getY()-10, menu.getWidth()+20, menu.getHeight()+20);
        endGameShadow.setBackground(Color.BLACK);
        endGameShadow.setVisible(true);
        //endGameShadow.setOpacity((float) .6);
        //endGameShadow.setAlwaysOnTop(true);
        
        tf.setVisible(true);
        tf.setBounds((AI.getX()+80), 10, 150, 30);
        tf.setFont(INFO_SMALL_FONT);
        tf.setForeground(Color.DARK_GRAY);
        tf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                textFieldMousePressed(evt);
            }
        });
        addPlayer.setVisible(true);
        addPlayer.setBounds((menu.getWidth()/2)-(130/2), menu.getHeight()-200, 130, 30);
        addPlayer.setActionCommand("add another player");
        addPlayer.addActionListener(std);
        done.setBounds((menu.getWidth()/2)-(130/2), menu.getHeight()-40, 130, 30);
        done.setActionCommand("start game");
        done.addActionListener(std);
        done.setVisible(true);
        sliderR.setBounds(tf.getX(), 60, 150, 45);
        sliderR.setMajorTickSpacing(50);
        sliderR.setMinorTickSpacing(1);
        sliderR.setPaintTicks(true);
        sliderR.setPaintLabels(true);
        sliderR.setOpaque(false);
        sliderR.setValue(50);
        sliderR.setFont(new Font("Square721 BT", Font.PLAIN, 10));
        sliderR.setForeground(Color.DARK_GRAY);
        sliderR.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderRChangeStateChanged(event);
			}
		});
        sliderR.setVisible(true);
        sliderG.setBounds(tf.getX(), 100, 150, 45);
        sliderG.setMajorTickSpacing(50);
        sliderG.setMinorTickSpacing(1);
        sliderG.setPaintTicks(true);
        sliderG.setPaintLabels(true);
        sliderG.setOpaque(false);
        sliderG.setValue(50);
        sliderG.setFont(new Font("Square721 BT", Font.PLAIN, 10));
        sliderG.setForeground(Color.DARK_GRAY);
        sliderG.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderGChangeStateChanged(event);
			}
		});
        sliderG.setVisible(true);
        sliderB.setBounds(tf.getX(), 140, 150, 45);
        sliderB.setMajorTickSpacing(50);
        sliderB.setMinorTickSpacing(1);
        sliderB.setPaintTicks(true);
        sliderB.setPaintLabels(true);
        sliderB.setOpaque(false);
        sliderB.setValue(50);
        sliderB.setFont(new Font("Square721 BT", Font.PLAIN, 10));
        sliderB.setForeground(Color.DARK_GRAY);
        sliderB.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderBChangeStateChanged(event);
			}
		});
        sliderB.setVisible(true);
        
        bonuses.setBounds(sliderR.getX()+50, 250, 100, 45);
        bonuses.setMajorTickSpacing(5);
        bonuses.setMinorTickSpacing(1);
        bonuses.setPaintTicks(true);
        bonuses.setPaintLabels(true);
        bonuses.setOpaque(false);
        bonuses.setValue(3);
        bonuses.setForeground(Color.DARK_GRAY);
        bonuses.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				bonusesChangeStateChanged(event);
			}
		});
        bonuses.setVisible(true);
        
        JRadioButton small = new JRadioButton("Small");
        small.setActionCommand("Small");
        small.setBounds(10, menu.getHeight()-95, 70, 30);
        small.setOpaque(false);
        small.setSelected(false);
        small.setForeground(Color.DARK_GRAY);
        small.setFont(new Font("Square721 BT", Font.BOLD, 12));
        JRadioButton medium = new JRadioButton("Medium");
        medium.setActionCommand("Medium");
        medium.setBounds(85, menu.getHeight()-95, 80, 30);
        medium.setOpaque(false);
        medium.setSelected(true);
        medium.setForeground(Color.DARK_GRAY);
        medium.setFont(new Font("Square721 BT", Font.BOLD, 12));
        JRadioButton large = new JRadioButton("Large");
        large.setActionCommand("Large");
        large.setBounds(170, menu.getHeight()-95, 70, 30);
        large.setOpaque(false);
        large.setSelected(false);
        large.setForeground(Color.DARK_GRAY);
        large.setFont(new Font("Square721 BT", Font.BOLD, 12));
        
        ButtonGroup groupMapSize = new ButtonGroup();
        groupMapSize.add(small);
        groupMapSize.add(medium);
        groupMapSize.add(large);
        
        small.addActionListener(std);
        medium.addActionListener(std);
        large.addActionListener(std);
        menu.add(small);
        menu.add(medium);
        menu.add(large);
        
        AI.setBounds(15, 0, 80, 50);
        AI.addItemListener(std);
        AI.setOpaque(false);
        AI.setForeground(Color.DARK_GRAY);
        AI.setVisible(true);
        AI.setFont(INFO_SMALL_FONT);
        
        red.setVisible(true);
        red.setBounds(sliderR.getX()-40, sliderR.getY()+10, 50, 15);
        red.setForeground(Color.RED);
        green.setVisible(true);
        green.setBounds(sliderG.getX()-40, sliderG.getY()+10, 50, 15);
        green.setForeground(Color.GREEN);
        blue.setVisible(true);
        blue.setBounds(sliderB.getX()-40, sliderB.getY()+10, 50, 15);
        blue.setForeground(Color.BLUE);
        
        lblBonuses.setVisible(true);
        lblBonuses.setBounds(30, bonuses.getY()+5, 100, 15);
        lblBonuses.setForeground(Color.DARK_GRAY);
        numBonuses.setVisible(true);
        numBonuses.setBounds(60, lblBonuses.getY()+lblBonuses.getHeight(), 30, 20);
        numBonuses.setForeground(Color.DARK_GRAY);
        numBonuses.setFont(INFO_SMALL_FONT);

        Color newColor = new Color(240, 240, 240);
		menu.getContentPane().setBackground(newColor);
        menu.setVisible(true);
        
        menu.add(numBonuses);
        menu.add(lblBonuses);
        menu.add(bonuses);
        menu.add(sliderR);
        menu.add(sliderG);
        menu.add(sliderB);
        menu.add(tf);
        menu.add(addPlayer);
        menu.add(done);
        menu.add(AI);
        menu.add(red);
        menu.add(green);
        menu.add(blue);
        menu.add(mapProgress);
        
        menuBar = new JMenuBar();
		
		Bmenu = new JMenu("Menu");
		Bmenu.setMnemonic(KeyEvent.VK_A);
		Bmenu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(Bmenu);

		menuItem = new JMenuItem("New Game");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.addActionListener(std);
		menuItem.setActionCommand("New Game");
		Bmenu.add(menuItem);

		menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic(KeyEvent.VK_E);
		menuItem.addActionListener(std);
		menuItem.setActionCommand("Exit");
		Bmenu.add(menuItem);
		
		//a group of check box menu items
		Bmenu.addSeparator();
		cbMenuItem1 = new JCheckBoxMenuItem("Sound");
		cbMenuItem1.setMnemonic(KeyEvent.VK_S);
		cbMenuItem1.setActionCommand("Sound");
		cbMenuItem1.setSelected(true);
		cbMenuItem1.addItemListener(std);
		Bmenu.add(cbMenuItem1);

		cbMenuItem2 = new JCheckBoxMenuItem("Graph");
		cbMenuItem2.setMnemonic(KeyEvent.VK_G);
		cbMenuItem2.setActionCommand("Graph");
		cbMenuItem2.setSelected(false);
		cbMenuItem2.addItemListener(std);
		Bmenu.add(cbMenuItem2);
		
		//a submenu
		Bmenu.addSeparator();
		submenu = new JMenu("Speed");
		submenu.setMnemonic(KeyEvent.VK_L);

		menuItem = new JMenuItem("Slow");
		menuItem.setActionCommand("Slow");
		menuItem.addActionListener(std);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Normal");
		menuItem.setActionCommand("Normal");
		menuItem.addActionListener(std);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Fast");
		menuItem.setActionCommand("Fast");
		menuItem.addActionListener(std);
		submenu.add(menuItem);
		
		Bmenu.add(submenu);
		
		frame.setJMenuBar(menuBar);
		
        // add antialiasing
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                                  RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);

        // frame stuff
        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel draw = new JLabel(icon);

        //zoomAndPanListener = new ZoomAndPanListener(draw);
        
        //draw.addMouseListener(zoomAndPanListener);
        //draw.addMouseMotionListener(zoomAndPanListener);
        //draw.addMouseWheelListener(zoomAndPanListener);
        
        draw.addMouseListener(std);
        draw.addMouseMotionListener(std);

        frame.setContentPane(draw);
        frame.addKeyListener(std);    // JLabel cannot get keyboard focus
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            // closes all windows
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        frame.setTitle("Hex Animation");
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
        endGameShadow.setVisible(true);
    }

    private static void textFieldMousePressed(MouseEvent evt) {
        tf.setText("");
    }
    
    public static void setMenuVisibility(boolean vis){
    	menu.setVisible(vis);
    }
    
    public static void clear() { clear(DEFAULT_CLEAR_COLOR); }

    public static void clear(Color color) {
        offscreen.setColor(color);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(penColor);
        //draw();
    }

    public static void setHexSide(int side) {
		s=side;
		t =  (int) (s / 2);			//t = s sin(30) = (int) CalculateH(s);
		r =  (int) (s * 0.8660254037844);	//r = s cos(30) = (int) CalculateR(s); 
		h=2*r;
	}
    
    public static void setRegionColor(int r, int g, int b){
    	if (hexVenture.regColor + r > 255)
    		r = 255;
    	if (hexVenture.regColor + r < 0)
    		r = 0;
    	if (hexVenture.regColor + g > 255)
    		g = 255;
    	if (hexVenture.regColor + g < 0)
    		g = 0;
    	if (hexVenture.regColor + b > 255)
    		b = 255;
    	if (hexVenture.regColor + b < 0)
    		b = 0;
    	REGION = new Color(r, g, b);
    }
    
    public static Color getRegionColor(){
    	return REGION;
    }
    
	public static void setHexHeight(int height) {
		h = height;			// (distance between two hex centers)
		r = h/2;			// r = radius of inscribed circle
		s = (int) (h / 1.73205);	// s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h / sqrt(3)
		t = (int) (r / 1.73205);	// t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2 sqrt(3)) = r / sqrt(3)
	}
	
	public static void showStats(){
		endGameShadow.setVisible(true);
		endGame.setVisible(true);
		for (int p = 0; p <= hexVenture.numPlayers; p++){
			if (p == 0){
				p1bs.setText(""+hexVenture.player[p].getBasesSacked());
				p1fk.setText(""+hexVenture.player[p].getFightersKilled());
				p1fs.setText(""+hexVenture.player[p].getFightersStarved());
			}
			if (p == 1){
				p2bs.setText(""+hexVenture.player[p].getBasesSacked());
				p2fk.setText(""+hexVenture.player[p].getFightersKilled());
				p2fs.setText(""+hexVenture.player[p].getFightersStarved());
			}
			if (p == 2){
				p3bs.setText(""+hexVenture.player[p].getBasesSacked());
				p3fk.setText(""+hexVenture.player[p].getFightersKilled());
				p3fs.setText(""+hexVenture.player[p].getFightersStarved());
			}
			if (p == 3){
				p4bs.setText(""+hexVenture.player[p].getBasesSacked());
				p4fk.setText(""+hexVenture.player[p].getFightersKilled());
				p4fs.setText(""+hexVenture.player[p].getFightersStarved());
			}
			if (p == 4){
				p5bs.setText(""+hexVenture.player[p].getBasesSacked());
				p5fk.setText(""+hexVenture.player[p].getFightersKilled());
				p5fs.setText(""+hexVenture.player[p].getFightersStarved());
			}
			if (p == 5){
				p6bs.setText(""+hexVenture.player[p].getBasesSacked());
				p6fk.setText(""+hexVenture.player[p].getFightersKilled());
				p6fs.setText(""+hexVenture.player[p].getFightersStarved());
			}
		}
		graph.repaint();
	}
	
	public static void hideStats(){
		endGameShadow.setVisible(false);
		endGame.setVisible(false);
	}
	
	public static void drawGameOver(){
		setGameOverFont();
		setPenColor(Color.WHITE);
		
		if (hexVenture.gameStatus==GameStatus.PLAYER1WON){
			text(41, 49, hexVenture.player[0].getName()+" WON!");
			setPenColor(hexVenture.player[0].getColorShade());
			text(40.5, 49.5, hexVenture.player[0].getName()+" WON!");
		}
		else if (hexVenture.gameStatus==GameStatus.PLAYER2WON){
			text(41, 49, hexVenture.player[1].getName()+" WON!");
			setPenColor(hexVenture.player[1].getColorShade());
			text(40.5, 49.5, hexVenture.player[1].getName()+" WON!");
		}
		else if (hexVenture.gameStatus==GameStatus.PLAYER3WON){
			text(41, 49, hexVenture.player[2].getName()+" WON!");
			setPenColor(hexVenture.player[2].getColorShade());
			text(40.5, 49.5, hexVenture.player[2].getName()+" WON!");
		}
		else if (hexVenture.gameStatus==GameStatus.PLAYER4WON){
			text(41, 49, hexVenture.player[3].getName()+" WON!");
			setPenColor(hexVenture.player[3].getColorShade());
			text(40.5, 49.5, hexVenture.player[3].getName()+" WON!");
		}
		else if (hexVenture.gameStatus==GameStatus.PLAYER5WON){
			text(41, 49, hexVenture.player[4].getName()+" WON!");
			setPenColor(hexVenture.player[4].getColorShade());
			text(40.5, 49.5, hexVenture.player[4].getName()+" WON!");
		}
		else if (hexVenture.gameStatus==GameStatus.PLAYER6WON){
			text(41, 49, hexVenture.player[5].getName()+" WON!");
			setPenColor(hexVenture.player[5].getColorShade());
			text(40.5, 49.5, hexVenture.player[5].getName()+" WON!");
		}
		
		setInfoSmallFont();
	}
	
	public static void drawHighs(int pl, int sack, int link, int kill, int starve){
		//setInfoSmallFont();
		//setPenColor(Color.WHITE);
		
		if (hexVenture.player[pl].getBasesSacked() == sack){
			//text(41, 40, "Conqueror:  "+ hexVenture.player[pl].getName());
			//setPenColor(hexVenture.player[pl].getColorShade());
			//text(41, 40, "Conqueror: "+ hexVenture.player[pl].getName());
		}
		if (hexVenture.player[pl].getBasesLinked() == link){
			//setPenColor(Color.WHITE);
			//text(41, 35, "Uniter: "+ hexVenture.player[pl].getName());
			//setPenColor(hexVenture.player[pl].getColorShade());
			//text(41, 35, "Uniter: "+ hexVenture.player[pl].getName());
		}
		if (hexVenture.player[pl].getFightersKilled() == kill){
			//setPenColor(Color.WHITE);
			//text(41, 30, "Killer: "+ hexVenture.player[pl].getName());
			//setPenColor(hexVenture.player[pl].getColorShade());
			//text(41, 30, "Killer: "+ hexVenture.player[pl].getName());
		}
		if (hexVenture.player[pl].getFightersStarved() == starve){
			//setPenColor(Color.WHITE);
			//text(41, 25, "Famine: "+ hexVenture.player[pl].getName());
			//setPenColor(hexVenture.player[pl].getColorShade());
			//text(41, 25, "Famine: "+ hexVenture.player[pl].getName());
		}
	}
	
	public static void drawSideInfo(double x, double y){
		double xs = scaleX(x);
        double ys = scaleY(y);
        
        //StdDraw.picture(89, 57, "sideinforaw.png", 20, 10);
        
		int yadjust=(-60), xAdjust=28;

		setPenRadius(.005);
		offscreen.drawRoundRect((int)xs+2, (int)ys+yadjust, 375, 110, 20, 20);
		drawFighterSelect(xs+150, ys+yadjust/2, 1, 1);
		drawFighterSelect(xs+210, ys+yadjust/2, 1, 2);
		drawFighterSelect(xs+270, ys+yadjust/2, 1, 3);
		drawFighterSelect(xs+330, ys+yadjust/2, 1, 4);
		offscreen.setColor(Color.DARK_GRAY);
		offscreen.drawString("rank", (int)xs+24, (int)ys+yadjust/3);
		
		yadjust=(+25);
		offscreen.drawString(""+hexVenture.FIGHTER_RANK_ONE_EXPENSE, (int)xs+145,(int)ys+yadjust);
		offscreen.drawString(""+hexVenture.FIGHTER_RANK_TWO_EXPENSE, (int)xs+205,(int)ys+yadjust);
		offscreen.drawString(""+hexVenture.FIGHTER_RANK_THREE_EXPENSE, (int)xs+260,(int)ys+yadjust);
		offscreen.drawString(""+hexVenture.FIGHTER_RANK_FOUR_EXPENSE, (int)xs+320,(int)ys+yadjust);
		offscreen.drawString("wage", (int)xs+24, (int)ys+yadjust);
		
		int counter=-1;
		double hexLinkCount = 0;
		double playerPerc;
		
		for (int p = 0; p <= hexVenture.numPlayers; p++)
			hexLinkCount += hexVenture.player[p].getHexesLinked();
		
		int h = (int)100*hexVenture.numPlayers;
		int prevY1 = (int)ys+100;
		
		// draw player hex count table
		
		setTinyFont();
		do{ counter++; 
		if (counter == hexVenture.playerTurn)
			offscreen.setColor(Color.DARK_GRAY);
		else
			offscreen.setColor(hexVenture.player[counter].getColorActual());
			offscreen.drawString(hexVenture.player[counter].getName(), (int)xs+15, (int)(ys+130+(counter*80))); // +" "+hexVenture.player[counter].getHexesLinked()
		
			playerPerc = hexVenture.player[counter].getHexesLinked() / hexLinkCount;

			offscreen.setColor(hexVenture.player[counter].getColorActual());
			offscreen.fillRect((int)xs+170, prevY1, 200, (int)(playerPerc * h));
			
			setPenRadius(.005);
			offscreen.drawLine((int)xs+105, (int)(ys+125+(counter*80)), (int)xs+170, prevY1+((int)(playerPerc * h)/2));
			
			prevY1 = prevY1 + (int)(playerPerc * h);
		}while (counter<hexVenture.numPlayers);
		
		setPenRadius(.01);
		offscreen.setColor(Color.DARK_GRAY);
		offscreen.drawRoundRect((int)xs+170, (int)ys+100, 200, h, 10, 10);
	}
	
    public static void line(int i, int j, int num) {
    	double x = hexVenture.hexes[num].getX();
    	double y = hexVenture.hexes[num].getY();
        
        offscreen.setColor(Color.WHITE);
		setPenRadius(.005);

		if (hexVenture.hexes[num].line[0] == hexVenture.selectedBase)
			offscreen.draw(new Line2D.Double(x+t, y, x+s+t, y));
		if (hexVenture.hexes[num].line[1] == hexVenture.selectedBase)
			offscreen.draw(new Line2D.Double(x+t, y+r+r, x+s+t, y+r+r));
		if (hexVenture.hexes[num].line[2] == hexVenture.selectedBase)
			offscreen.draw(new Line2D.Double(x+t+s, y, x+s+t+t, y+r));
		if (hexVenture.hexes[num].line[3] == hexVenture.selectedBase)
			offscreen.draw(new Line2D.Double(x+s+t+t, y+r, x+t+s, y+r+r));
		if (hexVenture.hexes[num].line[4] == hexVenture.selectedBase)
			offscreen.draw(new Line2D.Double(x+t, y+r+r, x, y+r));
		if (hexVenture.hexes[num].line[5] == hexVenture.selectedBase)
			offscreen.draw(new Line2D.Double(x, y+r, x+t, y));
		
		setPenRadius(.002);
		
		//draw();
    }

    public static void drawLine(int xx1, int yy1, int xx2, int yy2){
    	double x1 = scaleX(xx1);
        double y1 = scaleY(yy1);
        double x2 = scaleX(xx2);
        double y2 = scaleY(yy2);
    	setPenRadius(.005);
    	offscreen.draw(new Line2D.Double(x1, y1, x2, y2));
    }
    
    public static void regLine(int i, int j, int num, int reg, int p) {
    	double x = hexVenture.hexes[num].getX();
    	double y = hexVenture.hexes[num].getY();
        
		offscreen.setColor(Color.LIGHT_GRAY);
		setDashPenRadius(.01);
		if ((hexVenture.bonus[reg-1].isControlled()) && (hexVenture.hexes[num].isVisible())){
			offscreen.setColor(getRegionColor());
			setDashPenRadius(hexVenture.bonus[reg-1].getThickness());
		}

		if (hexVenture.hexes[num].regLine[0] ==  reg)
			offscreen.draw(new Line2D.Double(x+t, y, x+s+t, y));
		if (hexVenture.hexes[num].regLine[1] ==  reg)
			offscreen.draw(new Line2D.Double(x+t, y+r+r, x+s+t, y+r+r));
		if (hexVenture.hexes[num].regLine[2] ==  reg)
			offscreen.draw(new Line2D.Double(x+t+s, y, x+s+t+t, y+r));
		if (hexVenture.hexes[num].regLine[3] ==  reg)
			offscreen.draw(new Line2D.Double(x+s+t+t, y+r, x+t+s, y+r+r));
		if (hexVenture.hexes[num].regLine[4] ==  reg)
			offscreen.draw(new Line2D.Double(x+t, y+r+r, x, y+r));
		if (hexVenture.hexes[num].regLine[5] ==  reg)
			offscreen.draw(new Line2D.Double(x, y+r, x+t, y));
		
		setPenRadius(.002);
		
		//draw();
    }

    private static void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    public static void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double r = penRadius;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);

        // double ws = factorX(2*r);
        // double hs = factorY(2*r);
        // if (ws <= 1 && hs <= 1) pixel(x, y);
        if (scaledPenRadius <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - scaledPenRadius/2, ys - scaledPenRadius/2,
                                                 scaledPenRadius, scaledPenRadius));
        draw();
    }

    public static void circle(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        //draw();
    }

    public static void filledCircle(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        //draw();
    }

    public static void arc(double x, double y, double r, double angle1, double angle2) {
        if (r < 0) throw new IllegalArgumentException("arc radius must be nonnegative");
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
        draw();
    }

    public static void rectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width must be nonnegative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.drawRoundRect((int)(xs - ws/2), (int)(ys - hs/2), (int)ws, (int)hs, 20, 20); //offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        //draw();
    }

    public static void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth  < 0) throw new IllegalArgumentException("half width must be nonnegative");
        if (halfHeight < 0) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fillRoundRect((int)(xs - ws/2), (int)(ys - hs/2), (int)ws, (int)hs, 20, 20); //new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs)
        //draw();
    }
    
    public static int getHexX(int i, int j, int num){
    	int x = i * (s+t);
    	return (int) ((int)x*1.15)+hexVenture.HEXSIZE/2;
    }
    
    public static int getHexY(int i, int j, int num){
		int y = j * h + (i%2) * h/2;
		return (int) ((int)y*1.15)+hexVenture.HEXSIZE/2;
    }
    
    public static Polygon hex (int x0, int y0) {
    	 
		int y = y0;
		int x = x0;
				
		int[] cx,cy;

		cx = new int[] {x+t,x+s+t,x+s+t+t,x+s+t,x+t,x};
 
		cy = new int[] {y,y,y+r,y+r+r,y+r+r,y+r};
		return new Polygon(cx,cy,6);
	}
	
    public static void setHexOriginXY(int i, int j, int num){
    	hexVenture.hexes[num].setOox(i * (s+t));
    	hexVenture.hexes[num].setOoy(j * h + (i%2) * h/2);
    	hexVenture.hexes[num].setOx(i * (s+t));
    	hexVenture.hexes[num].setOy(j * h + (i%2) * h/2);
    	hexVenture.hexes[num].setOox(hexVenture.hexes[num].getOox()*1.2);
    	hexVenture.hexes[num].setOoy(hexVenture.hexes[num].getOoy()*1.2);
    	hexVenture.hexes[num].setOx(hexVenture.hexes[num].getOx()*1.2);
    	hexVenture.hexes[num].setOy(hexVenture.hexes[num].getOy()*1.2);
    }
    
    public static void drawHexShadow(int num){
    	double x = hexVenture.hexes[num].getOox();
    	double y = hexVenture.hexes[num].getOoy();
    	float tran = 0.3f;
    	
    	offscreen.setColor(Color.DARK_GRAY);
    	
    	Composite originalComposite = offscreen.getComposite();
    	Path2D.Double path = getHexPath(x, y, r);
        offscreen.setComposite(makeComposite(tran));
        offscreen.fill(path);
    	offscreen.setComposite(originalComposite);
    }
    
	public static void drawHex(int num) {
		if (((hexVenture.hexes[num].occupation.getPlayer() == hexVenture.playerTurn)) && ((hexVenture.hexes[num].occupation.getBase() > -1)))
			drawHexShadow(num);
		Path2D.Double path = getHexPath(hexVenture.hexes[num].getX(), hexVenture.hexes[num].getY(), r);
		offscreen.setColor(hexVenture.hexes[num].getColor());
		offscreen.fill(path);
		if (hexVenture.hexes[num].occupation.getPlayer() > -1)
			offscreen.setColor(hexVenture.player[hexVenture.hexes[num].occupation.getPlayer()].getColorShade());
		else
			offscreen.setColor(VISIBLEFILL);
		if (hexVenture.gameStatus != GameStatus.GAME_ON)
			offscreen.setColor(VISIBLEFILL);
		setPenRadius(.01);
		offscreen.draw(path);
		setPenRadius(.002);
		
		if ((hexVenture.hexes[num].isHighlighted) && (!hexVenture.player[hexVenture.playerTurn].isAI())){
			if (((hexVenture.hexes[num].occupation.getPlayer() == hexVenture.playerTurn) && (hexVenture.hexes[num].occupation.getBase() != hexVenture.selectedBase)) || (hexVenture.hexes[num].occupation.getPlayer() != hexVenture.playerTurn)){
				setPenRadius(.014);
				offscreen.setColor(BOOK_BLUE);
				offscreen.draw(path);
				setPenRadius(.002);
			}
		}
		
		//if (hexVenture.hexHighlight == num){
			//setPenRadius(.014);
			//offscreen.setColor(Color.WHITE);
			//offscreen.draw(path);
			//setPenRadius(.002);
			//return;
		//}
		
		//if (hexVenture.hexes[num].getThreatRank()==0){
			//offscreen.setColor(Color.BLUE);
			//offscreen.drawPolygon(poly);
		//}
		//if (hexVenture.hexes[num].getThreatRank()==1){
			//offscreen.setColor(Color.GREEN);
			//offscreen.drawPolygon(poly);
		//}
		//if (hexVenture.hexes[num].getThreatRank()==2){
			//offscreen.setColor(Color.YELLOW);
			//offscreen.drawPolygon(poly);
		//}
		//if (hexVenture.hexes[num].getThreatRank()==3){
			//offscreen.setColor(Color.ORANGE);
			//offscreen.drawPolygon(poly);
		//}
		//if (hexVenture.hexes[num].isFighterCover()){
			//offscreen.setColor(Color.BLUE);
			//offscreen.drawPolygon(poly);
		//}
		//hexes[i].setSumRank(ai.testHexes[i].getSumRank());

		//setTinyFont();
		//offscreen.setColor(Color.GREEN);
		//offscreen.drawString(""+hexVenture.hexes[num].occupation.getBase(),(int)hexVenture.hexes[num].getX()+8,(int)hexVenture.hexes[num].getY()+16);
		//offscreen.setColor(Color.RED);
		//offscreen.drawString(""+hexVenture.hexes[num].getThreatRank(),(int)hexVenture.hexes[num].getX()+8,(int)hexVenture.hexes[num].getY()+26);
		//offscreen.setColor(Color.WHITE);
		//offscreen.drawString(""+hexVenture.hexes[num].isFighterCover(),(int)hexVenture.hexes[num].getX()+8,(int)hexVenture.hexes[num].getY()+35);
		//offscreen.setColor(Color.WHITE);
		//offscreen.drawString(""+num,(int)hexVenture.hexes[num].getX()+8,(int)hexVenture.hexes[num].getY()+43);
		//offscreen.drawString(""+hexVenture.hexes[num].occupation.getFighter(),(int)hexVenture.hexes[num].getX()+6,(int)hexVenture.hexes[num].getY()+22);
		//offscreen.drawString(""+(num),(int)hexVenture.hexes[num].getX()+6,(int)hexVenture.hexes[num].getY()+22);
		//offscreen.drawString(""+(num),x+10,y+28);
		//offscreen.drawString(""+(hexVenture.hexes[num].getSetR()+" "+hexVenture.hexes[num].getSetG()+" "+hexVenture.hexes[num].getSetB()),x+4,y+24);
	}
	
	public static void drawOldHex(int num) {
		Path2D.Double path = getHexPath(hexVenture.hexes[num].getX(), hexVenture.hexes[num].getY(), r);
		
		//if (hexVenture.hexes[num].isLinked())
		//offscreen.setColor(hexVenture.hexes[num].getColor());
		//else
		if ((hexVenture.hexes[num].oldOccupation[hexVenture.playerTurn].getPlayer() > -1) && (!hexVenture.hexes[num].isBlank())){
			//offscreen.setColor(hexVenture.player[hexVenture.hexes[num].oldOccupation[hexVenture.playerTurn].getPlayer()].getColorShade());
			offscreen.setColor(hexVenture.player[hexVenture.hexes[num].occupation.getPlayer()].getColorShade());
			offscreen.fill(path);
			setPenRadius(.02);
			offscreen.setColor(Color.BLACK);
			offscreen.draw(path);
		}else{
			setPenRadius(.02);
			offscreen.setColor(Color.BLACK);
			offscreen.fill(path);
			offscreen.draw(path);
		}
		
		offscreen.setColor(Color.BLACK);
		setPenRadius(.002);
	}
	
	public static void drawSelectHex(int num) {
		Path2D.Double path = getHexPath(hexVenture.hexes[num].getX(), hexVenture.hexes[num].getY(), r);
		offscreen.setColor(Color.WHITE);
		setPenRadius(.01);
		offscreen.draw(path);
	}
	
	public static Polygon Outpost (int x0, int y0, int rank) {
		 
		int y = y0;
		int x = x0;
				
		int[] cx = null,cy = null;
		
		cx = new int[] {x+1*2,x+1*2,x+3*2,x+3*2,x+3*2,x+3*2,x-3*2,x-3*2,x-3*2,x-3*2,x-1*2,x-1*2,x+1*2};
		cy = new int[] {y,y-1*2,y-1*2,y,y,y+8*2,y+8*2,y,y,y-1*2,y-1*2,y,y};
		
		return new Polygon(cx,cy,13);
	}
	
	public static Path2D.Double getHexPath(double x, double y, double r){
		Path2D.Double path = new Path2D.Double();
		
		double xs = x;//scaleX(x);
		double ys = y;//scaleY(y);
		
		double[] cx = null,cy = null;

		cx = new double[] {xs+t,xs+s+t,xs+s+t+t,xs+s+t,xs+t,xs};
		cy = new double[] {ys,ys,ys+r,ys+r+r,ys+r+r,ys+r};

		path.moveTo(cx[0], cy[0]);
		for(int ii = 1; ii < cx.length; ++ii) {
		   path.lineTo(cx[ii], cy[ii]);
		}
		path.closePath();
		//AffineTransform at = new AffineTransform();
		//at.scale(2, 2);
		//path.transform(at);
		return path;
	}
	
	public static Path2D.Double getFighterPath(double x, double y, double r, int rank){
		Path2D.Double path = new Path2D.Double();
		
		double[] cx = null,cy = null;
		
		x += +1;
		y += -1;
		
			cx = new double[] {x,x+(3*r),x+(6*r),x+(3*r),x+(4*r),x+(11*r),x+(11*r),x+(5*r),x+(2*r),x+(2*r),x-(2*r),x-(2*r),x-(5*r),x-(11*r),x-(11*r),x-(4*r),x-(3*r),x-(6*r),x-(3*r),x,x,x,x,x};
			cy = new double[] {y+(2*r),y+(15*r),y+(15*r),y+(2*r),y-(6*r),y-(1*r),y-(3*r),y-(10*r),y-(10*r),y-(14*r),y-(14*r),y-(10*r),y-(10*r),y-(3*r),y-(1*r),y-(6*r),y+(2*r),y+(15*r),y+(15*r),y+(2*r),y+(2*r),y+(2*r),y+(2*r),y+(2*r)};
		
		if (rank==2){
			cx = new double[] {x,x+(3*r),x+(6*r),x+(3*r),x+(4*r),x+(11*r),x+(11*r),x+(13*r),x+(13*r),x+(11*r),x+(11*r),x+(5*r),x+(2*r),x+(2*r),x-(2*r),x-(2*r),x-(5*r),x-(11*r),x-(11*r),x-(4*r),x-(3*r),x-(6*r),x-(3*r),x};
			cy = new double[] {y+(2*r),y+(15*r),y+(15*r),y+(2*r),y-(6*r),y-(1*r),y+(15*r),y+(15*r),y-(12*r),y-(12*r),y-(3*r),y-(10*r),y-(10*r),y-(14*r),y-(14*r),y-(10*r),y-(10*r),y-(3*r),y-(1*r),y-(6*r),y+(2*r),y+(15*r),y+(15*r),y+(2*r)};
		}
		if (rank==3){
			cx = new double[] {x,x+(3*r),x+(6*r),x+(3*r),x+(4*r),x+(11*r),x+(11*r),x+(13*r),x+(13*r),x+(11*r),x+(11*r),x+(5*r),x+(2*r),x+(2*r),x-(2*r),x-(2*r),x-(11*r),x-(12*r),x-(8*r),x-(4*r),x-(3*r),x-(6*r),x-(3*r),x};
			cy = new double[] {y+(2*r),y+(15*r),y+(15*r),y+(2*r),y-(6*r),y-(1*r),y+(15*r),y+(15*r),y-(12*r),y-(12*r),y-(3*r),y-(10*r),y-(10*r),y-(14*r),y-(14*r),y-(10*r),y-(10*r),y-(6*r),y+(1*r),y-(6*r),y+(2*r),y+(15*r),y+(15*r),y+(2*r)};
		}
		if (rank==4){
			cx = new double[] {x,x+(3*r),x+(6*r),x+(3*r),x+(4*r),x+(11*r),x+(11*r),x+(13*r),x+(13*r),x+(11*r),x+(11*r),x+(5*r),x+(2*r),x+(5*r),x+(2*r),x-(3*r),x-(3*r),x-(11*r),x-(12*r),x-(8*r),x-(4*r),x-(3*r),x-(6*r),x-(3*r)};
			cy = new double[] {y+(2*r),y+(15*r),y+(15*r),y+(2*r),y-(6*r),y-(1*r),y+(15*r),y+(15*r),y-(12*r),y-(12*r),y-(3*r),y-(10*r),y-(10*r),y-(13*r),y-(16*r),y-(16*r),y-(10*r),y-(10*r),y-(6*r),y+(1*r),y-(6*r),y+(2*r),y+(15*r),y+(15*r)};
		}
		
		path.moveTo(cx[0], cy[0]);
		for(int ii = 1; ii < cx.length; ++ii) {
		   path.lineTo(cx[ii], cy[ii]);
		}
		path.closePath();
		
		return path;
	}
	
	public static Path2D.Double getBasePath(double x, double y, double r, int rank){
		Path2D.Double path = new Path2D.Double();
		
		double[] cx = null,cy = null;

		x += 1;
		y += -1;
		
		cx = new double[] {x+1*r,x+1*r,x+3*r,x+3*r,x+7*r,x+7*r,x-7*r,x-7*r,x-3*r,x-3*r,x-1*r,x-1*r,x+1*r};
		cy = new double[] {y,y-1*r,y-1*r,y,y,y+4*r,y+4*r,y,y,y-1*r,y-1*r,y,y};
		
		if (rank==2){
			cx = new double[] {x+3*r,x+7*r,x+7*r,x-7*r,x-7*r,x-3*r,x-3*r,x-2*r,x+2*r,x+3*r,x+3*r,x+3*r,x+3*r};
			cy = new double[] {y,y,y+4*r,y+4*r,y,y,y-1*r,y-3*r,y-3*r,y-1*r,y,y,y};
		}
		if (rank==3){
			cx = new double[] {x+3*r,x+3*r,x+1*r,x+1*r,x+3*r,x-1*r,x-1*r,x-3*r,x-3*r,x-7*r,x-7*r,x+7*r,x+7*r};
			cy = new double[] {y,y-3*r,y-4*r,y-5*r,y-6*r,y-7*r,y-4*r,y-3*r,y,y,y+4*r,y+4*r,y};
		}

		path.moveTo(cx[0], cy[0]);
		for(int ii = 1; ii < cx.length; ++ii) {
		   path.lineTo(cx[ii], cy[ii]);
		}
		path.closePath();
		
		return path;
	}
	
	public static Path2D.Double getDeathPath (double x, double y, double i) {
		Path2D.Double path = new Path2D.Double();
		
		double[] cx = null,cy = null;

		x += 8;
		y += 7;
		
		cx = new double[] {x-4*i,x-4*i,x-1*i,x-1*i,x+1*i,x+1*i,x+4*i,x+4*i,x+1*i,x+1*i,x-1*i,x-1*i};
		cy = new double[] {y-2*i,y,y,y+5*i,y+5*i,y,y,y-2*i,y-2*i,y-5*i,y-5*i,y-2*i};
		
		path.moveTo(cx[0], cy[0]);
		for(int ii = 1; ii < cx.length; ++ii) {
		   path.lineTo(cx[ii], cy[ii]);
		}
		path.closePath();
		
		return path;
	}
 
	public static void drawOutpost(int i, int j, int rank, int p, int b) {
		int x = (i * (s+t))+20;
		int y = (j * h + (i%2) * h/2)+10;
		Polygon poly = Outpost(x,y,3);
		offscreen.setColor(hexVenture.player[p].colorShade);
		offscreen.fillPolygon(poly);
		offscreen.setColor(CANNOT_MOVE);
		offscreen.drawPolygon(poly);
	}
	
	public static void drawDeath(double x, double y, int p) {
		Path2D.Double path = getDeathPath(x, y, 3.0);

		offscreen.setColor(hexVenture.player[p].colorShade);
		offscreen.fill(path);
		offscreen.setColor(CAN_MOVE);
		offscreen.draw(path);
	}
	
	public static void drawBase(double x, double y, double r, int rank, int p, int b){
		Path2D.Double path = getBasePath(x, y, r, rank);
		
		offscreen.setColor(hexVenture.player[p].colorShade);
		offscreen.fill(path);
		
		setPenRadius(.003);
		
		if (hexVenture.resume){
			if (b >= hexVenture.player[p].base.size())
				return;
			if (hexVenture.player[p].base.get(b).getMoney()>=(hexVenture.FIGHTER_RANK_ONE_EXPENSE*5))
				offscreen.setColor(CAN_MOVE);
			else
				offscreen.setColor(CANNOT_MOVE);
		}
		
		offscreen.draw(path);
	}
	
	public static void drawFighter(double x, double y, double r, int rank, int p, int b, int f){
		Path2D.Double path = getFighterPath(x, y, r, rank);

		offscreen.setColor(hexVenture.player[p].colorShade);
		offscreen.fill(path);
		
		setPenRadius(.003);
		
		if (hexVenture.resume){
			if (b >= hexVenture.player[p].base.size())
				return;
			if (f >= hexVenture.player[p].base.get(b).fighter.size())
				return;
			if (!hexVenture.player[p].base.get(b).fighter.get(f).isMoved())
				offscreen.setColor(CAN_MOVE);
			else
				offscreen.setColor(CANNOT_MOVE);
		
			if ((p == hexVenture.playerTurn) && (b == hexVenture.selectedBase) && (f == hexVenture.selectedFighter)){
				Color newColor = new Color(hexVenture.selectedFighterHighlight, hexVenture.selectedFighterHighlight, hexVenture.selectedFighterHighlight);
				offscreen.setColor(newColor);
			}
		}
		
		offscreen.draw(path);
		//offscreen.drawString(""+hexVenture.player[p].base.get(b).fighter.get(f).isMoved(),(int)x+10,(int)y+15);
	}
	
	public static void drawFighterShadow(double x, double y, double r, int rank, int p, int b, int f){
    	float tran = 0.3f;
    	
    	offscreen.setColor(Color.DARK_GRAY);
    	
    	Composite originalComposite = offscreen.getComposite();
    	Path2D.Double path = getFighterPath(x+4, y+3, r+.3, rank);
        offscreen.setComposite(makeComposite(tran));
        offscreen.fill(path);
    	offscreen.setComposite(originalComposite);
		
	}
	
	private static AlphaComposite makeComposite(float alpha) {
    	int type = AlphaComposite.SRC_OVER;
    	return(AlphaComposite.getInstance(type, alpha));
    }
	
	public static void filledIcon(double x, double y, double r, float trans, Color color) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = r; //factorX(2*r);
        double hs = r; //factorY(2*r);
        
        if (ws <= 1 && hs <= 1){ pixel(x, y); return; }
        offscreen.setColor(color);
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    	offscreen.setComposite(originalComposite);
    }
	
	public static void filledCircle(double x, double y, double r, float trans, Color color) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        //double xs = scaleX(x);
        //double ys = scaleY(y);
        double ws = r; //factorX(2*r);
        double hs = r; //factorY(2*r);
        
        if (ws <= 1 && hs <= 1){ pixel(x, y); return; }
        offscreen.setColor(color);
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.fill(new Ellipse2D.Double(x - ws/2, y - hs/2, ws, hs));
    	offscreen.setComposite(originalComposite);
    }
	
	public static void drawSelect(int p, double x, double y, double r, float tran, float penR, String type, int rank){
    	setPenRadius(penR);
    	setPenColor(Color.WHITE);
    	
    	Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite(tran));
    	
    	if (type.equalsIgnoreCase("particle"))
    		offscreen.draw(new Ellipse2D.Double(x - r/2, y - r/2, r, r));
    	else if (type.equalsIgnoreCase("fighter"))
    		drawFighterSelect(x, y, r, rank);
    	else if (type.equalsIgnoreCase("base"))
    		drawBaseSelect(x, y, r, rank);
    	
    	offscreen.setComposite(originalComposite);
    }
	
	public static void drawFighterSelect(double x, double y, double r, int rank){
		Path2D.Double path = getFighterPath(x, y, r, rank);
		setPenColor(Color.WHITE);
		setPenRadius(.003);
		offscreen.draw(path);
	}
	
	public static void drawBaseSelect(double x, double y, double r, int rank){
		Path2D.Double path = getBasePath(x, y, r, rank);
		offscreen.draw(path);
	}
	
	public static void drawBloom(double x, double y, double r, float tran, int player){
    	//double xs = scaleX(x);
        //double ys = scaleY(y);
        double ws = r; //factorX(2*r);
        double hs = r; //factorY(2*r);
        
        Paint p;
        
        p = new RadialGradientPaint(new Point2D.Double(x,
                y), (float) (hs / 2.0f),
                new Point2D.Double(x, y),
                new float[] { 0.0f, tran },
                new Color[] { new Color(255, 255, 255, 175),
                    new Color(255, 255, 255, 0) },
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB,
                AffineTransform.getScaleInstance(1.0, 1.0));
        offscreen.setPaint(p);
        offscreen.fill(new Ellipse2D.Double(x - ws/2, (float) (y-(hs*0.5)), ws-1, hs-1));
    }
	
	public static Point pxtoHex(double mx, double my) {
		Point p = new Point(-1,-1);

		mx = mx + 4;
		my = my + 4;
		int x = (int) (mx / (s+t));
		int y = (int) ((my - (x%2)*r)/h);
 
		double dx = mx - x*(s+t);
		double dy = my - y*h;
		
		//even columns
		if (x%2==0) {
			if (dy > r) {	//bottom half of hexes
				if (dx * r /t < dy - r) {
					x--;
				}
			}
			if (dy < r) {	//top half of hexes
				if ((t - dx)*r/t > dy ) {
					x--;
					y--;
				}
			}
		} else {  // odd columns
			if (dy > h) {	//bottom half of hexes
				if (dx * r/t < dy - h) {
					x--;
					y++;
				}
			}
			if (dy < h) {	//top half of hexes
				if ((t - dx)*r/t > dy - r) {
					x--;
				}
			}
		}
		p.x=x;
		p.y=y;
		
		return p;
	}
	
	 // get an image from the given filename
    private static Image getImage(String filename) {

        // to read from file
        ImageIcon icon = new ImageIcon(filename);

        // try to read from URL
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            try {
                URL url = new URL(filename);
                icon = new ImageIcon(url);
            } catch (Exception e) { /* not a url */ }
        }

        // in case file is inside a .jar
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = StdDraw.class.getResource(filename);
            if (url == null) throw new IllegalArgumentException("image " + filename + " not found");
            icon = new ImageIcon(url);
        }

        return icon.getImage();
    }
    
	/**
     * Draw picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
     * @param x the center x coordinate of the image
     * @param y the center y coordinate of the image
     * @param s the name of the image/picture, e.g., "ball.gif"
     * @param w the width of the image
     * @param h the height of the image
     * @throws IllegalArgumentException if the width height are negative
     * @throws IllegalArgumentException if the image is corrupt
     */
    public static void picture(double x, double y, String s, double w, double h) {
        Image image = getImage(s);
        double xs = scaleX(x);
        double ys = scaleY(y);
        if (w < 0) throw new IllegalArgumentException("width is negative: " + w);
        if (h < 0) throw new IllegalArgumentException("height is negative: " + h);
        double ws = factorX(w);
        double hs = factorY(h);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + s + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                       (int) Math.round(ys - hs/2.0),
                                       (int) Math.round(ws),
                                       (int) Math.round(hs), null);
        }
    }
    
    public static void text(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs - ws/2.0), (float) (ys + hs));
        //draw();
    }

    public static void text(double x, double y, String s, double degrees, float trans) {
    	setHeadingFont();
        //offscreen.rotate(Math.toRadians(-degrees), x, y);
        Composite originalComposite = offscreen.getComposite();
    	offscreen.setComposite(makeComposite((float) trans));
    	offscreen.drawString(s, (int)x, (int)y);
    	offscreen.setComposite(originalComposite);
        //offscreen.rotate(Math.toRadians(+degrees), x, y);
    }

    public static void textLeft(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs), (float) (ys + hs));
        //draw();
    }

    public static void textRight(double x, double y, String s) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(s);
        int hs = metrics.getDescent();
        offscreen.drawString(s, (float) (xs - ws), (float) (ys + hs));
        //draw();
    }

    public static void show(int t) {
        defer = false;
        draw();
        try { Thread.sleep(t); }
        catch (InterruptedException e) { System.out.println("Error sleeping"); }
        defer = true;
    }

    public static void show() {
        defer = false;
        draw();
    }

    // draw onscreen if defer is false
    private static void draw() {
        if (defer) return;
        //offscreen.setTransform(zoomAndPanListener.getCoordTransform());
        onscreen.drawImage(offscreenImage, 0, 20, null); // 15 // 37
        frame.repaint();
    }

    public static double getPenRadius() { return penRadius; }

    public static void setPenRadius() { setPenRadius(DEFAULT_PEN_RADIUS); }

    public static void setPenRadius(double r) {
        if (r < 0) throw new IllegalArgumentException("pen radius must be nonnegative");
        penRadius = r;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);
        BasicStroke stroke = new BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // BasicStroke stroke = new BasicStroke(scaledPenRadius);
        offscreen.setStroke(stroke);
    }

    public static void setDashPenRadius(double r) {
        if (r < 0) throw new IllegalArgumentException("pen radius must be nonnegative");
        penDashRadius = r;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);
        Stroke dotted = new BasicStroke(scaledPenRadius, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 8, new float[] {1,2}, 0);
        // BasicStroke stroke = new BasicStroke(scaledPenRadius);
        offscreen.setStroke(dotted);
    }

    public static Color getPenColor() { return penColor; }

    public static void setPenColor() { setPenColor(DEFAULT_PEN_COLOR); }

    public static void setPenColor(Color color) {
        penColor = color;
        offscreen.setColor(penColor);
    }

    public static void setPenColor(int red, int green, int blue) {
        if (red   < 0 || red   >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (green < 0 || green >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        if (blue  < 0 || blue  >= 256) throw new IllegalArgumentException("amount of red must be between 0 and 255");
        setPenColor(new Color(red, green, blue));
    }

    public static Font getFont() { return font; }

    public static void setFont() { 
    	 //File fontFile = new File("CoreHumanistSans-Regular.otf");
    	
    	 //try {
    	    //Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
    	    //GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
    	 //} catch (Exception ex) {     
    	 //}

    	//setFont(new Font("CoreHumanistSans-Regular", Font.TRUETYPE_FONT, 32));
    	//setFont(DEFAULT_FONT); 
    }

    public static void setHeadingFont() { setFont(HEADING_FONT); }
    
    public static void setInfoBigFont() { setFont(INFO_BIG_FONT); }
    
    public static void setDefaultFont() { setFont(DEFAULT_FONT); }
    
    public static void setTinyFont() { setFont(TINY_FONT); }
    
    public static void setInfoSmallFont() { setFont(INFO_SMALL_FONT); }
    
    public static void setGameOverFont() { setFont(GAME_OVER_FONT); }
    
    public static void setFont(Font f) { font = f; }
    
    public static void setXscale() { setXscale(DEFAULT_XMIN, DEFAULT_XMAX); }

    public static void setYscale() { setYscale(DEFAULT_YMIN, DEFAULT_YMAX); }

    public static void setXscale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
        }
    }

    public static void setYscale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
        }
    }

    public static void setScale(double min, double max) {
        double size = max - min;
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
        }
    }

    // helper functions that scale from user coordinates to screen coordinates and back
    private static double  scaleX(double x) { return width  * (x - xmin) / (xmax - xmin); }
    private static double  scaleY(double y) { return height * (ymax - y) / (ymax - ymin); }
    private static double factorX(double w) { return w * width  / Math.abs(xmax - xmin);  }
    private static double factorY(double h) { return h * height / Math.abs(ymax - ymin);  }
    private static double   userX(double x) { return xmin + x * (xmax - xmin) / width;    }
    private static double   userY(double y) { return ymax - y * (ymax - ymin) / height;   }

    private static void bonusesChangeStateChanged(ChangeEvent event) {
		hexVenture.numBonus = bonuses.getValue();
		numBonuses.setText(hexVenture.numBonus+"");
	}
    
    private static void sliderRChangeStateChanged(ChangeEvent event) {
		Color newColor = new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
		//menu.getContentPane().setBackground(newColor);
		endGameShadow.getContentPane().setBackground(newColor);
	}
    
    private static void sliderGChangeStateChanged(ChangeEvent event) {
		Color newColor = new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
		//menu.getContentPane().setBackground(newColor);
		endGameShadow.getContentPane().setBackground(newColor);
	}
    
    private static void sliderBChangeStateChanged(ChangeEvent event) {
		Color newColor = new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
		//menu.getContentPane().setBackground(newColor);
		endGameShadow.getContentPane().setBackground(newColor);
	}
    
    public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == cbMenuItem1){
	    	hexVenture.sound = true;
	    if (e.getStateChange() == ItemEvent.DESELECTED)
	    	hexVenture.sound = false;
		}
	    if (source == cbMenuItem2){
	    	showStats();
	    if (e.getStateChange() == ItemEvent.DESELECTED)
	    	hideStats();
	    }
	}
	
    public static void checkGraphState(){
    	if (cbMenuItem2.isSelected())
    		showStats();
    }
    
	public void actionPerformed(ActionEvent e) {
		Random rand = new Random();
		int r = 51, g = 51, b = 51;
		
	    if ("add another player".equals(e.getActionCommand())) {
	    	hexVenture.player[hexVenture.playerTurn].setAI(AI.isSelected());
	    	
	    	if (hexVenture.player[hexVenture.playerTurn].isAI()){
	    		r = sliderR.getValue();
	    		g = sliderG.getValue();
	    		b = sliderB.getValue();
	    		while ((r <= 50) && (g <= 50) && (b <= 50)){
	    			r = rand.nextInt(255);
	    			g = rand.nextInt(255);
	    			b = rand.nextInt(255);
	    		}
	    		sliderR.setValue(r);
	    		sliderG.setValue(g);
	    		sliderB.setValue(b);
	    	}
	    	
	    	hexVenture.player[hexVenture.playerTurn].setR(sliderR.getValue());
	    	hexVenture.player[hexVenture.playerTurn].setG(sliderG.getValue());
	    	hexVenture.player[hexVenture.playerTurn].setB(sliderB.getValue());
	    	hexVenture.player[hexVenture.playerTurn].setDr(sliderR.getValue()/2);
	    	hexVenture.player[hexVenture.playerTurn].setDg(sliderG.getValue()/2);
	    	hexVenture.player[hexVenture.playerTurn].setDb(sliderB.getValue()/2);
			hexVenture.player[hexVenture.playerTurn].setActualColor(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
			hexVenture.player[hexVenture.playerTurn].setShadeColor(sliderR.getValue()/2, sliderG.getValue()/2, sliderB.getValue()/2);
			
			hexVenture.player[hexVenture.playerTurn].setName(tf.getText());
			sliderR.setValue(50);
			sliderG.setValue(50);
			sliderB.setValue(50);
			Color newColor = new Color(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
			endGameShadow.getContentPane().setBackground(newColor);
			AI.setSelected(false);
	        hexVenture.playerTurn++;
	        hexVenture.numPlayers++;
	        tf.setText("player "+(hexVenture.numPlayers+1));
	        if (hexVenture.numPlayers == 5)
	        	addPlayer.setEnabled(false);
	        //if (hexVenture.sound)
				//SoundEffect.EndTurn.play();
	    }
	    else if ("start game".equals(e.getActionCommand())) {
	    	if (hexVenture.createMapCounter < hexVenture.realHexes.size()-1)
	    		return;
	    	else if (hexVenture.numPlayers == 0)
	    		return;
	    	
	    	hexVenture.player[hexVenture.playerTurn].setAI(AI.isSelected());
	    	
	    	if (hexVenture.player[hexVenture.playerTurn].isAI()){
	    		r = sliderR.getValue();
	    		g = sliderG.getValue();
	    		b = sliderB.getValue();
	    		while ((r <= 50) && (g <= 50) && (b <= 50)){
	    			r = rand.nextInt(255);
	    			g = rand.nextInt(255);
	    			b = rand.nextInt(255);
	    		}
	    		sliderR.setValue(r);
	    		sliderG.setValue(g);
	    		sliderB.setValue(b);
	    	}
	    	
	    	hexVenture.player[hexVenture.playerTurn].setName(tf.getText());
	    	hexVenture.player[hexVenture.playerTurn].setR(sliderR.getValue());
	    	hexVenture.player[hexVenture.playerTurn].setG(sliderG.getValue());
	    	hexVenture.player[hexVenture.playerTurn].setB(sliderB.getValue());
	    	hexVenture.player[hexVenture.playerTurn].setDr(sliderR.getValue()/2);
	    	hexVenture.player[hexVenture.playerTurn].setDg(sliderG.getValue()/2);
	    	hexVenture.player[hexVenture.playerTurn].setDb(sliderB.getValue()/2);
			hexVenture.player[hexVenture.playerTurn].setActualColor(sliderR.getValue(), sliderG.getValue(), sliderB.getValue());
			hexVenture.player[hexVenture.playerTurn].setShadeColor(sliderR.getValue()/2, sliderG.getValue()/2, sliderB.getValue()/2);

			//hexVenture.numPlayers = 0;
	    	hexVenture.playerTurn=0;
	    	//hexVenture.isEditor=true;
	    	hexVenture.numBonus = 3;
	    	hexVenture.setupGame();
	    	hexVenture.period = 1;
			StdDraw.mapProgress.setVisible(false);
	    }
	    else if ("Small".equals(e.getActionCommand())) {
	    	hexVenture.mapSize = 100;
			hexVenture.resetMap();
	    }
	    else if ("Medium".equals(e.getActionCommand())) {
	    	hexVenture.mapSize = 500;
			hexVenture.resetMap();
	    }
	    else if ("Large".equals(e.getActionCommand())) {
	    	hexVenture.mapSize = 1000;
			hexVenture.resetMap();
	    }
	    else if ("Hex History".equals(e.getActionCommand())) {
	    	graph.setGraphViewNum(0);
	    	graph.repaint();
	    }
	    else if ("Men History".equals(e.getActionCommand())) {
	    	graph.setGraphViewNum(1);
	    	graph.repaint();
	    }
	    else if ("Wage History".equals(e.getActionCommand())) {
	    	graph.setGraphViewNum(2);
	    	graph.repaint();
	    }
	    else if ("watch replay".equals(e.getActionCommand())) {
	    	hexVenture.resume = false;
	    	hideStats();
	    	hexVenture.timerReplayGame = new Timer();
	    	hexVenture.fx = -1;
	    	StdDraw.clear();
			hexVenture.replayGameTimer();
	    }
	    //else if ("New Game".equals(e.getActionCommand())) 
			//hexVenture.newGame();
		else if ("Exit".equals(e.getActionCommand())){
			//try {
			//	hexVenture.saveGame();
			//} catch (IOException e1) {
			//	e1.printStackTrace();
			//}
			System.exit(0);
		}
		else if ("Slow".equals(e.getActionCommand()))
			hexVenture.setAiSpeed(500);
		else if ("Normal".equals(e.getActionCommand()))
			hexVenture.setAiSpeed(100);
		else if ("Fast".equals(e.getActionCommand()))
			hexVenture.setAiSpeed(1);
	} 
	
	    public static boolean mousePressed() {
	        synchronized (mouseLock) {
	            return mousePressed;
	        }
	    }

	    public static double mouseX() {
	        synchronized (mouseLock) {
	            return mouseX;
	        }
	    }

	    public static double mouseY() {
	        synchronized (mouseLock) {
	            return mouseY;
	        }
	    }

	    public static double mouseXX() {
	        synchronized (mouseLock) {
	            return mouseXX;
	        }
	    }

	    public static double mouseYY() {
	        synchronized (mouseLock) {
	            return mouseYY;
	        }
	    }
	    
	    public void mouseClicked(MouseEvent e) { }

	    public void mouseEntered(MouseEvent e) { }

	    public void mouseExited(MouseEvent e) { }

	    public void mousePressed(MouseEvent e) {
	        synchronized (mouseLock) {
	            mouseX = userX(e.getX());
	            mouseY = userY(e.getY());
	            mousePressed = true;
	        }
	    }

	    public static void popup(){
	    	JOptionPane.showMessageDialog(frame, hexVenture.player[hexVenture.playerTurn].getName());
	    }
	    
	    public static void popupName(){
			//String s = (String)JOptionPane.showInputDialog("What is your name?");

			//If a string was returned, say so.
			//if ((s != null) && (s.length() > 0)) {
			//    hexVenture.player[hexVenture.playerTurn].setName(s);
			//    setPlayerColor(hexVenture.playerTurn);
			//    hexVenture.playerTurn++;
			//    return;
			//}

			//If you're here, the return value was null/empty.
			//setLabel("Come on, finish the sentence!");
	    }
	    
	    public static void setPlayerColor(int p){
	    	 //popup = new JPopupMenu();
	    	 //slider = new JSlider();
	    	 
	    	 //popup.add(slider);
	    	 //popup.add(new JSeparator());
	    	 //popup.setVisible(true);
	    	 //popup.show(popup, p, p);
	    }
	    
	    /**
	     * This method cannot be called directly.
	     */
	    public void mouseDragged(MouseEvent e)  {
	        synchronized (mouseLock) {
	            mouseX = userX(e.getX());
	            mouseY = userY(e.getY());
	        }
	    }

	    /**
	     * This method cannot be called directly.
	     */
	    public void mouseMoved(MouseEvent e) {
	        synchronized (mouseLock) {
	        	mouseXX = e.getX();
	            mouseYY = e.getY();
	            
	        	mouseX = userX(e.getX());
	            mouseY = userY(e.getY());
	        	int hex = 0;
	        	
	        	hexVenture.buttonPanel.one.setHighlighted(false);
    			hexVenture.buttonPanel.two.setHighlighted(false);
    			hexVenture.buttonPanel.endturn.setHighlighted(false);
    			
	        	if ((mouseX > (hexVenture.buttonPanel.one.getX()-hexVenture.buttonPanel.one.getW())) && (mouseX < (hexVenture.buttonPanel.one.getX()+hexVenture.buttonPanel.one.getW()))){
	        		if ((mouseY > (hexVenture.buttonPanel.one.getY()-hexVenture.buttonPanel.one.getH())) && (mouseY < (hexVenture.buttonPanel.one.getY()+hexVenture.buttonPanel.one.getH()))){
	        			hexVenture.buttonPanel.one.setHighlighted(true);
	        			hexVenture.buttonPanel.two.setHighlighted(false);
	        			hexVenture.buttonPanel.endturn.setHighlighted(false);
	        			hexVenture.buttonPanel.one.resetRGB();
	        			hexVenture.buttonPanel.one.setR((int) hexVenture.player[hexVenture.playerTurn].getR());
	        			hexVenture.buttonPanel.one.setG((int) hexVenture.player[hexVenture.playerTurn].getG());
	        			hexVenture.buttonPanel.one.setB((int) hexVenture.player[hexVenture.playerTurn].getB());
	        		}
	    		}
	        		
	    		if ((mouseX > (hexVenture.buttonPanel.two.getX()-hexVenture.buttonPanel.two.getW())) && (mouseX < (hexVenture.buttonPanel.two.getX()+hexVenture.buttonPanel.two.getW()))){
	        		if ((mouseY > (hexVenture.buttonPanel.two.getY()-hexVenture.buttonPanel.two.getH())) && (mouseY < (hexVenture.buttonPanel.two.getY()+hexVenture.buttonPanel.two.getH()))){
	        			hexVenture.buttonPanel.one.setHighlighted(false);
	        			hexVenture.buttonPanel.two.setHighlighted(true);
	        			hexVenture.buttonPanel.endturn.setHighlighted(false);
	        			hexVenture.buttonPanel.two.resetRGB();
	        			hexVenture.buttonPanel.two.setR((int) hexVenture.player[hexVenture.playerTurn].getR());
	        			hexVenture.buttonPanel.two.setG((int) hexVenture.player[hexVenture.playerTurn].getG());
	        			hexVenture.buttonPanel.two.setB((int) hexVenture.player[hexVenture.playerTurn].getB());
	        		}
	    		}
	    		
	    			if ((mouseX > (hexVenture.buttonPanel.endturn.getX()-hexVenture.buttonPanel.endturn.getW())) && (mouseX < (hexVenture.buttonPanel.endturn.getX()+hexVenture.buttonPanel.endturn.getW()))){
	    	    		if ((mouseY > (hexVenture.buttonPanel.endturn.getY()-hexVenture.buttonPanel.endturn.getH())) && (mouseY < (hexVenture.buttonPanel.endturn.getY()+hexVenture.buttonPanel.endturn.getH()))){
	    	    			hexVenture.buttonPanel.one.setHighlighted(false);
		        			hexVenture.buttonPanel.two.setHighlighted(false);
		        			hexVenture.buttonPanel.endturn.setHighlighted(true);
	    	    			hexVenture.buttonPanel.endturn.resetRGB();
	    	    			hexVenture.buttonPanel.endturn.setR((int) hexVenture.player[hexVenture.playerTurn].getR());
	    	    			hexVenture.buttonPanel.endturn.setG((int) hexVenture.player[hexVenture.playerTurn].getG());
	    	    			hexVenture.buttonPanel.endturn.setB((int) hexVenture.player[hexVenture.playerTurn].getB());
	    	    		}
	    			}
	    			
	    			hexVenture.buttonPanel.one.setFillColor();
	    			hexVenture.buttonPanel.two.setFillColor();
	    			hexVenture.buttonPanel.endturn.setFillColor();
	    			
	    			
	        	if ((!hexVenture.resume) || (hexVenture.selectedFighter < 0))
	        		return;
	        	for (int i=0;i<(hexVenture.BSIZE);i++){
	    			if ((mouseX > (hexVenture.hexes[i].getX())) && (mouseX < (hexVenture.hexes[i].getX()+hexVenture.HEXSIZE))){
	    	    		if ((mouseY > (hexVenture.hexes[i].getY())) && (mouseY < (hexVenture.hexes[i].getY()+hexVenture.HEXSIZE))){
	            			hex = i;
	            			break;
	            		}
	    			}
	    		}
				
	        	hexVenture.hexHighlight = hex;
	        }
	    }


	   /*************************************************************************
	    *  Keyboard interactions.
	    *************************************************************************/

	    /**
	     * Has the user typed a key?
	     * @return true if the user has typed a key, false otherwise
	     */
	    public static boolean hasNextKeyTyped() {
	        synchronized (keyLock) {
	            return !keysTyped.isEmpty();
	        }
	    }

	    /**
	     * What is the next key that was typed by the user? This method returns
	     * a Unicode character corresponding to the key typed (such as 'a' or 'A').
	     * It cannot identify action keys (such as F1
	     * and arrow keys) or modifier keys (such as control).
	     * @return the next Unicode key typed
	     */
	    public static char nextKeyTyped() {
	        synchronized (keyLock) {
	            return keysTyped.removeLast();
	        }
	    }

	    /**
	     * Is the keycode currently being pressed? This method takes as an argument
	     * the keycode (corresponding to a physical key). It can handle action keys
	     * (such as F1 and arrow keys) and modifier keys (such as shift and control).
	     * See <a href = "http://download.oracle.com/javase/6/docs/api/java/awt/event/KeyEvent.html">KeyEvent.java</a>
	     * for a description of key codes.
	     * @return true if keycode is currently being pressed, false otherwise
	     */
	    public static boolean isKeyPressed(int keycode) {
	        synchronized (keyLock) {
	            return keysDown.contains(keycode);
	        }
	    }


	    /**
	     * This method cannot be called directly.
	     */
	    public void keyTyped(KeyEvent e) {
	        synchronized (keyLock) {
	            keysTyped.addFirst(e.getKeyChar());
	        }
	    }

	    /**
	     * This method cannot be called directly.
	     */
	    public void keyPressed(KeyEvent e) {
	        synchronized (keyLock) {
	            keysDown.add(e.getKeyCode());
	        }
	    }

	    /**
	     * This method cannot be called directly.
	     */
	    public void keyReleased(KeyEvent e) {
	        synchronized (keyLock) {
	            keysDown.remove(e.getKeyCode());
	        }
	    }

		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
        	if (hexVenture.buttonPanel.one.isEnabled()){
        	if ((mouseX > (hexVenture.buttonPanel.one.getX()-hexVenture.buttonPanel.one.getW())) && (mouseX < (hexVenture.buttonPanel.one.getX()+hexVenture.buttonPanel.one.getW()))){
        		if ((mouseY > (hexVenture.buttonPanel.one.getY()-hexVenture.buttonPanel.one.getH())) && (mouseY < (hexVenture.buttonPanel.one.getY()+hexVenture.buttonPanel.one.getH()))){
        			hexVenture.buttonPanel.one.performButton(1);
        			hexVenture.buttonPanel.one.resetRGB();
        			hexVenture.buttonPanel.one.setR(240);
        			hexVenture.buttonPanel.one.setG(240);
        			hexVenture.buttonPanel.one.setB(240);
        		}
        	}
        	}
        	
        	if (hexVenture.buttonPanel.two.isEnabled()){
        	if ((mouseX > (hexVenture.buttonPanel.two.getX()-hexVenture.buttonPanel.two.getW())) && (mouseX < (hexVenture.buttonPanel.two.getX()+hexVenture.buttonPanel.two.getW()))){
        		if ((mouseY > (hexVenture.buttonPanel.two.getY()-hexVenture.buttonPanel.two.getH())) && (mouseY < (hexVenture.buttonPanel.two.getY()+hexVenture.buttonPanel.two.getH()))){
        			hexVenture.buttonPanel.two.performButton(2);
        			hexVenture.buttonPanel.two.resetRGB();
        			hexVenture.buttonPanel.two.setR(240);
        			hexVenture.buttonPanel.two.setG(240);
        			hexVenture.buttonPanel.two.setB(240);
        		}
        	}
        	}
        	
        	if (hexVenture.buttonPanel.endturn.isEnabled()){
	        	if ((mouseX > (hexVenture.buttonPanel.endturn.getX()-hexVenture.buttonPanel.endturn.getW())) && (mouseX < (hexVenture.buttonPanel.endturn.getX()+hexVenture.buttonPanel.endturn.getW()))){
	        		if ((mouseY > (hexVenture.buttonPanel.endturn.getY()-hexVenture.buttonPanel.endturn.getH())) && (mouseY < (hexVenture.buttonPanel.endturn.getY()+hexVenture.buttonPanel.endturn.getH()))){
	        			hexVenture.buttonPanel.endturn.performButton(4);
	        			hexVenture.buttonPanel.endturn.resetRGB();
	        			hexVenture.buttonPanel.endturn.setR(240);
	        			hexVenture.buttonPanel.endturn.setG(240);
	        			hexVenture.buttonPanel.endturn.setB(240);
	        		}
	        	}
	        	}
        	
        	if (e.isMetaDown()){
        		if (hexVenture.selectedFighter < 0){
        			if (hexVenture.buttonPanel.one.isEnabled() && hexVenture.selectedBase>=0){
						hexVenture.purchaseFighter(hexVenture.selectedPlayer, hexVenture.selectedBase);
						hexVenture.buttonPanel.one.resetRGB();
        				hexVenture.buttonPanel.one.setR(240);
        				hexVenture.buttonPanel.one.setG(240);
        				hexVenture.buttonPanel.one.setB(240);	
					}
        		}else if ((hexVenture.selectedFighter >= 0) && (hexVenture.player[hexVenture.selectedPlayer].base.get(hexVenture.selectedBase).fighter.get(hexVenture.selectedFighter).getHexNum() != hexVenture.player[hexVenture.selectedPlayer].base.get(hexVenture.selectedBase).getHexNum())){
        			hexVenture.isPlacingFighter = false;
        			hexVenture.player[hexVenture.selectedPlayer].base.get(hexVenture.selectedBase).fighter.get(hexVenture.selectedFighter).setSelected(false);
        			hexVenture.selectedFighter = -1;
        			for (int h=0;h<hexVenture.BSIZE;h++)
        				hexVenture.hexes[h].setHighlighted(false);
        		}
				return;
			}

        	mouseX = e.getX();
            mouseY = e.getY();
        	int px = 0, py = 0;
        	for (int i=0;i<(hexVenture.BSIZE);i++){
    			if ((mouseX > (hexVenture.hexes[i].getX())) && (mouseX < (hexVenture.hexes[i].getX()+hexVenture.HEXSIZE))){
    	    		if ((mouseY > (hexVenture.hexes[i].getY())) && (mouseY < (hexVenture.hexes[i].getY()+hexVenture.HEXSIZE))){
    	    			hexVenture.selectedHex = i;
    	    			px = hexVenture.hexes[i].getColumn();
    	    			py = hexVenture.hexes[i].getRow();
            			break;
            		}
    			}
    		}
        	
			//Point p = new Point( pxtoHex((e.getX()),(e.getY())) );
			//if (p.x < 0 || p.y < 0 || p.x >= hexVenture.COLSIZE || p.y >= hexVenture.ROWSIZE){
				//return;
			//}
			
			//for (int i=0;i<(hexVenture.BSIZE);i++){
				//if ((hexVenture.hexes[i].getColumn()==p.x)&&(hexVenture.hexes[i].getRow()==p.y)){
					//hexVenture.selectedHex=i;
					//break;
				//}
			//}
			
			//if ((!hexVenture.isPlacingFighter)){
			//	if ((!hexVenture.hexes[hexVenture.selectedHex].isVisible())||(hexVenture.hexes[hexVenture.selectedHex].getState()!=hexVenture.player[hexVenture.playerTurn].getColor())){
			//	}
			//	if ((hexVenture.hexes[hexVenture.selectedHex].isVisible())&&(hexVenture.hexes[hexVenture.selectedHex].getState()==hexVenture.player[hexVenture.playerTurn].getColor())){
			//	}
			//}
			
			if (hexVenture.isEditor){
				if (!hexVenture.isBonusTool){

				}else{
					if (hexVenture.hexes[hexVenture.selectedHex].occupation.getOccupiedBy()=="bonus"){
						hexVenture.hexes[hexVenture.selectedHex].occupation.setOccupiedBy("empty");
						return;
					}
					if (hexVenture.hexes[hexVenture.selectedHex].occupation.getOccupiedBy()=="empty"){
						hexVenture.hexes[hexVenture.selectedHex].occupation.setOccupiedBy("bonus");
						return;
					}
				}
				return;
			}
			
				if ((!hexVenture.isPlacingFighter) && (!hexVenture.isPlacingOutpost)) {	
					if (hexVenture.hexes[hexVenture.selectedHex].occupation.getPlayer() == hexVenture.playerTurn){
						hexVenture.selectedPlayer=hexVenture.playerTurn;
						hexVenture.selectedBase=hexVenture.hexes[hexVenture.selectedHex].occupation.getBase();
						hexVenture.selectBase(hexVenture.selectedPlayer,hexVenture.selectedBase);
	
						if (hexVenture.hexes[hexVenture.selectedHex].occupation.getFighter() > -1){
							if (!hexVenture.player[hexVenture.selectedPlayer].base.get(hexVenture.selectedBase).fighter.get(hexVenture.hexes[hexVenture.selectedHex].occupation.getFighter()).isMoved()){
								hexVenture.selectedFighter = hexVenture.hexes[hexVenture.selectedHex].occupation.getFighter();
								hexVenture.selectFighter(hexVenture.selectedPlayer, hexVenture.selectedBase, hexVenture.selectedFighter);
								return;
							}
						}
					}else{
						return;
					}
				}
					
				if (hexVenture.isPlacingFighter){
					if (hexVenture.hexes[hexVenture.selectedHex].isHighlighted){ // this hex is permissible to be placed upon...
						if (hexVenture.selectedHex==hexVenture.player[hexVenture.playerTurn].base.get(hexVenture.selectedBase).fighter.get(hexVenture.selectedFighter).getHexNum()){
							for (int h=0;h<hexVenture.BSIZE;h++) // player placed fighter on itself, reset, undo
								hexVenture.hexes[h].setHighlighted(false);
							hexVenture.isPlacingFighter=false;
							hexVenture.player[hexVenture.playerTurn].base.get(hexVenture.selectedBase).fighter.get(hexVenture.selectedFighter).setSelected(false);
							hexVenture.selectedFighter = -1;
							return;
						}
							if (hexVenture.hexes[hexVenture.selectedHex].occupation.getFighter() > -1){ // a fighter
								if (hexVenture.hexes[hexVenture.selectedHex].occupation.getPlayer()==hexVenture.playerTurn){ // own fighter
									hexVenture.combineFighters(hexVenture.playerTurn,hexVenture.selectedBase,hexVenture.selectedFighter,hexVenture.hexes[hexVenture.selectedHex].occupation.getFighter());
								}else{ // enemy fighter
									hexVenture.killFighter(hexVenture.hexes[hexVenture.selectedHex].occupation.getPlayer(),hexVenture.hexes[hexVenture.selectedHex].occupation.getBase(),hexVenture.hexes[hexVenture.selectedHex].occupation.getFighter());
									hexVenture.player[hexVenture.playerTurn].setFightersKilled(hexVenture.player[hexVenture.playerTurn].getFightersKilled()+1);
								
									hexVenture.placeFighterOnHex(hexVenture.selectedHex,hexVenture.playerTurn,hexVenture.selectedBase,hexVenture.selectedFighter,px,py, "null");
								}
							}
							else if (hexVenture.hexes[hexVenture.selectedHex].occupation.getOccupiedBy()=="base") // a base
								hexVenture.placeFighterOnHex(hexVenture.selectedHex,hexVenture.playerTurn,hexVenture.selectedBase,hexVenture.selectedFighter,px,py, "sack");
							else if (hexVenture.hexes[hexVenture.selectedHex].occupation.getOccupiedBy()=="empty") // unoccupied hex
								hexVenture.placeFighterOnHex(hexVenture.selectedHex,hexVenture.playerTurn,hexVenture.selectedBase,hexVenture.selectedFighter,px,py, "null");
						
						//hexVenture.checkBaseLink(hexVenture.selectedHex,hexVenture.playerTurn,hexVenture.selectedBase);
						
					}
					else{
						if (hexVenture.sound)
							SoundEffect.BadBeep.play();
						return; // selected hex isn't an option to be placed upon
					}
					for (int h=0;h<hexVenture.BSIZE;h++)
						hexVenture.hexes[h].setHighlighted(false);
					hexVenture.isPlacingFighter=false;
				}
				
				if (hexVenture.isPlacingOutpost){
					if (hexVenture.hexes[hexVenture.selectedHex].isHighlighted){ 
						hexVenture.hexes[hexVenture.selectedHex].occupation.setPlayerOutpost(hexVenture.playerTurn);
						hexVenture.hexes[hexVenture.selectedHex].occupation.setOccupiedBy("outpost");
					}
					hexVenture.isPlacingOutpost = false;
				}
				
			hexVenture.setVisibleGrid();
			hexVenture.selectBase(hexVenture.playerTurn, hexVenture.selectedBase);
            mousePressed = false;
        }

}
