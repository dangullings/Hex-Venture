import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {
	
	private static final Font FONT = new Font("Square721 BT", Font.BOLD, 10);
	
	int graphViewNum = 0;
	
    public GraphPanel() {
        super();
        setBackground(Color.WHITE);
    }

    public void paintComponent(Graphics g) {
        int xIndent = 25;             // width of window in pixels
        int yIndent = 40;           // height of window in pixels
        double height = 235;
        double x = 0, y = 0;
        
        super.paintComponent(g);            // call superclass to make panel display correctly

        Graphics2D g2 = (Graphics2D) g;
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(xIndent, getHeight()-yIndent, getWidth()-xIndent, getHeight()-yIndent);
        g2.drawLine(xIndent, getHeight()-yIndent, xIndent, 15);
        
        Double high = 0.0;
        for (int p = 0; p <= hexVenture.numPlayers; p++){
        	for (int t = 0; t <= hexVenture.turn; t++){
        		if (graphViewNum == 0){
        			if (hexVenture.player[p].hexHistory.get(t) > high)
        				high = hexVenture.player[p].hexHistory.get(t);
        		}
        		else if (graphViewNum == 1){
        			if (hexVenture.player[p].menHistory.get(t) > high)
        				high = hexVenture.player[p].menHistory.get(t);
        		}
        		else if (graphViewNum == 2){
        			if (hexVenture.player[p].wageHistory.get(t) > high)
        				high = hexVenture.player[p].wageHistory.get(t);
        		}
        	}
        }
        
        while ((int)(high * .25) != (high * .25))
        	high++;
        
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.GRAY);
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
        g2d.setStroke(dashed);
        g2d.draw(new Line2D.Double(xIndent, 132.5, getWidth()-xIndent, 132.5)); // (height / 2) + 15
        g2d.draw(new Line2D.Double(xIndent, 191.25, getWidth()-xIndent, 191.25)); // (height / .75) + 15
        g2d.draw(new Line2D.Double(xIndent, 73.75, getWidth()-xIndent, 73.75)); // (height / .25) + 15
        g2d.dispose();
        
        g2.setFont(FONT);
        g2.drawString(""+(int)(high*1), 0, 19);
		g2.drawString(""+(int)(high/2), 0, 135);
		g2.drawString(""+(int)(high*.25), 0, 196);
		g2.drawString(""+(int)(high*.75), 0, 77);
		g2.drawString(""+(int)(0), 0, 265);
		g2.drawString(""+("turns"), (getWidth()/2)-15, 282);
		
        x = xIndent;
        y = getHeight() - yIndent;
        for (int p = 0; p <= hexVenture.numPlayers; p++){
        	for (double t = 0; t <= hexVenture.turn; t++){
        		double prevX = x;
        		double prevY = y;
        		x = xIndent + ((t / hexVenture.turn) * (getWidth()-(xIndent*2)));
        		if (graphViewNum == 0)
        			y = (getHeight() - yIndent) - ((hexVenture.player[p].hexHistory.get((int) t) / high) * (getHeight()-(yIndent+15)));
        		else if (graphViewNum == 1)
        			y = (getHeight() - yIndent) - ((hexVenture.player[p].menHistory.get((int) t) / high) * (getHeight()-(yIndent+15)));
        		else if (graphViewNum == 2)
        			y = (getHeight() - yIndent) - ((hexVenture.player[p].wageHistory.get((int) t) / high) * (getHeight()-(yIndent+15)));
        		
        		g2.setColor(hexVenture.player[p].getColorActual());
        		
        		if ((x > xIndent) || (y < (getHeight()-yIndent))){
        		Ellipse2D.Double shape = new Ellipse2D.Double(x, y, 2, 2);
        	    g2.draw(shape);
        	    
        		g2.draw(new Line2D.Double(prevX, prevY, x, y)); 
        		
        		if ((int)t == (hexVenture.turn / 2)){
        			g2.setColor(Color.DARK_GRAY);
        			g2.draw(new Line2D.Double(x, getHeight()-yIndent, x, getHeight()-(yIndent+7)));
        			g2.drawString(""+(int)t, (int) x-6, getHeight()-(yIndent-15));
        		}
        		if ((int)t == (int)(hexVenture.turn * .75)){
        			g2.setColor(Color.DARK_GRAY);
        			g2.draw(new Line2D.Double(x, getHeight()-yIndent, x, getHeight()-(yIndent+7)));
        			g2.drawString(""+(int)t, (int) x-6, getHeight()-(yIndent-15));
        		}
        		if ((int)t == (int)(hexVenture.turn * .25)){
        			g2.setColor(Color.DARK_GRAY);
        			g2.draw(new Line2D.Double(x, getHeight()-yIndent, x, getHeight()-(yIndent+7)));
        			g2.drawString(""+(int)t, (int) x-6, getHeight()-(yIndent-15));
        		}
        		if ((int)t == (hexVenture.turn)){
        			g2.setColor(Color.DARK_GRAY);
        			g2.draw(new Line2D.Double(x, getHeight()-yIndent, x, getHeight()-(yIndent+7)));
        			g2.drawString(""+(int)t, (int) x-6, getHeight()-(yIndent-15));
        		}
        		}
        	}
        }

    }

	public int getGraphViewNum() {
		return graphViewNum;
	}

	public void setGraphViewNum(int graphViewNum) {
		this.graphViewNum = graphViewNum;
	}
    
}
