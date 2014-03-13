package player;

/**
*	Wrapper for pieces on the Grid to simplify finding pointers and neighbors.
**/



public class Field{
	private Piece piece;
	private int x;
	private int y;
	private Grid grid;

	//Directions as length 2 integer arrays representing slope
	static final int[] LEFT = {-1,0};
	static final int[] RIGHT = {1,0};
	static final int[] UP= {0,1};
	static final int[] DOWN = {0,-1};
	static final int[] LEFT_UP = {-1,1};
	static final int[] LEFT_DOWN = {-1,-1};
	static final int[] RIGHT_UP = {1,1};
	static final int[] RIGHT_DOWN = {1,-1};
	static final int[][] DIRECTIONS = {LEFT,RIGHT,UP,DOWN,LEFT_UP,LEFT_DOWN,RIGHT_UP,RIGHT_DOWN};

	/**
	*	Constructor for Field object.
	*	@param x is x coordinate of field.
	*	@param y is y coordinate of field.
	*	@param g is Grid that the field is on.
	**/
	public Field( int x, int y, Grid g){
		piece = null;
		this.x = x;
		this.y = y;
		grid = g;
	}

	/**
	*	Gets the coordinates of the Field.
	*	@return is a length 2 integer array contain x and y.
	**/
	public int[] position(){
		int[] temp = {x,y};
		return temp;
	}
	/**
	*	Sets the piece of a Field.
	*	@param p is the Piece that is being placed in the field.
	**/
	public void setPiece(Piece p){
		piece = p;
	}

	/**
	*	Gets the piece a field contains.
	*	@return is a piece if Field is occupied.
	* 	@return is null if Field is unoccupied.
	**/
	public Piece getPiece(){
		return piece;
	}

	/**
	*	Returns whether or not the Field has a piece.
	*	@return true if occupied and false if unoccupied.
	**/
	public boolean hasPiece(){
		return piece == null;
	}

	/**
	*	Gets a Field in an adjacdent direction
	*	@param dir is a length 2 integer array defining direction.
	*	@return is a Field if there is a valid Field in the direction.
	* 	@return is null if out of bounds of Grid.
	**/
	public Field adjacent(int[] dir){
		return grid.get(x+dir[0],y+dir[1]);
	}

	/**
	*	Checks if Field as one neighbor.
	*	@return is a Field if there is a neighbor (occupied adjacent Field).
	* 	@return is null if there is not a neighbor.
	**/
	public Field neighbor(){
		for(int[] dir : DIRECTIONS){
			Field adj = adjacent(dir);
			if(adj!=null&&adj.getPiece()!=null){
				return adj;
			}
		}
		return null;
	}

	/**
	*	Finds closest piece in given direction.
	*	@param dir is a length 2 integer array defining direction.
	*	@return is a Field if there is an occupied Field in the given direction.
	* 	@return null if it hits the edge of the Grid.
	**/
	public Field getInDirection(int[] dir){
		if(grid.get(x+dir[0],y+dir[1]).getPiece()!=null){
			return grid.get(x+dir[0],y+dir[1]);
		}
		else{
			return grid.get(x+dir[0],y+dir[1]).left();
		}
	}
	public Field left(){
		return getInDirection(LEFT);
	}
	public Field right(){
		return getInDirection(RIGHT);
	}
	public Field up(){
		return getInDirection(UP);
	}
	public Field down(){
		return getInDirection(DOWN);
	}
	public Field left_up(){
		return getInDirection(LEFT_UP);
	}
	public Field right_up(){
		return getInDirection(RIGHT_UP);
	}
	public Field left_down(){
		return getInDirection(LEFT_DOWN);
	}
	public Field right_down(){
		return getInDirection(RIGHT_DOWN);
	}
	
}