package player;

/**
*  Wrapper for pieces on the Grid to simplify finding pointers and neighbors.
**/



public class Square{
  private int piece;
  static final int NONE = 2;
  static final int white = 1;
  static final int black = 0;
  private int x;
  private int y;
  private Grid grid;
  private int black_potential;
  private int white_potential;
  private int black_networks;
  private int white_networks;

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
  *  Constructor for Square object.
  *  @param x is x coordinate of Square.
  *  @param y is y coordinate of Square.
  *  @param g is Grid that the Square is on.
  **/
  public Square( int x, int y, Grid g){
    piece = null;
    this.x = x;
    this.y = y;
    grid = g;
    black_networks = 0;
    white_networks = 0;
    black_potential = 0;
    white_potential = 0;
  }

  /**
  *  Gets the coordinates of the Square.
  *  @return is a length 2 integer array contain x and y.
  **/
  public int[] position(){
    int[] temp = {x,y};
    return temp;
  }
  /**
  *  Sets the piece of a Square.
  *  @param p where 0 is black and 1 is white.
  **/
  public void setPiece(int p){
    piece = p;
  }

  /**
  *  Removes the piece of a Square.
  **/
  public void removePiece(){
    piece = null;
  }

  /**
  *  Gets the piece a Square contains.
  *  @return is an int representing piece if Square is occupied.
  *   @return is null if Square is unoccupied.
  **/
  public int getPiece(){
    return piece;
  }

  /**
  *  Returns whether or not the Square has a piece.
  *  @return true if occupied and false if unoccupied.
  **/
  public boolean hasPiece(){
    return piece != NONE;
  }

  /**
  *  Gets a Square in an adjacdent direction
  *  @param dir is a length 2 integer array defining direction.
  *  @return is a Square if there is a valid Square in the direction.
  *   @return is null if out of bounds of Grid.
  **/
  public Square adjacent(int[] dir){
    return grid.get(x+dir[0],y+dir[1]);
  }

  /**
  *  Checks if Square as one neighbor.
  *  @return is a Square if there is a neighbor (occupied adjacent Square).
  *   @return is null if there is not a neighbor.
  **/
  public Square neighbor(int color){
    for(int[] dir : DIRECTIONS){
      Square adj = adjacent(dir);
      if(adj!=null&&adj.getPiece()==color){
        return adj;
      }
    }
    return null;
  }

  /**
  *  Finds closest piece in given direction.
  *  @param dir is a length 2 integer array defining direction.
  *  @return is a Square if there is an occupied Square in the given direction.
  *   @return null if it hits the edge of the Grid.
  **/
  public Square getInDirection(int[] dir){
    if(grid.get(x+dir[0],y+dir[1]).hasPiece()){
      return grid.get(x+dir[0],y+dir[1]);
    }
    else if(grid.get(x+dir[0],y+dir[1])==null){
      return null;
    }
    else{
      return grid.get(x+dir[0],y+dir[1]).getInDirection(dir);
    }
  }
}