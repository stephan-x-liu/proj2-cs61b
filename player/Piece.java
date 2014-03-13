package player;

public class Piece{

	private int color;
	private Field f;

	/**
	*	Constructor for Piece objects.
	*	@param color identifies color of Piece; 0 for black and 1 for white.
	*	@param Field is the field that the piece is in.
	**/
	public Piece(int color, Field f){
		this.color = color;
		this.f = f;
	}

	/**
	*	Constructs a Piece object without a Field.
	*	@param color identifies color of Piece; 0 for black and 1 for white.
	**/
	public Piece(int color){
		this(color,null);
	}

	/**
	*	Sets the field for a Piece object.
	*	@param new_field is the Field that the Piece is being moved to.
	**/
	public void setField(Field new_field){
		this.f = new_field;
	}

	/**
	*	Returns color of piece.
	*	@return int; 0 for black and 1 for white.
	**/
	public int color(){
		return color;
	}

	/**
	*	Returns Field of the Piece.
	*	@return Field object.
	**/
	public Field field(){
		return f;
	}
}