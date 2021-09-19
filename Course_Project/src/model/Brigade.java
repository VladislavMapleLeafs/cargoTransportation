package model;
// Клас бригади
public class Brigade {
	// Булева змінна, яка відповідає за те, чи перебуває бригада в стані спокою
	boolean calm = true;
	// Змінна, яка відповідає за стани бригади
	int state;
	// Кількість ящиків, які потрібно розвантажити бригаді з певної вантажівки
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
