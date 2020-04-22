import java.awt.Color;

public class Label {
	
	private Color color = new Color(200,200,200);
	private double x, y;
	private boolean visible = false;
	private String message;
	private boolean isHeading;
	int value, setValue;
	public Label(){
		
	}
	
	public void setupLabel(Color color, double x, double y, String message){
		this.color = color;
		this.x = x;
		this.y = y;
		this.message = message;
	}
	
	public void draw() {
    	StdDraw.setPenColor(color);
    	StdDraw.setPenRadius(.002);
    	StdDraw.setHeadingFont();
    	StdDraw.textRight(x, y, message);
    }
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value += value;
	}

	public int getSetValue() {
		return setValue;
	}

	public void setSetValue(int setValue) {
		this.setValue = setValue;
	}

	public boolean isHeading() {
		return isHeading;
	}

	public void setHeading(boolean isHeading) {
		this.isHeading = isHeading;
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

	public void setColor(Color color) {
		this.color = color;
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
}
