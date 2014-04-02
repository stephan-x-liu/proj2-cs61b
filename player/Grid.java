package player;
import java.lang.Math;
import list.*;

public class Grid{
  //These fields must be default protected so we can test stuff properly

	protected boolean add = true;
	private Square[][] board;
	protected Square[] blackSquares = new Square[10];
	protected int blackSquareCount = 0;
	protected Square[] whiteSquares = new Square[10];
	protected int whiteSquareCount = 0;
	public static final int DIMENSION = 8;
	static final int BLACK = 0;
	static final int WHITE = 1;
	static final int NONE = 2;
	static final int[][] DIRECTIONS = Square.DIRECTIONS;


  /**
  *  Grid constructor that creates a new empty game board (where all
  *  squares on the board are unoccupied).
  **/
  public Grid(){
  	board = new Square[DIMENSION][DIMENSION];
  	for(int i = 0; i < DIMENSION; i ++){
  		for(int j = 0; j < DIMENSION; j++){
  			board[i][j] = new Square(i,j,this);
  		}
  	}
  	blackSquareCount = 0;
  	whiteSquareCount = 0;
  }

  /**
    *  Grid constructor that creates a game board based on a 2D array.
    *  @param model is a 2D array that models the board (tells whether
    *  each Square is owned by black, white, or unoccupied).
    **/  
  public Grid(int[][] model){
  	board = new Square[DIMENSION][DIMENSION];
  	blackSquareCount = 0;
  	whiteSquareCount = 0;
  	for(int i = 0; i < DIMENSION; i ++){
  		for(int j = 0; j < DIMENSION; j++){
  			board[i][j] = new Square(i,j,this);
         //For every single x and y value, check if it's black or white, then set the pieces.
  			if(model[i][j] == BLACK){
  				blackSquares[blackSquareCount] = board[i][j];
  				blackSquareCount++;
  			}
  			if(model[i][j] == WHITE){
  				whiteSquares[whiteSquareCount] = board[i][j];
  				whiteSquareCount++;
  			}
  			board[i][j].setPiece(model[i][j]);
  		}
  	}
  }

  /**
  *  Grid constructor that creates a game board based on a one-line string.
  *  @param pieces is a 64-character string that models the board (each
  *  character tells whether a Square is owned by black, white, or
  *  unoccupied).
  **/
  public Grid(String pieces){
		blackSquareCount = 0;
		whiteSquareCount = 0;
	    pieces = pieces.replaceAll("W", Integer.toString(WHITE));
	    pieces = pieces.replaceAll("B", Integer.toString(BLACK));
	    pieces = pieces.replaceAll("\\.", Integer.toString(NONE));
		board = new Square[DIMENSION][DIMENSION];
	    char[] charArray = pieces.toCharArray();
		Square s;
		int i = 0;
	 	for (int y = 0; y < DIMENSION; y++){
	 		for (int x = 0; x < DIMENSION; x++){
	 			s = new Square(x, y, Integer.parseInt(String.valueOf(charArray[i])), this);
	 			board[x][y] = s;
	 			if (charArray[i] == WHITE){
	 				whiteSquares[whiteSquareCount] = s;
	 				whiteSquareCount++;
	 			}
	 			if (charArray[i] == BLACK){
	 				blackSquares[blackSquareCount] = s;
	 				blackSquareCount++;
	 				if (blackSquareCount==10){
	 					add = false;
	 				}
	 			}
	    		i++;
	 		}
	 	}
 	}

  /**
  *  Returns a new Grid that is a copy of this Grid. All Squares on
  *	 the new grid are owned by the same player as the corresponding
  *  Squares on this current Grid.
  *  @return a grid that is a an exact copy of the current instance.
  **/
  protected Grid cloneGrid(){
    Grid g = new Grid();
   	for (int x = 0; x < DIMENSION; x++){
   		for (int y = 0; y < DIMENSION; y++){
   			g.set(x,y,get(x,y).getPiece());
   		}
   	}
   	return g;
  }

  /**
  *  Returns the Square at the coordinates give. If the coordinates
  *  are invalid (x or y is greater than 7), it returns null.
  *  @param x is x coordinate
  *  @param y is the y coordinate
  *  @return the Square at the given position.
  **/
  protected Square get(int x, int y){
 	  try {
 		  return board[x][y];
 	  } catch(ArrayIndexOutOfBoundsException e){
 		  return null;
 	  }
  }

  /**
  *  Sets the Square at location (x,y) on the game board to belong
  *  to the color indicated.
  *  @param x is the x-coordinate of Square to be set
  *  @param y is the y coordinate of Square to be set
  *  @param color is the integer representation of who the Square
  *  should be set to belong to
  **/
  protected void set(int x, int y, int color){
  	try {
  		get(x,y).setPiece(color);
  		if (color==WHITE){
  			whiteSquares[whiteSquareCount]=get(x,y);
  			whiteSquareCount++;
  		}
  		if (color==BLACK){
  			blackSquares[blackSquareCount]=get(x,y);
  			blackSquareCount++;
  			if (blackSquareCount==10){
  				add = false;
  			}
  		}
  	} catch(ArrayIndexOutOfBoundsException e){
  		return ;
  	}

  }

  /**
  *  Returns number of goal zones occupied for a given color.
  *  @param color is integer representation of the player whose goal
  *  zones are in question.
  *  @return 0 for no goal zones occupied, 1 for 1 goal zone occupied,
  *  and 2 for 2 goal zones occupied.
  **/
  protected int getGoalZones(int color){
  	int goals = 0;
  	if (color==BLACK){
  		for (int x0 = 0; x0 < DIMENSION; x0++){
  			if (get(x0,0).getPiece() == BLACK){
  				goals++;
  				break;
  			}
  		}
  		for (int x7 = 0; x7 < DIMENSION; x7++){
  			if (get(x7,7).getPiece() == BLACK){
  				goals++;
  				break;
  			}
  		}
  		return goals;
  	}
  	else{
  		for (int y0 = 0; y0 < DIMENSION; y0++){
  			if (get(0,y0).getPiece() == WHITE){
  				goals++;
  				break;
  			}
  		}
  		for (int y7 = 0; y7 < DIMENSION; y7++){
  			if (get(7,y7).getPiece() == WHITE){
  				goals++;
  				break;
  			}
  		}
  		return goals;
  	}
  }

  /**
  *  Updates the game board after a move is made.
  *  @param move is the Move that is being performed.
  *  @param color is the integer representation of the player
  *  who is performing the move.
  **/
  protected void makeMove(Move move, int color){
  	if ((move != null)){
  		if (move.moveKind == Move.STEP){
  			get(move.x2, move.y2).removePiece();
  			get(move.x1,move.y1).setPiece(color);
  			for (int a=0; a<10; a++){
  				if (color==BLACK){
  					if (blackSquares[a]==get(move.x2, move.y2)){
  						blackSquares[a] = get(move.x1, move.y1);
  						break;
  					}
  				}
  				if (color==WHITE){
  					if (whiteSquares[a]==get(move.x2, move.y2)){
  						whiteSquares[a] = get(move.x1, move.y1);
  						break;
  					}
  				}
  			}
  		}
  		if (move.moveKind==Move.ADD) {
  			set(move.x1, move.y1, color);
  		}
  	}
  	updateNetworkList();
  }
  /**
  *  Returns a list of valid moves for a given player to be considered
  *  for Alpha-Beta Pruning.
  *  @param color is the integer representation of the player whose
  *  valid moves are calculated
  *  @return an array of valid Moves for the given player
  **/
  protected Move[] validMoves(int color){
  	Move[] validMoves = new Move[300];
  	int moveIndex = 0;
  	Move move;
  	if (add){
  		for (int y = 0; y < DIMENSION; y++){
  			for (int x = 0; x < DIMENSION; x++){
  				move = new Move(x, y);
  				if (isValidMove(move, color)){
  					validMoves[moveIndex] = move;
  					moveIndex++;
  				}
  			}
  		}
  	}
  	else {
  		Square[][] squares = new Square[3][10];
  		NetworkObject[] goals = goalNetworks(color);
  		NetworkObject longest = null;
  		squares[1] = whiteSquares;
  		squares[0] = blackSquares;
  		int count = 0;
  		Square[] movable = new Square[10];
  		for(Square s: squares[color]){
  			boolean inNetwork = false;
  			for(NetworkObject n: goals){
  				if(n!=null && (longest==null || n.length>longest.length))
  					longest = n;
  			}
  		}
  		for(Square s: squares[color]){
  			if(longest!=null&&s.alreadyInNetwork(longest.network)==false){
  				movable[count] = s;
  				count++;
  			}
  			else if(longest == null){
  				movable[count] = s;
  				count++;
  			}
  		}
  		if(count==0&&longest.length>9){
  			try{
  				movable[0] = longest.network[9];
  				movable[1] = longest.network[8];
  				movable[2] = longest.network[7];
  				movable[3] = longest.network[6];  
  			}
  			catch(NullPointerException e){

  			}

  		}
  		for (Square add: movable){
  			if (add != null){
  				for (int y = 0; y < DIMENSION; y++){
  					for (int x = 0; x < DIMENSION && moveIndex < 300; x++){
  						move = new Move(x, y, add.position()[0], add.position()[1]);
  						if (isValidMove(move, color)){
  							validMoves[moveIndex] = move;
  							moveIndex++;
  						}  
  					}
  				}
  			}
  		}
  	}
  	return validMoves;
  }


  /**
  *  Checks if a move is valid according to the rules of Network.
  *  @param move is the move being checked.
  *  @param color is the integer representation of the player
  *  trying to make the move.
  *  @return returns true if the move is valid, false if the move
  *  breaks a game rule.
  **/
  protected boolean isValidMove(Move move, int color){
  	if(move == null){
  		return false;
  	}
  	if (get(move.x1, move.y1).hasPiece()){
  		return false;
  	}
    if (move.x1 == 0){ //checking four corners 
    	if (move.y1 == 0 || move.y1 == 7){
    		return false;
    	}
    }
    if (move.y1 == 0){
    	if (move.x1 == 0 || move.x1 == 7){
    		return false;
    	}
    }
    if (color == WHITE){ //checking opponent goals
    	if (move.y1 == 0 || move.y1 == 7){
    		return false;
    	}
    }
    if (color == BLACK){
    	if (move.x1 == 0 || move.x1 == 7){
    		return false;
    	}
    }
    if (move.moveKind==Move.ADD){
    	Square[] neighbors = get(move.x1, move.y1).neighbor(color);
    	if(!add){
    		return false;
    	}
    	if(neighbors[1]!=null){
    		return false;
    	}
    	if (neighbors[0] != null){
    		if (neighbors[0].neighbor(color)[0] != null){
    			return false;
    		}
    	}
    }
    if (move.moveKind==Move.STEP){
    	if (add){
    		return false;
    	}
    	if (get(move.x2, move.y2).getPiece() != color){
    		return false;
    	}  
      if (move.x1==move.x2 && move.y1==move.y2){ //stepping to same square
      	return false;
      }
      get(move.x2, move.y2).removePiece();
      Square[] neighbors = get(move.x1, move.y1).neighbor(color);
      if(neighbors[1]!=null){
      	get(move.x2, move.y2).setPiece(color);
      	return false;
      }
      if (neighbors[0] != null){
      	if (neighbors[0].neighbor(color)[0] != null){
      		get(move.x2, move.y2).setPiece(color);
      		return false;
      	}
      }
      get(move.x2, move.y2).setPiece(color);
    }
    return true;
  }

  /**
  *  Finds the length of the longest network starting from anywhere on the board.
  *  @param color is the integer representation of the color
  *  @return integer length of longest network.
  **/
  protected int maxNetworkLength(int color){
  	int length = 0;
  	if(color == BLACK){
  		for(int i = 0; i < blackSquareCount; i++){
  			int temp = networkLength(blackSquares[i]);
  			if(temp>length){
  				length = temp;
  			}
  		}
  	}
  	if(color == WHITE){
  		for(int i = 0; i < whiteSquareCount; i++){
  			int temp = networkLength(whiteSquares[i]);
  			if(temp>length){
  				length = temp;
  			}
  		}
  	}
  	return length;
  }

  /**
  *  Returns a list of networks starting from the goal.
  *  @param color is the integer representation of the color
  *  @return list of NetworkObject objects. 
  **/
  protected NetworkObject[] goalNetworks(int color){
  	NetworkObject[] temp = new NetworkObject[10];
  	int count = 0;
  	if(color==WHITE){
  		for(int i = 1; i<DIMENSION-1; i++){
  			if(get(0,i).getPiece()==WHITE){
  				temp[count] = getNetwork(get(0,i));
  				count++;
  			}
  		}
  	}
  	else{
  		for(int i = 1; i<DIMENSION-1; i++){
  			if(get(i,0).getPiece()==BLACK){
  				temp[count] = getNetwork(get(i,0));
  				count++;
  			}
  		}
  	}
  	return temp;
  }

  /**
  *  Find network from a square.
  *  @param current is the starting square of the network.
  *  @return a NetworkObject object
  **/
  protected NetworkObject getNetwork(Square current){
  	Square[] network = new Square[10];
  	int length = 0;
  	int[] temp = {0,0};
  	NetworkObject n = findNetwork(current,network,length,temp);
  	return n;
  }

  /**
  *  Finds out whether or not there is a winning network for a color.
  *  @param color is the integer representation of the color
  *  @return boolean for whether or not there is a winning network.
  **/
  protected boolean hasWinningNetwork(int color){
  	int goal1 = 1;
  	int goal2 = 0;
  	int goal3 = 0;

  	if(color == BLACK){
  		for(int i = 1; i < DIMENSION-1; i++){
  			NetworkObject n = getNetwork(get(i,0));
  			if(n.length>=6){
  				for(int k = 1; k<n.length; k++){
  					if(n.network[k].position()[1]==7)
  						return true;
  				}
  			}
  		}
  	}
  	else{

  		for(int i = 1; i < DIMENSION-1; i++){
  			NetworkObject n = getNetwork(get(0,i));
  			if(n.length>=6){
  				for(int k = 1; k<n.length; k++){
  					if(n.network[k].position()[0]==7)
  						return true;
  				}
  			}
  		}
  	}
  	return false;
  }

  /**
  *  Finds length of a network from a specific square.
  *  @param current is the starting square of the network.
  *  @return integer length of network.
  **/
  protected int networkLength(Square current){
  	Square[] network = new Square[10];
  	int length = 0;
  	int[] temp = {0,0};
  	NetworkObject n = findNetwork(current,network,length,temp);
  	return n.length;
  }

  /**
  *  Deep copy of a network array.
  *  @param network is array of squares that represents a network.
  *  @return array of squares that represents a network.
  **/
  private Square[] copyNetworkArray(Square[] network){
  	Square[] temp = new Square[10];
  	for(int i = 0; i<10; i++){
  		temp[i] = network[i];
  	}
  	return temp;
  }

  /**
  *  Private function used within grid class for all the Network-related functions.
  *  @param current is square of where to start looking for the network
  *  @param network is length 10 array of squares representing a network
  *  @param length is length of current network
  *  @param prev_dir is a length two integer array representing the direction the network is coming from. 
  *  @return a NetworkObject object
  **/
  private NetworkObject findNetwork(Square current, Square[] network, int length, int[] prev_dir){
  	network[length] = current;
  	length++;
  	NetworkObject longest = new NetworkObject(network,length);
  	Square[] connections = current.connections(prev_dir);
  	if(current.getPiece()==WHITE && current.position()[0]==0){
  		connections[2]=null;
  		connections[3]=null;
  	}

  	if(current.getPiece()==BLACK && current.position()[1]==0){
  		connections[0]=null;
  		connections[1]=null;
  	}
  	if(current.position()[1]==7||current.position()[0]==7){
  		return longest;
  	}

  	for(int i = 0; i < connections.length; i++){
  		Square temp = connections[i];
  		if(temp!=null&&temp.getPiece()==WHITE&&temp.position()[0]==0){
  			connections[i]=null;
  		}
  		if(temp!=null&&temp.getPiece()==BLACK&&temp.position()[1]==0){
  			connections[i]=null;
  		}
  		temp = connections[i];
  		if(temp!=null && temp.alreadyInNetwork(network)==false){
  			NetworkObject s = findNetwork(temp,copyNetworkArray(network),length, DIRECTIONS[i]);
  			if(s.length>longest.length){
  				longest = new NetworkObject(s.network,s.length);
  			}
  		}  
  	}
  	return longest;

  }

  /**
  *  Used for grid evaluate.
  *  If there are more than 2 squares in either goalzone, it returns a negative multiplier.
  *  @param color is the integer representation of the color.
  *  @return integer multiplier used for eval.
  **/
  private int squaresInGoalZones(int color){
  	int count = 0;
  	if (color==BLACK){
  		for (int x0 = 0; x0 < DIMENSION; x0++){
  			if (get(x0,0).getPiece() == BLACK){
  				count++;
  			}
  		}
  		if(count>2){
  			return 2 - count;
  		}
  		count = 0;
  		for (int x7 = 0; x7 < DIMENSION; x7++){
  			if (get(x7,7).getPiece() == BLACK){
  				count++;
  			}
  		}
  		if(count>2){
  			return 2 - count;
  		}
  	}
  	else{
  		for (int y0 = 0; y0 < DIMENSION; y0++){
  			if (get(0,y0).getPiece() == WHITE){
  				count++;
  			}
  		}
  		if(count>2){
  			return 2 - count;
  		}
  		count = 0;
  		for (int y7 = 0; y7 < DIMENSION; y7++){
  			if (get(7,y7).getPiece() == WHITE){
  				count++;
  			}
  		}
  		if(count>2){
  			return 2 - count;
  		}
  	}
  	return 1;
  }

  /**
  *  The following methods are all for grid evaluation.
  **/
  /**
   * Takes in the friendly potential networks and weighs it for evaluating.
   * @param count is the number of friendly potential networks on a square
   * @return returns a weighted version of this potential meant for adding to eval sum
   **/
  private int fComputePotential(int count){
    //Heavy bias for overlap because we can make great networks out of overlap.
    //We subtract one because potential doesn't really help us unless it's overlapping.
    //We'll see if that's good.
  	if(count == 0){
  		return 0;
  	}
  	return (count-1)*(count-1);
  }

  /**
   * Takes in the friendly networks crossing and weighs it for evaluating.
   * @param count is the number of friendly  networks over a square
   * @return returns a weighted version of this network count meant for adding to eval sum
   **/
  private int fComputeNetwork(int count){
    //Squared makes heavy bias for overlap.
  	return count*count;
  }

  /**
   * Takes in the hostile potential networks and weighs it for evaluating.
   * @param count is the number of hostile potential networks on a square
   * @return returns a weighted version of this potential meant for adding to eval sum
   **/
  private int eComputePotential(int count){
    //Negative because it's bad, bias towards overlap because overlap bad.
  	return -4* count*count;
  }

  /**
   * Takes in the number of hostile networks crossing this square  and weighs it for evaluating.
   * @param count is the number of hostile networks crossing a square
   * @return returns a weighted version of this newtork count meant for adding to eval sum
   **/
  private int eComputeNetwork(int count){
    //The less networks the better
  	return -8* count*count;
  }


  /**
  *  Grid evaluate funciton. Computes a "value" for the grid to allow us to rank measure quality.
  *  @param friendly is the integer representation of the color.
  *  @return integer value of the current gamestate.
  **/
  protected int evaluate(int friendly){
  	int enemy;
  	int fComputedPotential = 0;
  	int fComputedNetwork = 0;
  	int eComputedPotential = 0;
  	int eComputedNetwork = 0;
  	updateNetworkList();
  	if(friendly == Square.BLACK){
  		enemy = Square.WHITE;
  	}else{
  		enemy = Square.BLACK;
  	}
    //For all squares
  	for (int y = 0; y < DIMENSION; y++){
  		for (int x = 0; x < DIMENSION; x++){
  			Square sq = get(x, y);
        //We need to flip our friendly and hostile functions depending on color
  			if(friendly == Square.BLACK){
  				fComputedPotential += fComputePotential(sq.getBlackPotential());
  				eComputedPotential += eComputePotential(sq.getWhitePotential());
  				fComputedNetwork += fComputeNetwork(sq.getBlackNetworks());
  				eComputedNetwork += eComputeNetwork(sq.getWhiteNetworks());
  			}
  			if(friendly == Square.WHITE){
  				eComputedPotential += eComputePotential(sq.getBlackPotential());
  				fComputedPotential += fComputePotential(sq.getWhitePotential());
  				eComputedNetwork += eComputeNetwork(sq.getBlackNetworks());
  				fComputedNetwork += fComputeNetwork(sq.getWhiteNetworks());
  			}
  		}
  	}

    //After evaluating the different squares, factor in networks.
  	NetworkObject[] fNetworks = goalNetworks(friendly);
  	NetworkObject[] eNetworks = goalNetworks(enemy);

  	int flongest = 0;
  	int fgoals = 0;
  	int egoals = 0;
  	int elongest = 0;
  	while(fNetworks[fgoals]!=null){
  		if(flongest<fNetworks[fgoals].length)
  			flongest = fNetworks[fgoals].length;
  		fgoals++;
  	}

  	while(eNetworks[egoals]!=null){
  		if(elongest<eNetworks[egoals].length)
  			elongest = eNetworks[egoals].length;
  		egoals++;
  	}

  	int emultiplier = maxNetworkLength(enemy)*(elongest-1);
  	int multiplier = getGoalZones(friendly)*squaresInGoalZones(friendly) *(maxNetworkLength(friendly) + 2*(flongest));
    //Add it all together
  	return multiplier*(fComputedPotential+fComputedNetwork)+emultiplier*(eComputedPotential+eComputedNetwork);

  }

  /**
  *  Resets the black/white potential and networks for all squares.
  **/
  protected void resetSquaresPN(){
    for (int x = 0; x < DIMENSION; x++){
      for (int y = 0; y < DIMENSION; y++){
        get(x, y).resetPN();
      }
    }
  }

  /**
  *  Updates network-related values in each square of a grid.
  **/
  protected void updateNetworkList(){
    int i = 0;
    Square mainSquare = blackSquares[i];
    SList squaresToChange;
    Square curSquare;

    resetSquaresPN();

    //Use while instead of for so we can stop at null instead of at end.
    while(mainSquare != null){
      //For every mainSquare
      for(int[] dir : DIRECTIONS){

        //Reset varialbes, since we're in a new direction (a new path)
        squaresToChange = new SList();
        curSquare = mainSquare.adjacent(dir);
        squaresToChange.insertBack(mainSquare);

        //Iterate through all squares on the path.
        while(true){
          //If we hit the edge or a white square, it's only a potential network.
          if(curSquare == null){
            for(Object item : squaresToChange){
              Square sq = (Square) item;
              sq.addBlackPotential();
            }
            break;
          //We need a seperate case anyway because if we hit a square we want to change its values too.
          }else if(curSquare.getPiece() == Square.WHITE){
            squaresToChange.insertBack(curSquare);
            for(Object item : squaresToChange){
              Square sq = (Square) item;
              sq.addBlackPotential();
            }
            break;
          //If we hit a black square, it's a network.
          }else if(curSquare.getPiece() == Square.BLACK){
            squaresToChange.insertBack(curSquare);
            for(Object item : squaresToChange){
              Square sq = (Square) item;
              sq.addBlackNetwork();
            }
            break;
          //This just leaves NONE, an empty square, in which case
          //we add the square to the potential and move on.
          }else{
            squaresToChange.insertBack(curSquare);
            curSquare = curSquare.adjacent(dir);
          }
        }
      }

      //Increment to the next mainSquare, depends on array being initialized to null
      i++;
      if(i == 10){
        break;
      }
      mainSquare = blackSquares[i];
    }

    //Repeat for white!
    i = 0;
    mainSquare = whiteSquares[i];
    while(mainSquare != null){
      for(int[] dir : DIRECTIONS){

        squaresToChange = new SList();
        curSquare = mainSquare.adjacent(dir);
        squaresToChange.insertBack(mainSquare);

        while(true){
          //If we hit the edge or a white square, it's only a potential network.
          if(curSquare == null){
            for(Object item : squaresToChange){
              Square sq = (Square) item;
              sq.addWhitePotential();
            }
            break;
          //We need a seperate case anyway because if we hit a square we want to change its values too.
          }else if(curSquare.getPiece() == Square.BLACK){
            squaresToChange.insertBack(curSquare);
            for(Object item : squaresToChange){
              Square sq = (Square) item;
              sq.addWhitePotential();
            }
            break;
         //it's a network! 
          }else if(curSquare.getPiece() == Square.WHITE){
            for(Object item : squaresToChange){
              Square sq = (Square) item;
              sq.addWhiteNetwork();
            }
            break;
          }else{
            squaresToChange.insertBack(curSquare);
            curSquare = curSquare.adjacent(dir);
          }
        }
      }
      i++;
      if(i == 10){
        break;
      }
      mainSquare = whiteSquares[i];
    }
  }

  /**
   * ALERT: All functions below are for testing purposes (storing and displaying the grid in
   * various ways). They don't impact the project, and are only worth looking at out of 
   * curiosity.
   **/


  /**
  *  Returns the grid as a string representation in one line.
  *  @return a one line String.
  **/
  String serializeToString(){
	    int tmp;
	    String out = "";
			for (int y = 0; y < DIMENSION; y++){
				for (int x = 0; x < DIMENSION; x++){
	        tmp = get(x, y).getPiece();
	        if(tmp == WHITE){
	          out = out.concat("W");
	        }else if(tmp == BLACK){
	          out = out.concat("B");
	        }else if(tmp == NONE){
	          out = out.concat(".");
	        }else{
	          System.out.println("Internal state warning in seralizeToString");
	        }
	      }
	    }
	    return out;
	}

  /**
  *  toString method that prints out Square with all its instance variables for debugging purposes.
  *  @return a String representation of the game board.
  **/
  public String toString(){
    //return simpleToString();
  	String s = "=========================================\n";
  	s+= "Stringified version:\n";
  	s+= serializeToString();
  	s+= "\n";
  	s+= "Code: <color ([W]hite,[B]lack)>:<blackNetworks>:<blackPotential>:<whiteNetworks>:<whitePotential>\n";
  	s += "-----------------------------------------";
  	for (int y = 0; y < DIMENSION; y++){
  		s+= "\n|";
  		for (int x = 0; x < DIMENSION; x++){
  			s += " "+get(x, y).toString()+" |";
  		}
  		s+="\n";
  	}
  	s += "-----------------------------------------";
  	return s;
  }

  /**
    *  Returns a string that is a simple representation of the board--Shows
    *  all the Squares on the board along with who occupies each Square.
    *  @return a String representation of the board.
    **/
  public String simpleToString(){

  	String s = "|=====================================|\n";
  	s+="|    || 0,| 1,| 2,| 3,| 4,| 5,| 6,| 7,|\n";
  	s += "|----||-------------------------------|\n";
  	for (int y = 0; y < DIMENSION; y++){
  		s+= "|----||-------------------------------|\n";
  		s+= "| "+y+"_ ||";
  		for (int x = 0; x < DIMENSION; x++){
  			if (get(x,y).getPiece()==BLACK){
  				s+=" B |";
  			}
  			if (get(x,y).getPiece()==WHITE){
  				s+=" W |";
  			}
  			if (get(x,y).getPiece()==NONE){
  				s+="   |";
  			}
  		}
  		s += "\n";
  	}
  	s += "|=====================================|\n";
  	s += "BLACK SQUARES: ";
  	for (Square a: blackSquares){
  		if (a == null){
  			s += "\nnull";
  		}
  		else {
  			s += "\n"+a.simpleToString();
  		}
  	}
  	s += "\n WHITE SQUARES: ";
  	for (Square b: whiteSquares){
  		if (b == null){
  			s += "\nnull";
  		}
  		else {
  			s += "\n"+b.simpleToString();
  		}
  	}
  	return s;
  }
  
  /**
  *  Print method that prints out arrays of squares.
  **/
  public static void printSquares(Square[] squares){
  	for(Square s: squares){
  		System.out.println(s);
  	}
  }
}








