package model;
// Клас митниці
public class Customs {
	// Координати митниці
	int x = 0;
	int y;
	// Булева змінна, яка відпвідає за те, чи пропускає в данний момент митниця вантажівку
	//(якщо горить червоний колір, то не пропускає, зелений - навпаки)
	boolean red = true;
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public boolean getRed() {
		return red;
	}

	public void setRed(boolean red) {
		this.red = red;
	}

	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
}
