package model;
//  лас денного циклу
public class Day {
	//  оординати сонц€ (чи м≥с€ц€)
	int x;
	int y;
	// Ѕулева зм≥нна, визначаЇ зараз день чи н≥ч
	boolean sun = true;
	
	public boolean getSun() {
		return sun;
	}

	public void setSun(boolean sun) {
		this.sun = sun;
	}

	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

}
