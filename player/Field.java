package player;

public class Field{
	private Piece piece;
	private int x;
	private int y;
	private Grid grid;
	static final int[] LEFT = {-1,0};
	static final int[] RIGHT = {1,0};
	static final int[] UP= {0,1};
	static final int[] DOWN = {0,-1};
	static final int[] LEFT_UP = {-1,1};
	static final int[] LEFT_DOWN = {-1,-1};
	static final int[] RIGHT_UP = {1,1};
	static final int[] RIGHT_DOWN = {1,-1};
	static final int[][] DIRECTIONS = {LEFT,RIGHT,UP,DOWN,LEFT_UP,LEFT_DOWN,RIGHT_UP,RIGHT_DOWN};

	public Field( int x, int y, Grid g){
		piece = null;
		this.x = x;
		this.y = y;
		grid = g;
	}

	public int[] position(){
		int[] temp = {x,y};
		return temp;
	}

	public void setPiece(Piece p){
		piece = p;
	}

	public Piece getPiece(){
		return piece;
	}

	public Field adjacent(int[] dir){
		try{
			return grid.get(x+dir[0],y+dir[1]);
		}
		catch(NullPointerException e){
			return null;
		} 
	}

	public Field neighbor(){
		for(int[] dir : DIRECTIONS){
			Field adj = adjacent(dir);
			if(adj!=null&&adj.getPiece()!=null){
				return adj;
			}
		}
		return null;
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