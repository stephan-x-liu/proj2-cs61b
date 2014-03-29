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

 	public int[][] board(){
 		int[][] temp = new int[DIMENSION][DIMENSION];
 		for(int i = 0; i < DIMENSION; i ++){
			for(int j = 0; j < DIMENSION; j++){
				temp[i][j] = board[i][j].getPiece();
			}
		}
		return temp;
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

	public void makeMove(Move move, int color){
		if ((move != null)){
			if (move.moveKind == Move.STEP){
				board[move.x2][move.y2].removePiece();
				board[move.x1][move.y1].setPiece(color);
				for (int a=0; a<10; a++){
					if (color==BLACK){
						if (blackSquares[a]==board[move.x2][move.y2]){
							blackSquares[a] = board[move.x1][move.y1];
							break;
						}
					}
					if (color==WHITE){
						if (whiteSquares[a]==board[move.x2][move.y2]){
							whiteSquares[a] = board[move.x1][move.y1];
							break;
						}
					}
				}
			}
			if (move.moveKind==Move.ADD) {
				board[move.x1][move.y1].setPiece(color);
				if (color==BLACK){
					blackSquares[blackSquareCount] = board[move.x1][move.y1];
					blackSquareCount++;
					if (blackSquareCount==10){
						add = false;
					}

				}
				if (color==WHITE){
					whiteSquares[whiteSquareCount] = board[move.x1][move.y1];
					whiteSquareCount++;
					// for (Square a: blackSquares){
						// System.out.println(a);
					// }
				}
			}
		}
		// else {
		// 	if (color==BLACK){
		// 		System.out.println("Player BLACK is trying to make an invalid move!");
		// 	}
		// 	else {
		// 		System.out.println("Player WHITE is trying to make an invalid move!");
		// 	}
		// 	System.out.println(" ERROR " + move.error);
		// }
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
			for (int i = 0; i < squares[color].length && squares[color][i]!= null; i++){
				Square add = squares[color][i];
				for (int x = 0; x < DIMENSION; x++){
					for (int y = 0; y < DIMENSION && moveIndex < 64; y++){
						move = new Move(x, y, add.position()[0], add.position()[1]);
						if (isValidMove(move, color)){
							validMoves[moveIndex] = move;
							moveIndex++;
						}
					}
				}
			}
		}
		return validMoves;
	}

	public boolean isValidMove(Move move, int color){
		if(move == null){
			//move.error=("INVALID: null move");
			return false;
		}
		if (get(move.x1, move.y1).hasPiece()){
			move.error=("INVALID: has piece at" + move.x1 + ","+move.y1 + " " + get(move.x1, move.y1).getPiece());
			return false;
		}
		if (move.x1 == 0){ //checking four corners 
			if (move.y1 == 0 || move.y1 == 7){
				move.error=("INVALID: corner");
				return false;
			}
		}
		if (move.y1 == 0){
			if (move.x1 == 0 || move.x1 == 7){
				move.error=("INVALID: corner");
				return false;
			}
		}
		if (color == WHITE){ //checking opponent goals
			if (move.y1 == 0 || move.y1 == 7){
				move.error=("INVALID: opponent goal");
				return false;
			}
		}
		if (color == BLACK){
			if (move.x1 == 0 || move.x1 == 7){
				move.error=("INVALID: opponent goal");
				return false;
			}
		}
		if (move.moveKind==Move.ADD){
			Square[] neighbors = board[move.x1][move.y1].neighbor(color);
			if(!add){
				return false;
			}
			if(neighbors[1]!=null){
				move.error=("INVALID: neighbors");
				return false;
			}
			if (neighbors[0] != null){
				if (neighbors[0].neighbor(color)[0] != null){
					move.error=("INVALID: neighbors2");
					return false;
				}
			}
		}
		if (move.moveKind==Move.STEP){
			if (add){
				move.error=("INVALID: stepped when should add");
				return false;
			}
			if ((board[move.x2][move.y2]).getPiece() != color){
				move.error="INVALID: trying to step opponent's piece";
				return false;
			}	
			if (move.x1==move.x2 || move.y1==move.y2){ //stepping to same square
				move.error=("INVALID: stepping on same square");
				return false;
			}
			board[move.x2][move.y2].removePiece();
			Square[] neighbors = get(move.x1, move.y1).neighbor(color);
			if(neighbors[1]!=null){
				move.error=("INVALID: neighbors");
				return false;
			}
			if (neighbors[0] != null){
				if (neighbors[0].neighbor(color)[0] != null){
					move.error=("INVALID: neighbors2");
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

  public boolean hasWinningNetwork(){
    return false;
  }

  public int evaluate(){
    return (int)Math.floor(100*Math.random());
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
		return simpleToString();
		// String s = "=========================================\n";
	 //    s+= "Stringified version:\n";
	 //    s+= serializeToString();
	 //    s+= "\n";
	 //    s+= "Code: <color ([W]hite,[B]lack)>:<blackNetworks>:<blackPotential>:<whiteNetworks>:<whitePotential>\n";
	 //    s += "-----------------------------------------";
		// for (int y = 0; y < DIMENSION; y++){
		// 	s+= "\n|";
		// 	for (int x = 0; x < DIMENSION; x++){
	 //       		s += " "+get(x, y).toString()+" |";
		// 	}
		// 	s+="\n";
		// }
		// s += "-----------------------------------------";
		// return s;
	}

	public String simpleToString(){

	    String s = "==================================\n";
	    s+="   | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |";
		for (int y = 0; y < DIMENSION; y++){
			s+= "\n----------------------------------\n";
			s+= y+"_ |";
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
		}
		s += "\n==================================";
		s += "\n BLACK SQUARES: ";
		for (Square a: blackSquares){
			s += "\n"+a;
		}
		s += "\n WHITE SQUARES: ";
		for (Square b: whiteSquares){
			s += "\n"+b;
		}

		return s;
	}
	

	// public static void main(String[] args){
	// 	Grid g = new Grid();
	// 	g.set(1,5,WHITE);
	// 	g.set(6,3,BLACK);
	// 	g.set(6,4,WHITE);
	// 	g.set(3,6,BLACK);
	// 	g.set(0,5,WHITE);
	// 	g.set(0,3,BLACK);
	// 	g.set(0,4,WHITE);
	// 	g.set(0,6,BLACK);
	// 	g.set(2,5,WHITE);
	// 	g.set(2,3,BLACK);
	// 	g.set(2,4,WHITE);
	// 	g.set(2,6,BLACK);
	// 	g.set(4,5,WHITE);
	// 	g.set(4,3,BLACK);
	// 	g.set(4,4,WHITE);
	// 	g.set(4,6,BLACK);
	// 	g.set(7,5,WHITE);
	// 	g.set(7,3,BLACK);
	// 	g.set(7,4,WHITE);
	// 	g.set(7,6,BLACK);
	// 	System.out.println(g);
	// 	Grid a = g.cloneGrid();
	// 	System.out.println(a);

	// 	System.out.println(g.add);
	// 	System.out.println(a.add);
	// }

}
