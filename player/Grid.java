package player;

public class Grid{
	private static final int NONE = 2;
	private static final int WHITE = 1;
	private static final int BLACK = 0;
	private static final int DIMENSION = 8;
	private boolean add = true;
	private Square[][] board;
	private Square[] blackSquares = new Square[10];
	private Square[] whiteSquares = new Square[10];
	//put in length 10 array of black pieces
	//put in length 10 array of white pieces
	public Grid(){
		board = new Square[DIMENSION][DIMENSION];
		for(int i = 0; i < DIMENSION; i ++){
			for(int j = 0; j < DIMENSION; j++){
				board[i][j] = new Square(i,j,this);
			}
		}
		blackSquares = new Square[10];
	}

	public Square get(int x, int y){
		try{
			return board[x][y];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
		
	}

	// public Grid(String pieces){
	// 	board = new Square[DIMENSION][DIMENSION];
	// 	Square s;
	// 	int w = 0, b = 0;
	// 	for (int x = 0; x < DIMENSION; x++){
	// 		for (int y = 0; y < DIMENSION; y++){
	// 			s = new Square(x, y, Integer.parseInt(pieces[0]), this);
	// 			board[x][y] = s;
	// 			if (pieces[0] == WHITE){
	// 				whiteSquares[w] = s;
	// 				w++;
	// 			}
	// 			if (pieces[0] == BLACK){
	// 				blackSquares[b] = s;
	// 				b++;
	// 			}
	// 			pieces = pieces.substring(1, pieces.length());
	// 		}
	// 	}

	// 	}

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

	public String toString(){
		String s = "-----------------------------------------";
		for (int x = 0; x < DIMENSION; x++){
			s+= "\n|";
			for (int y = 0; x < DIMENSION; y++){
				if (get(x, y).getPiece() == WHITE){
					s+=" W |";
				}
				if (get(x, y).getPiece() == BLACK) {
					s+= " B |";
				}
				else {
					s+="   |";
				}
			}
			s+="\n";
		}
		s += "-----------------------------------------";
		return s;
	}

}