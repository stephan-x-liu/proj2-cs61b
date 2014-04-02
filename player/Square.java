package player;

/**
*  Wrapper for pieces on the Grid to simplify finding pointers and neighbors.
**/



public class Square{
  
  //Set to "simple" for short toString, "locations" for pieces with coordinates, and "complex" for debug info.
  private static final String DEBUG = "locations";
  
  static final int NONE = 2;
  static final int WHITE = 1;
  static final int BLACK = 0;
  private int piece;
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
  *  Constructs a new Square object at location (x,y) on game
  *  board g, unoccupied by any players.
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

  /**
  *  Constructs a new Square object at location (x,y) on game
  *  board g, occupied by the indicated player.
  *  @param x is x coordinate of Square.
  *  @param y is y coordinate of Square.
  *  @param piece is the integer representation of the player
  *  who owns this Square.
  *  @param g is Grid that the Square is on.
  **/
  public Square( int x, int y, int piece, Grid g){
    this.piece = piece;
    this.x = x;
    this.y = y;
    grid = g;
  }

  /**
  *  Gets the coordinates of the Square on the game board.
  *  @return is a length-2 integer array. The first element in
  *  the array is the x-coordinate of the Square, the second
  *  element is the y-coordinate of the Square.
  **/
  protected int[] position(){
    int[] temp = {x,y};
    return temp;
  }
  /**
  *  Sets the Square to be occupied by a given player.
  *  @param p is the integer representation of the player who
  *  owns this Square (0 is black, 1 is white)
  **/
  protected void setPiece(int p){
    piece = p;
  }

  /**
  *  Sets the Square so that it is unoccupied (not owned by
  *  black or white)
  **/
  public void removePiece(){
    piece = NONE;
  }

  /**
  *  Gets the player who owns the Square.
  *  @return is an int representing the player who owns the
  *  Square (0 for black, 1 for white, 2 if the Square is
  *  unoccupied)
  **/
  public int getPiece(){
    return piece;
  }

  /**
  *  Returns true if Square is occupied by white or black, false
  *  if Square is unoccupied.
  *  @return true if occupied and false if unoccupied.
  **/
  public boolean hasPiece(){
    return !(piece == NONE);
  }

  public Square[] connections(int[] dir){
    Square[] temp = new Square[8];
    int connect = 0;
    for(int[] d : DIRECTIONS){
      if((d[0]!=dir[0] || d[1]!=dir[1])){
        if(getInDirection(d)!=null&&getInDirection(d).getPiece()==piece)
          temp[connect] = getInDirection(d);
        else
          temp[connect] = null;
      }
      connect++;
    }
    return temp;
  }

  public boolean alreadyInNetwork(Square[] network){
    for(Square s: network){
      if(s!=null && this.samePlace(s)){
        return true;
      }
    }
    return false;
  }

  /**
  *  Gets a Square in an adjacent direction
  *  @param dir is a length 2 integer array defining direction.
  *  @return is a Square if there is a valid Square in the direction.
  *  @return is null if out of bounds of Grid.
  **/
  public Square adjacent(int[] dir){
    return grid.get(x+dir[0],y+dir[1]);
  }

  /**
  *  Checks if Square has one neighbor.
  *  @return is a Square if there is a neighbor (occupied adjacent Square).
  *  @return is null if there is not a neighbor.
  **/
  public Square[] neighbor(int color){
    Square[] neighbors = new Square[8];
    int count = 0;
    for(int[] dir : DIRECTIONS){
      Square adj = adjacent(dir);
      if(adj!=null&&adj.getPiece()==color){
        neighbors[count] = adj;
        count++;
      }
    }
    return neighbors;
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

  public boolean samePlace(Square s){
    return s.position()[0] == x && s.position()[1]==y;
  }

  /**
  *  Finds closest piece in given direction.
  *  @param dir is a length 2 integer array defining direction.
  *  @return is a Square if there is an occupied Square in the given direction.
  *  @return null if it hits the edge of the Grid.
  **/
  public Square getInDirection(int[] dir){
    if(grid.get(x+dir[0],y+dir[1])==null){
      return null;
    }
    if(grid.get(x+dir[0],y+dir[1]).hasPiece()){
      return grid.get(x+dir[0],y+dir[1]);
    }
    else{
      return grid.get(x+dir[0],y+dir[1]).getInDirection(dir);
    }
  }

  /**
   * Returns a string that includes the information about the Square--
   * the owner of the square (or if it is unoccupied), the
   * location, and the network information.
   * @return a string representation of the Square.
   **/

   public String toString(){
    String pieceStr = " ";
    if(piece == WHITE){
      pieceStr = "W";
    }
    else if(piece == BLACK){
      pieceStr = "B";
    }
    if(DEBUG == "locations"){
      return pieceStr+" ( "+ x + ", "+y+")";
    }
    else if(DEBUG == "simple"){
      return pieceStr;
    }
    else{
      return pieceStr+":"+blackNetworks/2+":"+blackPotential+":"+whiteNetworks/2+":"+whitePotential;
    }
  }

  /**
   * Returns a string that includes the Square's location and
   * owner (or if it is unoccupied).
   * @return a string representation of the Square.
   **/

  public String simpleToString(){
    String s = "("+x+","+y+")-";
    if (piece==NONE){
      s+="N";
    }
    if (piece==WHITE){
      s+="W";
    }
    if (piece==BLACK){
      s+="B";
    }
    return s;
  }

}
