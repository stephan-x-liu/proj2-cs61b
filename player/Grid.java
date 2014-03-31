package player;
import java.lang.Math;
import list.*;

public class Grid{
  //These fields must be default protected so we can test stuff properly
	static final int BLACK = 0;
	static final int WHITE = 1;
	static final int NONE = 2;
	public static final int DIMENSION = 8;
	protected boolean add = true;
	private Square[][] board;
	public Square[] blackSquares = new Square[10];
  	protected int blackSquareCount = 0;
	public Square[] whiteSquares = new Square[10];
  	protected int whiteSquareCount = 0;
  	static final int[][] DIRECTIONS = Square.DIRECTIONS;
	
	//put in length 10 array of black pieces
	//put in length 10 array of white pieces
	
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
					if (blackSquareCount>=10){
						add = false;
					}
		        }
		        if(model[i][j] == WHITE){
						whiteSquares[whiteSquareCount] = board[i][j];
						whiteSquareCount++;
		        }
	    		board[i][j].setPiece(model[i][j]);
			}
		}

	}

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
 	
 	public Grid cloneGrid(){
 		Grid g = new Grid();
 		for (int x = 0; x < DIMENSION; x++){
 			for (int y = 0; y < DIMENSION; y++){
  				g.set(x,y,get(x,y).getPiece());
 			}
 		}
 		return g;
 	}

	public Square get(int x, int y){
		try {
			return board[x][y];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	public void set(int x, int y, int color){
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

	public int getGoalZones(int color){
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

	public void makeMove(Move move, int color){
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

	public Move[] validMoves(int color){
		Move[] validMoves = new Move[300];
		int moveIndex = 0;
		Move move;
		if (add){
			for (int x = 0; x < DIMENSION; x++){
				for (int y = 0; y < DIMENSION; y++){
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
			squares[1] = whiteSquares;
			squares[0] = blackSquares;
			for (Square add: squares[color]){
				if (add != null){
					for (int x = 0; x < DIMENSION; x++){
						for (int y = 0; y < DIMENSION && moveIndex < 300; y++){
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

	public boolean isValidMove(Move move, int color){
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
   * Resets the black/white potential and networks for all squares.
   **/

	public void resetSquaresPN(){
		for (int x = 0; x < DIMENSION; x++){
			for (int y = 0; y < DIMENSION; y++){
       			get(x, y).resetPN();
      		}
    	}
	}

  private int fComputePotential(int count){
    //Heavy bias for overlap because we can make great networks out of overlap.
    //We subtract one because potential doesn't really help us unless it's overlapping.
    //We'll see if that's good.
    if(count == 0){
      return 0;
    }
    return (count-1)*(count-1);
  }

  private int fComputeNetwork(int count){
    //Weigh it more heavily (2*) with heavy bias for overlap.
    return 2*count*count;
  }

  private int eComputePotential(int count){
    //Negative because it's bad, bias towards overlap because overlap bad.
    return -1* count*count;
  }

  private int eComputeNetwork(int count){
    //The less networks the better
    return -4* count*count;
  }

  public int maxNetworkLength(int color){
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

  public Network getNetwork(Square current){
  	Square[] network = new Square[10];
  	int length = 0;
  	int[] temp = {0,0};
  	Network n = findNetwork(current,network,length,temp);
  	return n;
  }

  public boolean hasWinningNetwork(int color){
  	int goal1 = 1;
  	int goal2 = 0;
  	if(color == BLACK){
  		for(int i = 1; i < DIMENSION-1; i++){
  			Network n = getNetwork(get(i,0));

  			if(n.length>=6){
  				for(int k = 1; k < n.length; k++){
  					if(n.network[k].position()[1]==7)
  						goal2++;
  				}
  				for(int j = 1; j < n.length; j++){
  					if(n.network[j].position()[1]==0)
  						goal1++;
  				}
  				if(goal1==1 && goal2==1){
  					return true;
  				}
  				goal1=1;
  				goal2=0;
  			}
  		}
  	}
  	else{
  		for(int i = 1; i < DIMENSION-1; i++){
  			Network n = getNetwork(get(0,i));
  			if(n.length>=6){
  				for(int k = 1; k < n.length; k++){
  					if(n.network[k].position()[0]==7)
  						goal2++;
  				}
  				for(int j = 1; j < n.length; j++){
  					if(n.network[j].position()[0]==0)
  						goal1++;
  				}
  				if(goal1==1 && goal2==1){
  					return true;
  				}
  				goal1=1;
  				goal2=0;
  			}
  		}
  	}
  	return false;
  }


  public int networkLength(Square current){
  	Square[] network = new Square[10];
  	int length = 0;
  	int[] temp = {0,0};
  	Network n = findNetwork(current,network,length,temp);
  	return n.length;
  }

  private Network findNetwork(Square current, Square[] network, int length, int[] prev_dir){
  	int num_connects = 0;
  	Square[] connections = new Square[8];
  	int[][] dirConnection = new int[8][2];
  	for(int[] direction : DIRECTIONS){
  		Square temp = current.getInDirection(direction);
  		if(temp!=null&&temp.getPiece()==current.getPiece()){
  			boolean already_connected = false;
  			for(Square prev: network){
  				if(prev!= null && temp.samePlace(prev)){
  					already_connected = true;
  				}
  			}
  			if(!already_connected&&(prev_dir[0]!=direction[0]||prev_dir[1]!=direction[1])){
	  			connections[num_connects] = temp;
	  			dirConnection[num_connects] = direction;
	  			num_connects++;
	  		}
  		}
  	}
  	if(num_connects==0){
  		network[length] = current;
  		Network temp = new Network(network,length+1);
  		return temp;
  	}
  	int score = 0;
  	Network longest = null;
  	for(int i = 0; i< num_connects; i++){
  		network[length] = current;
  		Network s = findNetwork(connections[i],network,length+1, dirConnection[i]);
  		if(s.length>score){
  			score=s.length;
  			longest = s;
  		}
  	}
  	return longest;

  }



  public int evaluate(int friendly){
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

    for (int y = 0; y < DIMENSION; y++){
      for (int x = 0; x < DIMENSION; x++){
        Square sq = get(x, y);
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
    int networkEncourage = 10*maxNetworkLength(friendly);
    int multiplier = getGoalZones(friendly);
    int emultiplier = getGoalZones(enemy);
    return multiplier*(fComputedPotential+fComputedNetwork)+emultiplier*(eComputedPotential+eComputedNetwork);
        
    //We make seperate functions so we can change the algorithm for each.

  }

	public void updateNetworkList(){
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
				//s += " "+get(x,y).getPiece()+" |";
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
	

	public static void main(String[] args){
		Grid g = new Grid();
		g.set(1,2,BLACK);
		g.set(2,5,BLACK);
		g.set(3,1,BLACK);
		g.set(3,5,BLACK);
		g.set(5,1,BLACK);
		g.set(5,5,BLACK);
		g.set(6,0,BLACK);
		g.set(6,4,BLACK);
		g.set(0,1,WHITE);
		g.set(0,4,WHITE);
		g.set(1,3,WHITE);
		g.set(2,1,WHITE);
		g.set(3,6,WHITE);
		g.set(4,1,WHITE);
		g.set(4,3,WHITE);
		g.set(7,4,WHITE);

		System.out.println(g.simpleToString());
		System.out.println(g.networkLength(g.get(4,0)));

		System.out.println(g.maxNetworkLength(BLACK));
		System.out.println(g.maxNetworkLength(WHITE));
		System.out.println(g.hasWinningNetwork(BLACK));
		System.out.println(g.hasWinningNetwork(WHITE));
	}
}

class Network{
	Square[] network;
	int length;
	public Network(Square[] n, int l){
		length = l;
		network = n;
	}
}






