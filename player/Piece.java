package player;

public class Piece{

	private int color;
	private int x;
	private int y;
	private Field f;

	public Piece(int color, int x, int y, Field f){
		this.x = x;
		this.y = y;
		this.color = color;
		this.f = g;
	}

	public int color(){
		return color;
	}

	public int[] position(){
		return {x,y};
	}

	public Field field(){
		return f;
	}
}