package model;
// ���� �������
public class Brigade {
	// ������ �����, ��� ������� �� ��, �� �������� ������� � ���� ������
	boolean calm = true;
	// �����, ��� ������� �� ����� �������
	int state;
	// ʳ������ �����, �� ������� ������������ ������ � ����� ���������
	int boxCount;

	public int getState() {
		return state;
	}

	public int getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(int boxCount) {
		this.boxCount = boxCount;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean getCalm() {
		return calm;
	}

	public void setCalm(boolean calm) {
		this.calm = calm;
	}
	
}
