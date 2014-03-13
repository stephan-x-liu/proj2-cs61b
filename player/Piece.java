package player;

public class Piece{

	private int color;
	private Field f;

	public Piece(int color, Field f){
		this.color = color;
		this.f = f;
	}

	public int color(){
		return color;
	}

	public Field field(){
		return f;
	}
}