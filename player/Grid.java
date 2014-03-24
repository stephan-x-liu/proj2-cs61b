package player;
import list.*;

public class Grid{
  //These fields must be default protected so we can test stuff properly
	static final int NONE = 2;
	static final int WHITE = 1;
	static final int BLACK = 0;
	public static final int DIMENSION = 8;
	private boolean add = true;
	private Square[][] board;
	private Square[] blackSquares = new Square[10];
  private int blackSquareCount;
	private Square[] whiteSquares = new Square[10];
  private int whiteSquareCount;
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
	}

  public Grid(int[][] model){
    board = new Square[DIMENSION][DIMENSION];
		for(int i = 0; i < DIMENSION; i ++){
			for(int j = 0; j < DIMENSION; j++){
				board[i][j] = new Square(i,j,this);
        //For every single x and y value, check if it's black and white, then set the pieces.
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

	public Square get(int x, int y){
		try{
			return board[x][y];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		
	}

	public Grid(String pieces){
    pieces = pieces.replaceAll("W", Integer.toString(WHITE));
    pieces = pieces.replaceAll("B", Integer.toString(BLACK));
    pieces = pieces.replaceAll(".", Integer.toString(NONE));
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
	 			}
        i++;
	 		}
	 	}

 	}

	// private Square neighbor(int x, int y){
	// 	return board[x][y].neighbor();
	// }

	// public Square[] validMoves(int piece){

	// }

	public boolean isValidMove(Move move, int color){
		if (board[move.x1][move.y1].hasPiece()){
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
		if (move.moveKind==1){
			Square neighbor = board[move.x1][move.y1].neighbor(color);
			if (neighbor != null){
				if (neighbor.neighbor(color) != null){
					return false;
				}
			}
		}
		if (move.moveKind==2){ //step move
			if (move.x1==move.x2 || move.y1==move.y2){ //stepping to same square
				return false;
			}
			board[move.x2][move.y2].removePiece();
			Square neighbor = board[move.x1][move.y1].neighbor(color);
			if (neighbor != null){
				if (neighbor.neighbor(color) != null){
					return false;
				}
			}
			board[move.x2][move.y2].setPiece(color);
		}
		return true;
	}

	public Move[] validStepMoves(int color){
		Move[] validMoves = new Move[240]; //DIMENSION OF ARRAY?????
		int moveIndex = 0;
		Square[][] squares = new Square[3][10];
		squares[1] = whiteSquares;
		squares[0] = blackSquares;
		Move move;
		for (Square add: squares[color]){
			for (int x = 0; x < DIMENSION; x++){
				for (int y = 0; y < DIMENSION; y++){
					move = new Move(add.position()[0], add.position()[1], x, y);
					if (isValidMove(move, color)){
						validMoves[moveIndex] = move;
						moveIndex++;
					}
				}
			}
		}
		return validMoves;
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

        //Iterate through all squares on the path.
        while(true){
          //If we hit the edge or a white square, it's only a potential network.
          if(curSquare == null || curSquare.getPiece() == Square.WHITE){
            for(Object item : squaresToChange){
              Square sq = (Square) item;
              sq.addBlackPotential();
            }
            break;
          //If we hit a black square, it's a network.
          }else if(curSquare.getPiece() == Square.BLACK){
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
      mainSquare = blackSquares[i];
    }

    //Repeat for white!
    i = 0;
    mainSquare = whiteSquares[i];
    while(mainSquare != null){
      for(int[] dir : DIRECTIONS){
        
        squaresToChange = new SList();
        curSquare = mainSquare.adjacent(dir);

        while(true){
          if(curSquare == null || curSquare.getPiece() == Square.BLACK){
            for(Object item : squaresToChange){
              Square sq = (Square) item;
              sq.addWhitePotential();
            }
            break;
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
      mainSquare = whiteSquares[i];
    }
  }
  

	public Move[] validAddMoves(int color){
		Move[] validMoves = new Move[64];
		int moveIndex = 0;
		Move move;
		for (int x = 0; x < DIMENSION; x++){
			for (int y = 0; y < DIMENSION; y++){
				move = new Move(x, y);
				if (isValidMove(move, color)){
					validMoves[moveIndex] = move;
					moveIndex++;
				}
			}
		}
		return validMoves;
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
        /*
				if (get(x, y).getPiece() == WHITE){
					s+=" W |";
				}
				if (get(x, y).getPiece() == BLACK) {
					s+= " B |";
				}
				else {
					s+="   |";
				}
        */
			}
			s+="\n";
		}
		s += "-----------------------------------------";
		return s;
	}

}
