package model;
// ���� ���������
public class Truck {
	// ���������� ���������
	int x = -120;
	int y;
	// ʳ������ �����, �� ���������� ���������
	int boxCount;
	// ������ �����, �������, �� ��������� ��� ������ �������������
	boolean arrived = false;
	// ������ �����, �������, �� ��������� �������� � ����� ������ �� ��������� ����� �� �������
	boolean rides = false;
	// ������ �����, �������, �� ��������� �������� � ����� ������ �� ������� �� �������
	boolean rides2 = false;
	// ������ �����, �������, �� ������ ��������� �������� � �������
	boolean inCustom = false;
	
	public boolean getInCustom() {
		return inCustom;
	}

	public void setInCustom(boolean inCustom) {
		this.inCustom = inCustom;
	}

	public boolean getRides2() {
		return rides2;
	}

	public void setRides2(boolean rides2) {
		this.rides2 = rides2;
	}

	public boolean getRides() {
		return rides;
	}

	public void setRides(boolean rides) {
		this.rides = rides;
	}

	public boolean getArrived() {
		return arrived;
	}

	public void setArrived(boolean arrived) {
		this.arrived = arrived;
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
	
	public int getBoxCount() {
		return boxCount;
	}
	
	public void setBoxCount(int boxCount) {
		this.boxCount = boxCount;
	}
	
}
