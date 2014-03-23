package player;

/**
*  Wrapper for pieces on the Grid to simplify finding pointers and neighbors.
**/



public class Square{
  
  //Set to "simple" for short toString, "complex" for debug info.
  private static final String DEBUG = "complex";
  
  private int piece;
  static final int NONE = 2;
  static final int WHITE = 1;
  static final int BLACK = 0;
  private int x;
  private int y;
  private Grid grid;
  private int blackPotential;
  private int whitePotential;

  //White and black networks are both 2x the actual networks because
  //of the way the tracing algorithm works. We simply account for this when
  //we return our results in order to minimize computing time.
  private int blackNetworks;
  private int whiteNetworks;


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
    piece = NONE;
    this.x = x;
    this.y = y;
    grid = g;
  }

  public Square( int x, int y, int piece, Grid g){
    this.piece = piece;
    this.x = x;
    this.y = y;
    grid = g;
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
    piece = 0;
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
    return (piece == 0);
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
  *  Checks if Square has one neighbor.
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
   * Reset the white/black potential and network counts for this square
   **/
  public void resetPN(){
    blackPotential = 0;
    whitePotential = 0;
    blackNetworks = 0;
    whiteNetworks = 0;

  }
  public void addBlackPotential(){
    blackPotential++;
  }

  public void addWhitePotential(){
    whitePotential++;
  }

  public void addBlackNetwork(){
    blackNetworks++;
  }

  public void addWhiteNetwork(){
    whiteNetworks++;
  }

  public int getBlackPotential(){
    return blackPotential;
  }

  public int getWhitePotential(){
    return whitePotential;
  }

  //Count for black networks always 2x what it should be because we record
  //the network from both ends.
  public int getBlackNetworks(){
    return blackNetworks/2;
  }

  //Count for white networks always 2x what it should be because we record
  //the network from both ends.
  public int getWhiteNetworks(){
    return whiteNetworks/2;
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

  /**
   * Just a normal to string method
   * @return a string representing piece contents including: color, potential and actual networks
   **/
  public String toString(){
    String pieceStr = " ";
    if(piece == WHITE){
      pieceStr = "W";
    }else if(piece == BLACK){
      pieceStr = "B";
    }
    if(DEBUG == "simple"){
      return pieceStr;
    }else{
      return pieceStr+":"+blackNetworks/2+":"+blackPotential+":"+whiteNetworks/2+":"+whitePotential;
    }
  }
}
