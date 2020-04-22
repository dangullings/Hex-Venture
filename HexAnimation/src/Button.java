import java.awt.Color;

public class Button {
	
	private Color color = new Color(0,0,0);
	private Color fillColor = new Color(0,0,0);
	private Color textColor = new Color(240,240,240);
	private double x, y, w, h;
	private boolean visible = true, enabled = true;
	private String message;
	private int r, g, b;
	private int rr, gg, bb;
	private boolean highlighted;
	
	public Button(){
		
	}
	
	public void setupButton(Color color, double x, double y, double w, double h, String message){
		this.color = color;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.message = message;
	}
	
	public void draw() {
    	StdDraw.setPenRadius(.005);
    	StdDraw.setPenColor(fillColor);
    	StdDraw.filledRectangle(x, y, w, h);
    	StdDraw.setPenColor(color);
        StdDraw.rectangle(x, y, w, h);
        StdDraw.setPenColor(Color.DARK_GRAY);
        if (!enabled)
        	StdDraw.setPenColor(Color.DARK_GRAY);
        StdDraw.setInfoSmallFont();
        StdDraw.text(x, y, message);
    }
	
	public void performButton(int num){
		if (visible){
			if (num == 1)
				hexVenture.purchaseFighter(hexVenture.playerTurn, hexVenture.selectedBase);
			//if (num == 2)
				//hexVenture.purchaseOutpost(hexVenture.playerTurn, hexVenture.selectedBase);
			if (num == 4)
				hexVenture.endTurn();
		}
	}

	public void resetRGB(){
		this.r = 0;
		this.g = 0;
		this.b = 0;
	}
	
	public void resetRRGGBB(){
		this.rr = 0;
		this.gg = 0;
		this.bb = 0;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public int getRR() {
		return rr;
	}

	public void setRR(int rr) {
		this.rr += rr;
	}

	public int getGG() {
		return gg;
	}

	public void setGG(int gg) {
		this.gg += gg;
	}

	public int getBB() {
		return bb;
	}

	public void setBB(int bb) {
		this.bb += bb;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r += r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g += g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b += b;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Color getColor() {
		return color;
	}

	public Color getFillColor() {
		return fillColor;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public void setFillColor() {
		this.fillColor = new Color(r, g, b);
	}
	
	public void setTextColor() {
		if (rr > 255)
			rr = 255;
		else if (rr < 0)
			rr = 0;
		if (gg > 255)
			gg = 255;
		else if (gg < 0)
			gg = 0;
		if (bb > 255)
			bb = 255;
		else if (bb < 0)
			bb = 0;
		this.textColor = new Color(rr, gg, bb);
	}
	
	public void setTextColor(Color color) {
		this.textColor = color;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}
	
	
}
