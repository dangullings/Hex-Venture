import java.awt.Color;

import javax.swing.JTextField;

public class ButtonPanel {
	
	private Color color = new Color(0,0,0);
	
	Button one = new Button();
	Button two = new Button();
	Button endturn = new Button();
	
	Label lblSavings = new Label();
	Label lblIncome = new Label();
	Label lblWages = new Label();
	Label lblBank = new Label();
	Label lblBankNext = new Label();
	
	public ButtonPanel(){
		
	}
	
	public void draw() {
		if (one.isVisible())
			one.draw();
		if (two.isVisible())
			two.draw();
		if (endturn.isVisible())
			endturn.draw();
		
		StdDraw.setPenColor(Color.DARK_GRAY);
		StdDraw.setHeadingFont();
		
		if (lblSavings.isVisible()){
	    	StdDraw.textRight(lblSavings.getX() - 8, lblSavings.getY(), "savings");
			lblSavings.draw();
		}
		if (lblIncome.isVisible()){
	    	StdDraw.textRight(lblIncome.getX() - 8, lblIncome.getY(), "income");
	    	StdDraw.text(lblIncome.getX() - 5, lblIncome.getY(), "+");
			lblIncome.draw();
		}
		if (lblWages.isVisible()){
	    	StdDraw.textRight(lblWages.getX() - 8, lblWages.getY(), "expense");
	    	StdDraw.text(lblWages.getX() - 5, lblWages.getY(), "-");
			lblWages.draw();
		}
		
		StdDraw.drawLine((int)lblBank.getX() - 6, (int)lblBank.getY()+3, (int)lblBank.getX(), (int)lblBank.getY()+3);
		
		if (lblBank.isVisible()){
	    	StdDraw.textRight(lblBank.getX() - 8, lblBank.getY(), "bank");
	    	StdDraw.text(lblBank.getX() - 5, lblBank.getY(), "$");
	    	lblBank.draw();
		}
		
		if (lblBankNext.isVisible()){
			StdDraw.setDefaultFont();
	    	//StdDraw.textRight(lblBankNext.getX() - 3, lblBankNext.getY(), "est.");
	    	lblBankNext.draw();
		}
    }
	
	public void reset(){
		one.resetRGB();
		two.resetRGB();
		endturn.resetRGB();
		one.setR((int) (hexVenture.player[hexVenture.playerTurn].getR()));
		one.setG((int) (hexVenture.player[hexVenture.playerTurn].getG()));
		one.setB((int) (hexVenture.player[hexVenture.playerTurn].getB()));
		two.setR((int) (hexVenture.player[hexVenture.playerTurn].getR()));
		two.setG((int) (hexVenture.player[hexVenture.playerTurn].getG()));
		two.setB((int) (hexVenture.player[hexVenture.playerTurn].getB()));
		endturn.setR((int) (hexVenture.player[hexVenture.playerTurn].getR()));
		endturn.setG((int) (hexVenture.player[hexVenture.playerTurn].getG()));
		endturn.setB((int) (hexVenture.player[hexVenture.playerTurn].getB()));
		one.setFillColor();
		two.setFillColor();
		endturn.setFillColor();
		one.resetRRGGBB();
		two.resetRRGGBB();
		endturn.resetRRGGBB();
		one.setRR((int) (hexVenture.player[hexVenture.playerTurn].getR()));
		one.setGG((int) (hexVenture.player[hexVenture.playerTurn].getG()));
		one.setBB((int) (hexVenture.player[hexVenture.playerTurn].getB()));
		two.setRR((int) (hexVenture.player[hexVenture.playerTurn].getR()));
		two.setGG((int) (hexVenture.player[hexVenture.playerTurn].getG()));
		two.setBB((int) (hexVenture.player[hexVenture.playerTurn].getB()));
		endturn.setRR((int) (hexVenture.player[hexVenture.playerTurn].getR()));
		endturn.setGG((int) (hexVenture.player[hexVenture.playerTurn].getG()));
		endturn.setBB((int) (hexVenture.player[hexVenture.playerTurn].getB()));
		one.setTextColor();
		two.setTextColor();
		endturn.setTextColor();
		one.setColor(hexVenture.player[hexVenture.playerTurn].getColorActual());
		two.setColor(hexVenture.player[hexVenture.playerTurn].getColorActual());
		endturn.setColor(hexVenture.player[hexVenture.playerTurn].getColorActual());
		one.setEnabled(false);
		two.setEnabled(false);
		endturn.setEnabled(false);
	}
}
