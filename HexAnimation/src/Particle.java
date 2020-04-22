import java.awt.Color;
import java.util.Random;

public class Particle {
    private double x, y;    // position
    private double vx, vy;    // velocity
    private double vvx, vvy;
    private double r;    // radius
    private double mass;      // mass
    private double targetX, targetY;
    private double startX, startY, tarX, tarY, tarXX, tarYY;
	private double speed, speedX;
	private double totalDist, totalDistX, dist, distX;
	private double mag, magX;
	private double distPerc, distXPerc;
	private int targetPlanet;
	private int red, green, blue;
	private float tran;
	private Color color = Color.WHITE;      // color
	private boolean hasTarget;
	private int player;
	private double explodeRadius = .00001;
	private float explodeTran = 1.0f;
	private float explodePenR = .0185f;
	private boolean explodeGraphic = false;
	private double bloomR;
	private float bloomT;
	private double distRate;
	private boolean alive;
	double xv = .001, yv = .001, xvX, yvX;
	int targetSpace;
	String message;
	
    // create a new particle with given parameters        
    public Particle() {
        
    }
         
    public void setup(Color color, double x, double y, double r, double sx, double sy, float tran, double td){
		this.color = color;
		this.x = x;
		this.y = y;
		this.r = r;
		this.startX = sx;
		this.startY = sy;
		this.tran = tran;
		this.totalDist = td;
	}

    // draw the particle
    public void draw() {
    	if (!alive)
			return;
		StdDraw.setPenRadius(.02);
		setColor(red, green, blue);
    	StdDraw.filledCircle(x, y, r, tran, color);
    }
    public void reset(int hex, int tarHex){
		Random rnd = new Random();

		startX = StdDraw.getHexX(hexVenture.hexes[hex].getColumn(), hexVenture.hexes[hex].getRow(), hex);
		startY = StdDraw.getHexY(hexVenture.hexes[hex].getColumn(), hexVenture.hexes[hex].getRow(), hex);
		
		//distPerc = 1;
		//distXPerc = 0;
		x = startX + (rnd.nextInt(5)+2) - (rnd.nextInt(5)+2);
		y = startY + (rnd.nextInt(5)+2) - (rnd.nextInt(5)+2);
		
		//tarXX = startX + (rnd.nextInt(50)+2) - (rnd.nextInt(50)+2); //StdDraw.getHexX(hexVenture.hexes[tarHex].getColumn(), hexVenture.hexes[tarHex].getRow(), tarHex);
		//tarYY = hexVenture.screenSizeH; //StdDraw.getHexY(hexVenture.hexes[tarHex].getColumn(), hexVenture.hexes[tarHex].getRow(), tarHex);
		
		//tarX = startX + (rnd.nextInt(50)+20) - (rnd.nextInt(50)+20);
		//tarY = startY + (rnd.nextInt(50)+20) - (rnd.nextInt(50)+20);
		
		//if (tarX == startX)
			//tarX++;
		//if (tarY == startY)
			//tarY++;
		
		//totalDistX = Math.sqrt(((tarXX-x) * (tarXX-x)) + ((tarYY-y) * (tarYY-y)));
		//totalDist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
		
		xv = (6.1 + (1 - 6.1) * rnd.nextDouble()) - (6.1 + (1 - 6.1) * rnd.nextDouble());
		yv = -(5.1 + (.5 - 5.1) * rnd.nextDouble());
		
		r = (20.1 + (10 - 20.1) * rnd.nextDouble());
		tran = 1.0f;
		//speedX = 0;
		//bloomR = r;
		//bloomT = .1f;
		//distRate = (0.020 + (.030 - 0.020) * rnd.nextDouble());
	}
	
	public void moveToTarget(){
		if (explodeGraphic){
			double perT = .035 * 1.0;
			double perP = .035 * .005;
			drawExplodeGraphic(perT, perP);
		}
		
		if (!alive)
			return;
		
    	//dist = Math.sqrt(((tarX-x) * (tarX-x)) + ((tarY-y) * (tarY-y)));
    	//distX = Math.sqrt(((tarXX-x) * (tarXX-x)) + ((tarYY-y) * (tarYY-y)));
    	
		//if (r <= 2){
			//color = Color.white;
		//}
		
		if (r <= .3){
			explodeGraphic = true;
    		explodeRadius = .0001;
			explodeTran = 0.6f;
			explodePenR = .005f;
			
    		//dist = 0;
    		//tran = 0;
    		//bloomR = 0;
    		//bloomT = 0;
    		//r = 0;
    		alive = false;
    		hasTarget = false;
		}
		
    	//if (distX < 1){
    		//explodeGraphic = true;
    		//explodeRadius = .0001;
			//explodeTran = 1.0f;
			//explodePenR = .005f;
			
    		//dist = 0;
    		//tran = 0;
    		//bloomR = 0;
    		//bloomT = 0;
    		//r = 0;
    		//alive = false;
    		//hasTarget = false;
    	//}
    	
    	//xv = tarX - x;
    	//yv = tarY - y;
    	//mag = Math.sqrt(xv * xv + yv * yv);
    	//xvX = tarXX - x;
    	//yvX = tarYY - y;
    	//magX = Math.sqrt(xvX * xvX + yvX * yvX);
    	
		if (r >= .3)
			r = r - .3;
		
		if (red > 0)
			red--;
		if (green > 0)
			green--;
		if (blue > 0)
			blue--;
		
    	//tran = (float) (distX / totalDistX);
    	//speed = (dist / totalDist);
    	//speedX += .1;
    	//if (tran > 1.0)
    		//tran = 1.0f;
    	//if (tran < 0.0)
    		//tran = 0.0f;
    	//xv = xv * speed / mag;
    	//yv = yv * speed / mag;
    	//xvX = xvX * speedX / magX;
    	//yvX = yvX * speedX / magX;

		//if (tran > .01)
			//tran = (float) (tran - .01);
		
    	if (xv > 0)
    		xv -= .05;
    	else if (xv < 0)
    		xv += .05;
    	
    	yv += .1;
    	//xvX *= distXPerc;
    	//yvX *= distXPerc;
    	
    	//if (distPerc > 0){
    		//distPerc -= distRate;
    		//distXPerc += distRate;
    	//}
    	
    	x += (xv);
    	y += (yv);
    	//x += (xv + xvX);
    	//y += (yv + yvX);
	}

	public void drawExplodeGraphic(double perT, double perP){
		//StdDraw.drawBloom(x, y, explodeRadius*.75, .5f, player);
		StdDraw.drawSelect(player, x, y, explodeRadius, explodeTran, explodePenR, "particle", 0);
		explodeRadius += 1;
        explodeTran -= perT;
        explodePenR -= perP;
        if (explodeTran <= 0.0)
        	explodeGraphic = false;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setColor(double r, double g, double b){
		red = (int)r;
		green = (int)g;
		blue = (int)b;
		
		if (red > 255)
			red = 255;
		if (green > 255)
			green = 255;
		if (blue > 255)
			blue = 255;
		
		color = new Color(red, green, blue);
	}
	
	public int getTargetPlanet() {
		return targetPlanet;
	}

	public void setTargetPlanet(int targetPlanet) {
		this.targetPlanet = targetPlanet;
	}

	public boolean isHasTarget() {
		return hasTarget;
	}

	public void setHasTarget(boolean hasTarget) {
		this.hasTarget = hasTarget;
	}

	public double getTargetX() {
		return targetX;
	}

	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}

	public double getStartX() {
		return startX;
	}

	public void setStartX(double startX) {
		this.startX = startX;
	}

	public double getStartY() {
		return startY;
	}

	public void setStartY(double startY) {
		this.startY = startY;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public boolean isExplodeGraphic() {
		return explodeGraphic;
	}

	public void setExplodeGraphic(boolean explodeGraphic) {
		this.explodeGraphic = explodeGraphic;
	}

	public double getBloomR() {
		return bloomR;
	}

	public void setBloomR(double bloomR) {
		this.bloomR = bloomR;
	}

	public float getBloomT() {
		return bloomT;
	}

	public void setBloomT(float bloomT) {
		this.bloomT = bloomT;
	}
	
}