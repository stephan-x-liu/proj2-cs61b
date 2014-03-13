package player;

public class Field{
	private Piece piece;
	private int x;
	private int y;
	private Grid grid;
	private final int[] LEFT = {-1,0};
	private final int[] RIGHT = {1,0};
	private final int[] UP= {0,1};
	private final int[] DOWN = {0,-1};
	private final int[] LEFT_UP = {-1,1};
	private final int[] LEFT_DOWN = {-1,-1};
	private final int[] RIGHT_UP = {1,1};
	private final int[] RIGHT_DOWN = {1,-1};

	public Field( int x, int y, Grid g){
		piece = null;
		this.x = x;
		this.y = y;
		grid = g;
	}

	public int[] position(){
		return {x,y};
	}

	public setPiece(Piece p){
		piece = p;
	}

	public getPiece(){
		return piece;
	}

	public Field getInDirection(int[] dir){
		try{
			if(grid.get(x+dir[0],y+dir[1]).getPiece()!=null){
				return grid.get(x+dir[0],y+dir[1]);
			}
			else{
				return grid.get(x+dir[0],y+dir[1]).left();
			}
		}
		catch(NullPointerException e){
			return null;
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